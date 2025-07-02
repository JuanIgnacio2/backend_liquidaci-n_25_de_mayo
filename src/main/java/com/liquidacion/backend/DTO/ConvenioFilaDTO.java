package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ConvenioFilaDTO {
    private String nombreCategoria;
    private BigDecimal basico;
    private Map<String, BigDecimal> montosPorArea;
}
