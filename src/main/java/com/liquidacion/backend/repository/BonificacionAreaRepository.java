package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.BonificacionArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonificacionAreaRepository extends JpaRepository<BonificacionArea, Integer> {
    List<BonificacionArea> findByArea_Id(Integer idArea);
    Optional<BonificacionArea> findByArea_IdAndCategoria_IdCategoria(Integer categoriaId, Integer areaId);
}
