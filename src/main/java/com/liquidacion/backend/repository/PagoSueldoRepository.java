package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.entities.PagoSueldo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoSueldoRepository extends JpaRepository<PagoSueldo, Long> {
    List<PagoSueldo> findByEmpleado_Legajo(Integer legajo);
    List<PagoSueldo> findByEmpleado(Empleado empleado);
    boolean existsByEmpleado_LegajoAndPeriodoPago(Integer legajo, String periodoPago);
    Optional<PagoSueldo> findByEmpleado_LegajoAndPeriodoPago(Integer legajo, String periodoPago);
    List<PagoSueldo> findByPeriodoPago(String periodo);
    List<PagoSueldo> findTop4ByOrderByFechaPagoDesc();
}