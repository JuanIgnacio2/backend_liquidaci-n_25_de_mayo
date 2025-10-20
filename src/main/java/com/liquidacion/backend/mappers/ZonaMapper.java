package com.liquidacion.backend.mappers;

import com.liquidacion.backend.DTO.CategoriaZonaUocraDTO;
import com.liquidacion.backend.DTO.ZonaDTO;
import com.liquidacion.backend.entities.CategoriasZonasUocra;
import com.liquidacion.backend.entities.ZonasUocra;

import java.util.List;
import java.util.stream.Collectors;

public class ZonaMapper {

    public static ZonaDTO toZonaDTO(ZonasUocra zona, List<CategoriasZonasUocra> categoriasZona) {
        ZonaDTO dto = new ZonaDTO();
        dto.setIdZona(zona.getIdZona());
        dto.setNombre(zona.getNombre());

        if (categoriasZona != null && !categoriasZona.isEmpty()) {
            dto.setCategorias(
                    categoriasZona.stream()
                            .map(catZona -> {
                                CategoriaZonaUocraDTO cDto = new CategoriaZonaUocraDTO();
                                cDto.setIdCategoria(catZona.getCategoria().getIdCategoria());
                                cDto.setNombreCategoria(catZona.getCategoria().getNombre());
                                cDto.setBasico(catZona.getBasico());
                                return cDto;
                            })
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }
}
