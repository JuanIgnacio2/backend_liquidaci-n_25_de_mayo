package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Integer id, Categoria categoriaActualizada) {
        Categoria categoria = buscarCategoriaPorId(id);
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setBasico(categoriaActualizada.getBasico());
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
