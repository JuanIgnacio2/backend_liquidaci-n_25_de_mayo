package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoConceptoDTO {
    private Long idEmpleadoConcepto;
    private Integer legajo;
    private String tipoConcepto; // ej: BONIFICACION_FIJA, BONIFICACION_AREA, CATEGORIA_ZONA
    private Integer idReferencia; // referencia al id en su tabla
    private Integer unidades; // ej: horas, días, etc.
}
