package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.DescuentoDTO;
import com.liquidacion.backend.entities.Descuentos;
import com.liquidacion.backend.repository.DescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescuentoService {
    private final DescuentoRepository descuentoRepository;

    public List<Descuentos> listar(){
        return descuentoRepository.findAll();
    }

    public Descuentos obtenerPorId(Integer id){
        return descuentoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Descuento no encontrado"));
    }

    public Descuentos guardar(Descuentos descuento){
        return descuentoRepository.save(descuento);
    }

    public Descuentos actualizar(Integer id, Descuentos actualizado){
        Descuentos descuento = obtenerPorId(id);
        descuento.setNombre(actualizado.getNombre());
        descuento.setPorcentaje(actualizado.getPorcentaje());
        return descuentoRepository.save(descuento);
    }

    public void eliminar(Integer id){
        descuentoRepository.deleteById(id);
    }
}