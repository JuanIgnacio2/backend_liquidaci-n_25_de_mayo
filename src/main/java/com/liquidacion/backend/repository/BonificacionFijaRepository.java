package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.BonificacionFija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonificacionFijaRepository extends JpaRepository<BonificacionFija, Integer> {
}
