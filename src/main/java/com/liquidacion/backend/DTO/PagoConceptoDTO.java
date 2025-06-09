package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoConceptoDTO {
    private String tipoConcepto;
    private String nombre;
    private Integer unidades;
    private BigDecimal montoUnitario;
    private BigDecimal total;
}
