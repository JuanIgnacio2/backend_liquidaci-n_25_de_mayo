package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.*;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiquidacionSueldosService {
    private final EmpleadoRepository empleadoRepository;
    private final BonificacionFijaRepository bonificacionFijaRepository;
    private final BonificacionAreaLyFRepository bonificacionAreaRepository;
    private final DescuentoRepository descuentoRepository;
    private final PagoSueldoRepository pagoSueldoRepository;
    private final PagoConceptoRepository pagoConceptoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CategoriasZonasUocraRepository categoriaZonaUocraRepository;

    public Optional<PagoSueldo> obtenerPagoPorId(Long idPago){
        return pagoSueldoRepository.findById(idPago);
    }

    public List<PagoSueldo> listarPagosPorEmpleado(Integer legajo) {
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return pagoSueldoRepository.findByEmpleado(empleado);
    }

    @Transactional
    public PagoSueldo liquidarSueldo(LiquidacionSueldoDTO dto, LocalDate fechaPago) {
        Empleado empleado = empleadoRepository.findById(dto.getLegajo())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        String gremio = empleado.getGremio().getNombre().toUpperCase();
        BigDecimal basico;
        TipoConcepto tipoBasico;

        if (gremio.contains("UOCRA")) {
            CategoriasZonasUocra categoriaZona = categoriaZonaUocraRepository
                    .findByCategoriaAndZona(empleado.getCategoria(), empleado.getZona())
                    .orElseThrow(() -> new RuntimeException("No se encontró categoría-zona para UOCRA"));

            basico = categoriaZona.getBasico();
            tipoBasico = TipoConcepto.CATEGORIA_ZONA;
        } else {
            basico = empleado.getCategoria().getBasico();
            tipoBasico = TipoConcepto.CATEGORIA;
        }

        BigDecimal totalBonificaciones = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;

        PagoSueldo pago = new PagoSueldo();
        pago.setEmpleado(empleado);
        pago.setPeriodoPago(dto.getPeriodoPago());
        pago.setFechaPago(fechaPago);
        pago.setTotal_bruto(BigDecimal.ZERO);
        pago.setTotal_descuentos(BigDecimal.ZERO);
        pago.setTotal_neto(BigDecimal.ZERO);
        pago = pagoSueldoRepository.save(pago);

        // Sueldo básico
        crearConcepto(pago, tipoBasico.name(), empleado.getCategoria().getIdCategoria(), 1, basico, basico);

        //Básico referencia de categoria 11
        Categoria categoriaReferencia = categoriaRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        BigDecimal basicoReferencia = categoriaReferencia.getBasico();

        // Bonificaciones específicas Luz y Fuerza
        if (gremio.contains("LUZ")) {
            for (Area area : empleado.getAreas()) {
                List<BonificacionAreaLyF> bonificaciones = bonificacionAreaRepository
                        .findByArea_IdAreaAndCategoria_IdCategoria(
                                area.getIdArea(),
                                empleado.getCategoria().getIdCategoria() // CORREGIDO
                        );

                for (BonificacionAreaLyF bonif : bonificaciones) {
                    BigDecimal monto = basicoReferencia.multiply(bonif.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalBonificaciones = totalBonificaciones.add(monto);

                    crearConcepto(pago, TipoConcepto.BONIFICACION_VARIABLE.name(),
                            bonif.getIdBonificacionVariable(), 1, monto, monto);
                }
            }
        }

        // Bonificaciones / descuentos manuales
        for (ConceptoInputDTO conceptoDTO : dto.getConceptos()) {

            String tipo = conceptoDTO.getTipoConcepto();
            if(tipo.equals("CATEGORIA") || tipo.equals("BONIFICACION_VARIABLE")) continue;
            Integer idRef = conceptoDTO.getIdReferencia();
            Integer unidades = Optional.ofNullable(conceptoDTO.getUnidades()).orElse(1);

            BigDecimal montoUnitario = BigDecimal.ZERO;

            switch (tipo) {
                case "BONIFICACION_FIJA" -> {
                    BonificacionFija bonFija = bonificacionFijaRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Bonificación fija no encontrada"));
                    montoUnitario = basicoReferencia.multiply(bonFija.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                }
                case "DESCUENTO" -> {
                    Descuentos desc = descuentoRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
                    BigDecimal base = basico.add(totalBonificaciones);
                    montoUnitario = base.multiply(desc.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalDescuentos = totalDescuentos.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                }
                default -> {}
            }

            BigDecimal totalConcepto = montoUnitario.multiply(BigDecimal.valueOf(unidades));
            crearConcepto(pago, tipo, idRef, unidades, montoUnitario, totalConcepto);
        }

        // Totales
        BigDecimal totalBruto = basico.add(totalBonificaciones);
        BigDecimal totalNeto = totalBruto.subtract(totalDescuentos);

        pago.setTotal_bruto(totalBruto);
        pago.setTotal_descuentos(totalDescuentos);
        pago.setTotal_neto(totalNeto);

        pagoSueldoRepository.save(pago);

        // Evitar bucles de serialización
        PagoSueldo response = new PagoSueldo();
        response.setIdPago(pago.getIdPago());
        response.setPeriodoPago(pago.getPeriodoPago());
        response.setFechaPago(pago.getFechaPago());
        response.setTotal_bruto(totalBruto);
        response.setTotal_descuentos(totalDescuentos);
        response.setTotal_neto(totalNeto);
        return response;
    }

    private void crearConcepto(PagoSueldo pago, String tipo, Integer idReferencia,
                               Integer unidades, BigDecimal montoUnitario, BigDecimal total) {

        PagoConcepto concepto = new PagoConcepto();
        concepto.setPago(pago);
        concepto.setTipoConcepto(TipoConcepto.valueOf(tipo));
        concepto.setUnidades(unidades);
        concepto.setMontoUnitario(montoUnitario);
        concepto.setTotal(total);

        // Asignar la entidad correspondiente según el tipo
        switch (tipo) {
            case "CATEGORIA" -> {
                if (idReferencia != null) {
                    categoriaRepository.findById(idReferencia)
                            .ifPresent(concepto::setCategoria);
                }
            }
            case "CATEGORIA_ZONA" -> {
                if (idReferencia != null) {
                    categoriaZonaUocraRepository.findById(idReferencia)
                            .ifPresent(concepto::setCategoriaZonaUocra);
                }
            }
            case "BONIFICACION_FIJA" -> {
                if (idReferencia != null) {
                    bonificacionFijaRepository.findById(idReferencia)
                            .ifPresent(concepto::setBonificacionFija);
                }
            }
            case "BONIFICACION_VARIABLE" -> {
                if (idReferencia != null) {
                    bonificacionAreaRepository.findById(idReferencia)
                            .ifPresent(concepto::setBonificacionAreaLyF);
                }
            }
            case "DESCUENTO" -> {
                if (idReferencia != null) {
                    descuentoRepository.findById(idReferencia)
                            .ifPresent(concepto::setDescuento);
                }
            }
        }

        pagoConceptoRepository.save(concepto);
    }

    public PagoSueldoDetalleDTO obtenerPagoConDetalle(Integer idPago) {
        PagoSueldo pago = pagoSueldoRepository.findById(Long.valueOf(idPago))
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        PagoSueldoDetalleDTO dto = new PagoSueldoDetalleDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setPeriodoPago(pago.getPeriodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setTotal(pago.getTotal_neto());

        Empleado e = pago.getEmpleado();
        dto.setLegajoEmpleado(e.getLegajo());
        dto.setNombreEmpleado(e.getNombre());
        dto.setApellidoEmpleado(e.getApellido());
        dto.setCategoriaEmpleado(e.getCategoria().getNombre());

        List<PagoConceptoDTO> conceptos = pagoConceptoRepository.findByPago(pago)
                .stream()
                .map(pc -> {
                    PagoConceptoDTO c = new PagoConceptoDTO();
                    c.setTipoConcepto(pc.getTipoConcepto().name());
                    c.setUnidades(pc.getUnidades());
                    c.setMontoUnitario(pc.getMontoUnitario());
                    c.setTotal(pc.getTotal());

                    // Nombre del concepto según el tipo
                    switch (pc.getTipoConcepto()) {
                        case BONIFICACION_FIJA -> {
                            if (pc.getBonificacionFija() != null) {
                                c.setNombreConcepto(pc.getBonificacionFija().getNombre());
                                c.setId_Bonificacion_Fija(pc.getBonificacionFija().getIdBonificacionFija());
                            }
                        }
                        case BONIFICACION_VARIABLE -> {
                            if (pc.getBonificacionAreaLyF() != null) {
                                var bonif = pc.getBonificacionAreaLyF();
                                String nombreArea = bonif.getArea() != null ? bonif.getArea().getNombre() : "Área desconocida";
                                String nombreCategoria = bonif.getCategoria() != null ? bonif.getCategoria().getNombre() : "Categoría desconocida";
                                c.setNombreConcepto("Bonificación Variable - " + nombreArea + " / " + nombreCategoria);
                                c.setId_Bonificacion_Area(bonif.getIdBonificacionVariable());
                            } else {
                                c.setNombreConcepto("Bonificación Variable");
                            }
                        }
                        case DESCUENTO -> {
                            if (pc.getDescuento() != null) {
                                c.setNombreConcepto(pc.getDescuento().getNombre());
                                c.setId_Categoria(pc.getDescuento().getIdDescuento());
                            }
                        }
                        case CATEGORIA -> {
                            if (pc.getCategoria() != null) {
                                c.setNombreConcepto("Sueldo Básico - " + pc.getCategoria().getNombre());
                                c.setId_Bonificacion_Fija(pc.getCategoria().getIdCategoria());
                            } else {
                                c.setNombreConcepto("Sueldo Básico");
                            }
                        }
                        case CATEGORIA_ZONA -> {
                            if (pc.getCategoriaZonaUocra() != null) {
                                c.setNombreConcepto("Categoría-Zona: " + pc.getCategoriaZonaUocra().getCategoria().getNombre()
                                        + " - " + pc.getCategoriaZonaUocra().getZona().getNombre());
                                c.setId_Categoria_Zona_Uocra(pc.getCategoriaZonaUocra().getId());
                            } else {
                                c.setNombreConcepto("Categoría-Zona");
                            }
                        }
                        default -> c.setNombreConcepto("Concepto desconocido");
                    }

                    return c;
                })
                .collect(Collectors.toList());

        dto.setConceptos(conceptos);
        return dto;
    }

    public List<PagoSueldoResumenDTO> listarTodosLosPagos() {
        List<PagoSueldo> pagos = pagoSueldoRepository.findAll();

        return pagos.stream().map(pago -> {
            PagoSueldoResumenDTO dto = new PagoSueldoResumenDTO();
            dto.setIdPago(pago.getIdPago());
            dto.setLegajoEmpleado(pago.getEmpleado().getLegajo());
            dto.setNombreEmpleado(pago.getEmpleado().getNombre());
            dto.setApellidoEmpleado(pago.getEmpleado().getApellido());
            dto.setPeriodoPago(pago.getPeriodoPago());
            dto.setFechaPago(pago.getFechaPago());
            dto.setTotal_neto(pago.getTotal_neto());
            return dto;
        }).collect(Collectors.toList());
    }
}