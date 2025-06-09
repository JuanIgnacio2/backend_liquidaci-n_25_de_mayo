package com.liquidacion.backend.DTO;

import lombok.Data;

import java.util.List;
@Data
public class LiquidacionSueldoDTO {
    private Integer legajo;
    private String periodoPago;
    private List<ConceptoInputDTO> conceptos;
}
