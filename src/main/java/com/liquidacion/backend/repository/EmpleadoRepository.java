package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.entities.EstadoEmpleado;
import com.liquidacion.backend.entities.Gremio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByCuil(String cuil);
    List<Empleado> findByEstado(EstadoEmpleado estado);
    Long countByGremioAndEstado(Gremio gremio, EstadoEmpleado estado);
    Long countByEstado(EstadoEmpleado estado);
}
