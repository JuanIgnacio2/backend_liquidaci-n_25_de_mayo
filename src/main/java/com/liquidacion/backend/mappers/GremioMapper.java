package com.liquidacion.backend.mappers;

import com.liquidacion.backend.DTO.GremioDTO;
import com.liquidacion.backend.entities.Gremio;

public class GremioMapper {
    public static GremioDTO toDTO(Gremio g) {
        if (g == null) return null;
        GremioDTO dto = new GremioDTO();
        dto.setIdGremio(g.getIdGremio());
        dto.setNombre(g.getNombre());
        return dto;
    }
}
