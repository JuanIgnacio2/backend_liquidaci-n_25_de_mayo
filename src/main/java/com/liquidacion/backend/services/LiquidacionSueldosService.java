package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.*;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiquidacionSueldosService {
    private final EmpleadoRepository empleadoRepository;
    private final ConceptosLyFRepository conceptosLyFRepository;
    private final ConceptosUocraRepository conceptosUocraRepository;
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

    public List<PagoSueldo> listarPagosPorPeriodo(String periodo){
        return pagoSueldoRepository.findByPeriodoPago(periodo);
    }

    public boolean existeLiquidacionPorPeriodo(Integer legajo, String periodoPago) {
        return pagoSueldoRepository.existsByEmpleado_LegajoAndPeriodoPago(legajo, periodoPago);
    }

    public Optional<PagoSueldo> obtenerLiquidacionPorPeriodo(Integer legajo, String periodoPago) {
        return pagoSueldoRepository.findByEmpleado_LegajoAndPeriodoPago(legajo, periodoPago);
    }

    @Transactional
    public PagoSueldo liquidarSueldo(LiquidacionSueldoDTO dto, LocalDate fechaPago) {
        Empleado empleado = empleadoRepository.findById(dto.getLegajo())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        // Verificar si ya existe una liquidación para este empleado en el periodo
        if (existeLiquidacionPorPeriodo(dto.getLegajo(), dto.getPeriodoPago())) {
            throw new RuntimeException("Ya existe una liquidación para el empleado " + dto.getLegajo()
                    + " en el periodo " + dto.getPeriodoPago());
        }

        String gremio = empleado.getGremio().getNombre().toUpperCase();
        BigDecimal basico;
        TipoConcepto tipoBasico;

        if (gremio.contains("UOCRA")) {
            if (empleado.getEmpleadoZona() == null || empleado.getEmpleadoZona().getZona() == null) {
                throw new RuntimeException("El empleado UOCRA no tiene zona asignada");
            }
            ZonasUocra zona = empleado.getEmpleadoZona().getZona();
            CategoriasZonasUocra categoriaZona = categoriaZonaUocraRepository
                    .findByCategoriaAndZona(empleado.getCategoria(), zona)
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
                    BigDecimal monto = basicoReferencia.multiply(bonif.getPorcentaje().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    totalBonificaciones = totalBonificaciones.add(monto);

                    crearConcepto(pago, TipoConcepto.BONIFICACION_AREA.name(),
                            bonif.getIdBonificacionVariable(), 1, monto, monto);
                }
            }
        }

        // Bonificaciones / descuentos manuales
        for (ConceptoInputDTO conceptoDTO : dto.getConceptos()) {

            String tipo = conceptoDTO.getTipoConcepto();
            if(tipo.equals("CATEGORIA") || tipo.equals("BONIFICACION_AREA")) continue;
            Integer idRef = conceptoDTO.getIdReferencia();
            Integer unidades = Optional.ofNullable(conceptoDTO.getUnidades()).orElse(1);

            BigDecimal montoUnitario = BigDecimal.ZERO;

            switch (tipo) {
                case "CONCEPTO_LYF" -> {
                    ConceptosLyF conLyF = conceptosLyFRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
                    montoUnitario = basicoReferencia.multiply(conLyF.getPorcentaje().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                }
                case "CONCEPTO_UOCRA" -> {
                    ConceptosUocra conUocra = conceptosUocraRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
                    montoUnitario = basico.multiply(conUocra.getPorcentaje().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                }
                case "DESCUENTO" -> {
                    Descuentos desc = descuentoRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
                    BigDecimal base = basico.add(totalBonificaciones);
                    montoUnitario = base.multiply(desc.getPorcentaje().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
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
                    conceptosLyFRepository.findById(idReferencia)
                            .ifPresent(concepto::setConceptosLyF);
                }
            }
            case "BONIFICACION_AREA" -> {
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
                        case CONCEPTO_LYF -> {
                            if (pc.getConceptosLyF() != null) {
                                c.setNombreConcepto(pc.getConceptosLyF().getNombre());
                                c.setId_Bonificacion_Fija(pc.getConceptosLyF().getIdConceptosLyf());
                            }
                        }
                        case BONIFICACION_AREA -> {
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

    public List<PagoSueldoResumenDTO> listarUltimos4Pagos() {
        List<PagoSueldo> pagos = pagoSueldoRepository.findTop4ByOrderByFechaPagoDesc();

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

    public DashboardLiquidacionesDTO obtenerDashboardMesActual() {

        // Obtener periodo actual YYYY-MM
        LocalDate hoy = LocalDate.now();
        String periodoActual = hoy.getYear() + "-" + String.format("%02d", hoy.getMonthValue());

        // Pagos hechos este periodo
        List<PagoSueldo> pagosMes = pagoSueldoRepository.findByPeriodoPago(periodoActual);

        BigDecimal totalBrutoMes = pagosMes.stream()
                .map(PagoSueldo::getTotal_bruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNetoMes = pagosMes.stream()
                .map(PagoSueldo::getTotal_neto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total empleados activos (o todos, según tu modelo)
        long totalEmpleados = empleadoRepository.count();

        int cantidadHechas = pagosMes.size();

        int pendientes = (int) (totalEmpleados - cantidadHechas);
        if (pendientes < 0) pendientes = 0;

        DashboardLiquidacionesDTO dto = new DashboardLiquidacionesDTO();
        dto.setTotalBrutoMes(totalBrutoMes);
        dto.setTotalNetoMes(totalNetoMes);
        dto.setCantidadEmpleados((int) totalEmpleados);
        dto.setCantidadLiquidacionesHechas(cantidadHechas);
        dto.setCantidadLiquidacionesPendientes(pendientes);

        return dto;
    }

    public DashboardLiquidacionesDTO obtenerDashboardPorPeriodo(String periodo) {

        List<PagoSueldo> pagosMes = pagoSueldoRepository.findByPeriodoPago(periodo);

        BigDecimal totalBrutoMes = pagosMes.stream()
                .map(PagoSueldo::getTotal_bruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNetoMes = pagosMes.stream()
                .map(PagoSueldo::getTotal_neto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalEmpleados = empleadoRepository.count();
        int cantidadHechas = pagosMes.size();

        int pendientes = (int) (totalEmpleados - cantidadHechas);
        if (pendientes < 0) pendientes = 0;

        DashboardLiquidacionesDTO dto = new DashboardLiquidacionesDTO();
        dto.setTotalBrutoMes(totalBrutoMes);
        dto.setTotalNetoMes(totalNetoMes);
        dto.setCantidadEmpleados((int) totalEmpleados);
        dto.setCantidadLiquidacionesHechas(cantidadHechas);
        dto.setCantidadLiquidacionesPendientes(pendientes);

        return dto;
    }

    public List<ResumenConceptosDTO> obtenerResumenConceptosMesActual() {
        LocalDate hoy = LocalDate.now();
        String periodoActual = hoy.getYear() + "-" + String.format("%02d", hoy.getMonthValue());
        return obtenerResumenConceptosPorPeriodo(periodoActual);
    }

    public List<ResumenConceptosDTO> obtenerResumenConceptosPorPeriodo(String periodo) {
        // Obtener todos los pagos del periodo
        List<PagoSueldo> pagos = pagoSueldoRepository.findByPeriodoPago(periodo);

        // Obtener todos los conceptos de esos pagos
        List<PagoConcepto> conceptos = pagos.stream()
                .flatMap(pago -> pagoConceptoRepository.findByPago(pago).stream())
                .collect(Collectors.toList());

        // Agrupar por nombre y tipo de concepto
        Map<String, ResumenConceptosDTO> resumenMap = new HashMap<>();

        for (PagoConcepto pc : conceptos) {
            String nombreConcepto = obtenerNombreConcepto(pc);
            String clave = nombreConcepto + "|" + pc.getTipoConcepto().name();

            resumenMap.computeIfAbsent(clave, k -> {
                ResumenConceptosDTO dto = new ResumenConceptosDTO();
                dto.setNombre(nombreConcepto);
                dto.setTipoConcepto(pc.getTipoConcepto());
                dto.setCantidad(0L);
                dto.setTotalPagado(BigDecimal.ZERO);
                return dto;
            });

            ResumenConceptosDTO dto = resumenMap.get(clave);
            dto.setCantidad(dto.getCantidad() + 1);
            dto.setTotalPagado(dto.getTotalPagado().add(pc.getTotal()));
        }

        return new ArrayList<>(resumenMap.values());
    }

    private String obtenerNombreConcepto(PagoConcepto pc) {
        switch (pc.getTipoConcepto()) {
            case CONCEPTO_LYF -> {
                if (pc.getConceptosLyF() != null) {
                    return pc.getConceptosLyF().getNombre();
                }
                return "Concepto Luz y Fuerza";
            }
            case CONCEPTO_UOCRA -> {
                // No hay referencia directa a ConceptosUocra en PagoConcepto
                return "Concepto UOCRA";
            }
            case BONIFICACION_AREA -> {
                if (pc.getBonificacionAreaLyF() != null) {
                    var bonif = pc.getBonificacionAreaLyF();
                    String nombreArea = bonif.getArea() != null ? bonif.getArea().getNombre() : "Área desconocida";
                    String nombreCategoria = bonif.getCategoria() != null ? bonif.getCategoria().getNombre() : "Categoría desconocida";
                    return "Bonificación Area - " + nombreArea + " / " + nombreCategoria;
                }
                return "Bonificación Area";
            }
            case DESCUENTO -> {
                if (pc.getDescuento() != null) {
                    return pc.getDescuento().getNombre();
                }
                return "Descuento";
            }
            case CATEGORIA -> {
                if (pc.getCategoria() != null) {
                    return "Sueldo Básico - " + pc.getCategoria().getNombre();
                }
                return "Sueldo Básico";
            }
            case CATEGORIA_ZONA -> {
                if (pc.getCategoriaZonaUocra() != null) {
                    return "Categoría-Zona: " + pc.getCategoriaZonaUocra().getCategoria().getNombre()
                            + " - " + pc.getCategoriaZonaUocra().getZona().getNombre();
                }
                return "Categoría-Zona";
            }
            default -> {
                return "Concepto desconocido";
            }
        }
    }

}