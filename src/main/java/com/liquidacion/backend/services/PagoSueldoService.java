package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.PagoSueldo;
import com.liquidacion.backend.repository.PagoSueldoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoSueldoService {
    private final PagoSueldoRepository pagoSueldoRepository;
    private final EmpleadoService empleadoService;

    public List<PagoSueldo> listar(){
        return pagoSueldoRepository.findAll();
    }

    public List<PagoSueldo> listarPorEmpleado(Integer legajo){
        return pagoSueldoRepository.findByEmpleado_Legajo(legajo);
    }

    public PagoSueldo buscarPorId(Long id){
        return pagoSueldoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Pago no encontrado"));
    }

    public PagoSueldo guardar(PagoSueldo pagoSueldo){
        return pagoSueldoRepository.save(pagoSueldo);
    }

    public void eliminar(Long id){
        pagoSueldoRepository.deleteById(id);
    }
}
