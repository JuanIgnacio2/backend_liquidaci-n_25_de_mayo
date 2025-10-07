package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombre(String nombre);
    List<Categoria> findByIdCategoriaBetween(Integer start, Integer end);
    List<Categoria> findByIdCategoriaGreaterThan(Integer min);
    Long countByIdCategoriaBetween (Integer min, Integer max);
    Long countByIdCategoriaGreaterThan (Integer min);
}
