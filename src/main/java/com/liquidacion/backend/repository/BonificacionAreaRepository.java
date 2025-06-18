package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.BonificacionArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonificacionAreaRepository extends JpaRepository<BonificacionArea, Integer> {
    List<BonificacionArea> findByCategoria_IdCategoriaAndArea_Id(Integer categoriaId, Integer areaId);
}
