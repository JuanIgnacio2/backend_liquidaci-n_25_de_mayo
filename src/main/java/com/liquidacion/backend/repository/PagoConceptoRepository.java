package com.liquidacion.backend.repository;

import com.liquidacion.backend.DTO.ResumenConceptosDTO;
import com.liquidacion.backend.entities.PagoConcepto;
import com.liquidacion.backend.entities.PagoSueldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoConceptoRepository extends JpaRepository<PagoConcepto, Long> {
    List<PagoConcepto> findByPago(PagoSueldo pago);
}
