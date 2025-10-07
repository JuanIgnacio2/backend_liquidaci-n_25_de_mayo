package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CategoriaZonaUocraDTO {
    private Integer id;
    private Integer idCategoria;
    private String nombreCategoria;
    private BigDecimal basico;
}
