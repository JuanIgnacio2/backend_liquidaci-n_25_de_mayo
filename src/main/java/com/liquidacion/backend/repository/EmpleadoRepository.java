package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByCuil(String cuil);
}
