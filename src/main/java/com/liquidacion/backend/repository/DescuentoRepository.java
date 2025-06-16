package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Descuento;
import com.liquidacion.backend.entities.PagoSueldo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
    //List<PagoSueldo> findByEmpleado_Legajo(Integer legajo);
}
