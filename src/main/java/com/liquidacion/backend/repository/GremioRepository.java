package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Gremio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GremioRepository extends JpaRepository<Gremio, Integer> {
    Gremio findByNombre(String nombre);
}
