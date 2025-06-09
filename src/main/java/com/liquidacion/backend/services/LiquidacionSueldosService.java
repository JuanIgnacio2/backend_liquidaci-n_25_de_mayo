package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ConceptoInputDTO;
import com.liquidacion.backend.DTO.LiquidacionSueldoDTO;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiquidacionSueldosService {
    private final EmpleadoRepository empleadoRepository;
    private final BonificacionFijaRepository bonificacionFijaRepository;
    private final BonificacionVariableRepository bonificacionVariableRepository;
    private final DescuentoRepository descuentoRepository;
    private final PagoSueldoRepository pagoSueldoRepository;
    private final PagoConceptoRepository pagoConceptoRepository;

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
        pago = pagoSueldoRepository.save(pago);

        //Concepto 1: Sueldo básico
        crearConcepto(pago, "BASICO", null, 1, basico, basico);

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
                    BonificacionVariable bonVar = bonificacionVariableRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Bonificacion no encontrada"));
                    montoUnitario = basico.multiply(bonVar.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    totalBonificaciones = totalBonificaciones.add(montoUnitario.multiply(BigDecimal.valueOf(unidades)));
                    break;

                case "DESCUENTO":
                    Descuento desc = descuentoRepository.findById(idRef)
                            .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
                    BigDecimal montoDescuento = desc.getPorcentaje().divide(BigDecimal.valueOf(100));
                    totalDescuentos = totalDescuentos.add(montoDescuento.multiply(BigDecimal.valueOf(unidades)));
                    break;

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
}
