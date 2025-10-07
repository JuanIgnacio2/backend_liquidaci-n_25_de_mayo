package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvenioResumenDTO {
    private String nombreConvenio;
    private Long empleadosActivos;
    private Long cantidadCategorias;
    private String descripcion;
    private String controller;
}
