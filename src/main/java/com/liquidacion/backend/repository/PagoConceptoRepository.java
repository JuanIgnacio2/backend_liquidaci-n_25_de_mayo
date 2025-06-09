package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.PagoConcepto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PagoConceptoRepository extends JpaRepository<PagoConcepto, Long> {
    List<PagoConcepto> findByPago_Id(Long pagoId);
}
