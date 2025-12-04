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
}
