package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Descuentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoRepository extends JpaRepository<Descuentos, Integer> {
}
