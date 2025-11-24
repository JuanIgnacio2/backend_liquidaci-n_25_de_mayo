package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConceptosLyFDTO {
    private Integer id;
    private String nombre;
    private BigDecimal porcentaje;
}
