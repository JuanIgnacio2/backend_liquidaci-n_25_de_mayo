package com.liquidacion.backend.DTO;

import lombok.Data;

@Data
public class EmpleadoConceptoDTO {
    private Long id;
    private Integer legajo;
    private String tipoConcepto;
    private Integer idReferencia;
    private Integer unidades;
}
