package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.entities.EmpleadoConcepto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoConceptoRepository extends JpaRepository<EmpleadoConcepto, Long> {
    List<EmpleadoConcepto> findByLegajo(Integer legajo);
}
