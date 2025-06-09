package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.BonificacionVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonificacionVariableRepository extends JpaRepository<BonificacionVariable, Integer> {
    List<BonificacionVariable> findByCategoria_IdAndArea_Id(Integer categoriaId, Integer areaId);
}
