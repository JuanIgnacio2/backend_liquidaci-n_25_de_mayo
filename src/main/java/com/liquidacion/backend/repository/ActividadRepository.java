package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Actividad;
import com.liquidacion.backend.entities.ReferenciaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findTop20ByOrderByFechaDesc();

    List<Actividad> findTop100ByOrderByFechaDesc();

    List<Actividad> findByReferenciaTipoOrderByFechaDesc(ReferenciaTipo referenciaTipo);

    List<Actividad> findByUsuarioOrderByFechaDesc(String usuario);
}


