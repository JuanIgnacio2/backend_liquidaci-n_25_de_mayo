package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaListDTO {
    private Integer idCategoria;
    private String nombreCategoria;
    private BigDecimal basico;
}
