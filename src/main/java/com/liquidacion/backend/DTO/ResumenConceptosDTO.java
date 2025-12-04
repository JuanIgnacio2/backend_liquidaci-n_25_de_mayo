package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.TipoConcepto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenConceptosDTO {
    private String nombre;
    private TipoConcepto tipoConcepto;
    private Long cantidad;
    private BigDecimal totalPagado;
}
