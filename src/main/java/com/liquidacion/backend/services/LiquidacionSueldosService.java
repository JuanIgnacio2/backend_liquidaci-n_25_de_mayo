package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.*;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
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
    private final BonificacionAreaRepository bonificacionAreaRepository;
    private final DescuentoRepository descuentoRepository;
    private final PagoSueldoRepository pagoSueldoRepository;
    private final PagoConceptoRepository pagoConceptoRepository;
    private final CategoriaRepository categoriaRepository;

    public Optional<PagoSueldo> obtenerPagoPorId(Long idPago){
        return pagoSueldoRepository.findById(idPago);
    }

    public List<PagoSueldo> listarPagosPorEmpleado(Integer legajo) {
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return pagoSueldoRepository.findByEmpleado(empleado);
    }

    @Transactional
    public PagoSueldo liquidarSueldo(LiquidacionSueldoDTO dto, LocalDate fechaPago){
        Empleado empleado = empleadoRepository.findById(dto.getLegajo())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        BigDecimal basico = empleado.getCategoria().getBasico();
        BigDecimal totalBonificaciones = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;

        PagoSueldo pago = new PagoSueldo();
        pago.setEmpleado(empleado);
        pago.setPeriodoPago(dto.getPeriodoPago());
        pago.setFechaPago(fechaPago);
        pago.setTotal(BigDecimal.ZERO);
        pago = pagoSueldoRepository.save(pago);

        //Concepto 1: Sueldo básico
        //crearConcepto(pago, "BASICO", null, 1, basico, basico);
        crearConcepto(pago, TipoConcepto.CATEGORIA.name(), empleado.getCategoria().getIdCategoria(), 1, basico, basico);

        //Bonificaciones y descuentos
        for(ConceptoInputDTO conceptoDTO : dto.getConceptos()){
            String tipo = conceptoDTO.getTipoConcepto();
            Integer idRef = conceptoDTO.getIdReferencia();
            Integer unidades = conceptoDTO.getUnidades() != null ? conceptoDTO.getUnidades() : 1;

            BigDecimal montoUnitario = BigDecimal.ZERO;

            switch (tipo){
                case "BONIFICACION_FIJA":
                    BonificacionFija bonFija = bonificacionFijaRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Bonificacion no encontrada"));
                    montoUnitario = basico.multiply(bonFija.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                    break;

                case "BONIFICACION_VARIABLE":
                    BonificacionArea bonVar = bonificacionAreaRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Bonificacion no encontrada"));
                    montoUnitario = basico.multiply(bonVar.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                    break;

                case "DESCUENTO":
                    Descuento desc = descuentoRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
                    BigDecimal base = basico.add(totalBonificaciones);
                    montoUnitario = base.multiply(desc.getPorcentaje()
                            .divide(BigDecimal.valueOf(100)));
                    totalDescuentos = totalDescuentos.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                    break;

                case "CATEGORIA":
                    // Si se encuentra "CATEGORIA", se omite, porque ya se añadió antes del for
                    continue;
                default:
                    throw new RuntimeException("Tipo de concepto no encontrado");
            }

            BigDecimal totalConcepto = montoUnitario.multiply(BigDecimal.valueOf(unidades));
            crearConcepto(pago, tipo, idRef, unidades, montoUnitario, totalConcepto);
        }

        //Total neto
        BigDecimal totalNeto = basico.add(totalBonificaciones).subtract(totalDescuentos);
        pago.setTotal(totalNeto);
        return pagoSueldoRepository.save(pago);
    }

    private void crearConcepto(PagoSueldo pago, String tipo, Integer idReferencia,
                               Integer unidades, BigDecimal montoUnitario, BigDecimal total) {
        PagoConcepto concepto = new PagoConcepto();
        concepto.setPago(pago);
        concepto.setTipoConcepto(TipoConcepto.valueOf(tipo));
        concepto.setIdReferencia(idReferencia);
        concepto.setUnidades(unidades);
        concepto.setMontoUnitario(montoUnitario);
        concepto.setTotal(total);
        pagoConceptoRepository.save(concepto);
    }

    public PagoSueldoDetalleDTO obtenerPagoConDetalle(Integer idPago){
        PagoSueldo pago = pagoSueldoRepository.findById(Long.valueOf(idPago))
                .orElseThrow(() -> new RuntimeException("Pago de concepto no encontrado"));
        PagoSueldoDetalleDTO dto = new PagoSueldoDetalleDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setPeriodoPago(pago.getPeriodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setTotal(pago.getTotal());

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

                    // Obtener nombre del concepto según tipo
                    if (pc.getTipoConcepto() == TipoConcepto.BONIFICACION_FIJA) {
                        bonificacionFijaRepository.findById(pc.getIdReferencia())
                                .ifPresent(b -> c.setNombre(b.getNombre()));
                    } else if (pc.getTipoConcepto() == TipoConcepto.BONIFICACION_VARIABLE) {
                        c.setNombre("Bonificación Variable");
                    } else if (pc.getTipoConcepto() == TipoConcepto.DESCUENTO) {
                        descuentoRepository.findById(pc.getIdReferencia())
                                .ifPresent(d -> c.setNombre(d.getNombre()));
                    } else if (pc.getTipoConcepto() == TipoConcepto.CATEGORIA) {
                        categoriaRepository.findById(pc.getIdReferencia())
                                .ifPresent(cat -> c.setNombre("Sueldo Básico - " + cat.getNombre()));
                    }

                    return c;
                }).collect(Collectors.toList());

        dto.setConceptos(conceptos);
        return dto;
    }

    public List<PagoSueldoDTO> listarTodosLosPagos() {
        List<PagoSueldo> pagos = pagoSueldoRepository.findAll();

        return pagos.stream().map(pago -> {
            PagoSueldoDTO dto = new PagoSueldoDTO();
            dto.setIdPago(pago.getIdPago());
            dto.setLegajoEmpleado(pago.getEmpleado().getLegajo());
            dto.setNombreEmpleado(pago.getEmpleado().getNombre());
            dto.setApellidoEmpleado(pago.getEmpleado().getApellido());
            dto.setPeriodoPago(pago.getPeriodoPago());
            dto.setFechaPago(pago.getFechaPago());
            dto.setTotal(pago.getTotal());
            return dto;
        }).collect(Collectors.toList());
    }
}