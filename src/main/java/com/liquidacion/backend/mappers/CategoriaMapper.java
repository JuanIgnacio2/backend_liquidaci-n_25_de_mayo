package com.liquidacion.backend.mappers;

import com.liquidacion.backend.DTO.CategoriaCreateDTO;
import com.liquidacion.backend.DTO.CategoriaListDTO;
import com.liquidacion.backend.DTO.CategoriaUpdateDTO;
import com.liquidacion.backend.entities.Categoria;

public class CategoriaMapper {
    public static CategoriaListDTO toListDTO(Categoria categoria) {
        CategoriaListDTO dto = new CategoriaListDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombreCategoria(categoria.getNombre());
        dto.setBasico(categoria.getBasico());
        return dto;
    }

    public static Categoria toEntity(CategoriaCreateDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombreCategoria());
        categoria.setBasico(dto.getBasico());
        return categoria;
    }

    public static void updateEntity(Categoria categoria, CategoriaUpdateDTO dto) {
        if(dto.getNombreCategoria() != null){
            categoria.setNombre(dto.getNombreCategoria());
        }
        if(dto.getBasico() != null){
            categoria.setBasico(dto.getBasico());
        }
    }
}
