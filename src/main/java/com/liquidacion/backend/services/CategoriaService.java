package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.CategoriaCreateDTO;
import com.liquidacion.backend.DTO.CategoriaListDTO;
import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaListDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaListDTO buscarCategoriaPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return toListDTO(categoria);
    }

    public CategoriaListDTO crearCategoria(CategoriaCreateDTO dto) {
        Categoria cat = new Categoria();
        cat.setNombre(dto.getNombre());
        cat.setBasico(dto.getBasico());
        return toListDTO(categoriaRepository.save(cat));
    }

    public CategoriaListDTO actualizar(Integer id, CategoriaCreateDTO dto) {
        Categoria categoria =  categoriaRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        categoria.setNombre(dto.getNombre());
        categoria.setBasico(dto.getBasico());
        return toListDTO(categoriaRepository.save(categoria));
    }

    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaListDTO toListDTO(Categoria categoria) {
        return new CategoriaListDTO(categoria.getIdCategoria(), categoria.getNombre(), categoria.getBasico());
    }
}
