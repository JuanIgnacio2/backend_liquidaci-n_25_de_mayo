package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaZonaUocraDTO {
    private Integer idCategoria;
    private String nombreCategoria;
    private BigDecimal basico;
}
