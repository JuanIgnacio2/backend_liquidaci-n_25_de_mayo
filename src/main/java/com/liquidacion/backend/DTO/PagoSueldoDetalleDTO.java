package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PagoSueldoDetalleDTO {
    private Integer idPago;
    private String periodoPago;
    private LocalDate fechaPago;
    private BigDecimal total;

    private Integer legajoEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String categoriaEmpleado;

    private List<PagoConceptoDTO> conceptos;
}
