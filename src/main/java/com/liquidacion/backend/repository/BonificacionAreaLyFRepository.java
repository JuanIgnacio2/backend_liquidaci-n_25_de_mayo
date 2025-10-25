package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Area;
import com.liquidacion.backend.entities.BonificacionAreaLyF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonificacionAreaLyFRepository extends JpaRepository<BonificacionAreaLyF, Integer> {
    List<BonificacionAreaLyF> findByArea_IdArea(Integer idArea);
    List<BonificacionAreaLyF> findByArea_IdAreaAndCategoria_IdCategoria(Integer areaId, Integer categoriaId);
}
