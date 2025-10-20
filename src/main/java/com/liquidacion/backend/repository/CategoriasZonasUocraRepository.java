package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.CategoriasZonasUocra;
import com.liquidacion.backend.entities.ZonasUocra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriasZonasUocraRepository extends JpaRepository<CategoriasZonasUocra, Integer> {
    // Buscar por categoría y zona
    Optional<CategoriasZonasUocra> findByCategoria_IdCategoriaAndZona_IdZona(Integer idCategoria, Integer idZona);

    List<CategoriasZonasUocra> findByZona(ZonasUocra idZona);

    List<CategoriasZonasUocra> findByZonaIdZona(Integer idZona);
}
