package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.CategoriaCreateDTO;
import com.liquidacion.backend.DTO.CategoriaListDTO;
import com.liquidacion.backend.DTO.CategoriaUpdateDTO;
import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.mappers.CategoriaMapper;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaListDTO> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaListDTO> dtos = new ArrayList<>();
        for (Categoria c : categorias) {
            dtos.add(CategoriaMapper.toListDTO(c));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public CategoriaListDTO obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return CategoriaMapper.toListDTO(categoria);
    }

    public CategoriaListDTO crearCategoria(CategoriaCreateDTO dto) {
        if (categoriaRepository.existsByNombre(dto.getNombreCategoria())) {
            throw new RuntimeException("Ya existe una categoría con este nombre");
        }
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toListDTO(guardada);
    }

    public CategoriaListDTO actualizar(Integer id, CategoriaUpdateDTO dto) {
        Categoria categoria =  categoriaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        CategoriaMapper.updateEntity(categoria, dto);
        Categoria actualizada = categoriaRepository.save(categoria);
        return CategoriaMapper.toListDTO(actualizada);
    }

    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaListDTO toListDTO(Categoria categoria) {
        return new CategoriaListDTO(categoria.getIdCategoria(), categoria.getNombre(), categoria.getBasico());
    }
}
