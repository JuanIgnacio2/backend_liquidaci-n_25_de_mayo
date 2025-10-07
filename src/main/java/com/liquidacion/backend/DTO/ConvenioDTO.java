package com.liquidacion.backend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ConvenioDTO {
    private String nombreConvenio; // "LUZ Y FUERZA" o "UOCRA"
    private List<CategoriaListDTO> categorias;
    private List<BonificacionAreaDTO> bonificacionesAreas; // solo LYF
    private List<BonificacionFijaDTO> bonificacionesFijas; // solo LYF
    private List<ZonaDTO> zonas; // solo UOCRA
}
