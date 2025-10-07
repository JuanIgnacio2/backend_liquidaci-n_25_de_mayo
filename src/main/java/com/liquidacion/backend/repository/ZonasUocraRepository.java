package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.ZonasUocra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZonasUocraRepository extends JpaRepository<ZonasUocra, Integer> {
}
