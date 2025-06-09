package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagoSueldoDTO {
    private Integer idPago;
    private Integer legajoEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private LocalDate fechaPago;
    private String periodoPago;
    private BigDecimal total;
}
