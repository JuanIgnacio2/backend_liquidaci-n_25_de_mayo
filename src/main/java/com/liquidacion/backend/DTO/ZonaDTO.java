package com.liquidacion.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ZonaDTO {
    private Integer idZona;
    private String nombre;
    private List<CategoriaZonaUocraDTO> categorias; // categorías que aplican en esa zona
}
