package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoConceptoDTO {
    private String tipoConcepto; // ej: BONIFICACION_FIJA, BONIFICACION_AREA, CATEGORIA_ZONA
    private String nombreConcepto; // nombre de la bonificación o categoría
    private Integer unidades;
    private BigDecimal montoUnitario;
    private BigDecimal total;
    private Integer id_Categoria;
    private Integer id_Bonificacion_Fija;
    private Integer id_Bonificacion_Area;
    private Integer id_Categoria_Zona_Uocra;
}
