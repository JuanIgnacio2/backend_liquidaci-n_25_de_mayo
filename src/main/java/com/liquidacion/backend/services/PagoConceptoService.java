package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.PagoConcepto;
import com.liquidacion.backend.repository.PagoConceptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoConceptoService {
    private final PagoConceptoRepository pagoConceptoRepository;

    public List<PagoConcepto> findAll() {
        return pagoConceptoRepository.findAll();
    }

    public PagoConcepto listarPorPago(Long id) {
        return pagoConceptoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Concepto de pago no encontrado"));
    }

    public PagoConcepto guardar(PagoConcepto concepto) {
        return pagoConceptoRepository.save(concepto);
    }
}
