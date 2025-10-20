package com.liquidacion.backend.mappers;

import com.liquidacion.backend.DTO.*;
import com.liquidacion.backend.entities.*;

import java.util.List;

public class ConvenioMapper {
    public static CategoriaListDTO toCategoriaDTO(Categoria categoria) {
        CategoriaListDTO dto = new CategoriaListDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombreCategoria(categoria.getNombre());
        dto.setBasico(categoria.getBasico());
        return dto;
    }

    public static BonificacionAreaDTO toBonificacionAreaDTO(BonificacionAreaLyF bonif) {
        BonificacionAreaDTO dto = new BonificacionAreaDTO();
        dto.setId(bonif.getIdBonificacionVariable());
        dto.setIdArea(bonif.getArea().getIdArea());
        dto.setNombreArea(bonif.getArea().getNombre());

        if (bonif.getCategoria() != null) {
            dto.setIdCategoria(bonif.getCategoria().getIdCategoria());
            dto.setNombreCategoria(bonif.getCategoria().getNombre());
        }

        dto.setPorcentaje(bonif.getPorcentaje());
        return dto;
    }

    public static BonificacionFijaDTO toBonificacionFijaDTO(BonificacionFija bonif) {
        BonificacionFijaDTO dto = new BonificacionFijaDTO();
        dto.setId(bonif.getIdBonificacionFija());
        dto.setNombre(bonif.getNombre());
        dto.setPorcentaje(bonif.getPorcentaje());
        return dto;
    }

    public static ZonaDTO toZonaDTO(ZonasUocra zona, List<CategoriasZonasUocra> categoriasZonas) {
        ZonaDTO dto = new ZonaDTO();
        dto.setIdZona(zona.getIdZona());
        dto.setNombre(zona.getNombre());
        dto.setCategorias(
                categoriasZonas.stream()
                        .map(ConvenioMapper::toCategoriaZonaDTO)
                        .toList()
        );
        return dto;
    }

    public static CategoriaZonaUocraDTO toCategoriaZonaDTO(CategoriasZonasUocra cz) {
        CategoriaZonaUocraDTO dto = new CategoriaZonaUocraDTO();
        dto.setIdCategoria(cz.getCategoria().getIdCategoria());
        dto.setNombreCategoria(cz.getCategoria().getNombre());
        dto.setBasico(cz.getBasico());
        return dto;
    }
}
