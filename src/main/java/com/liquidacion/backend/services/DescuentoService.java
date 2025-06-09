package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.Descuento;
import com.liquidacion.backend.repository.DescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescuentoService {
    private final DescuentoRepository descuentoRepository;

    public List<Descuento> listar(){
        return descuentoRepository.findAll();
    }

    public Descuento obtenerPorId(Integer id){
        return descuentoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Descuento no encontrado"));
    }

    public Descuento guardar(Descuento descuento){
        return descuentoRepository.save(descuento);
    }

    public Descuento actualizar(Integer id, Descuento actualizado){
        Descuento descuento = obtenerPorId(id);
        descuento.setNombre(actualizado.getNombre());
        descuento.setPorcentaje(actualizado.getPorcentaje());
        return descuentoRepository.save(descuento);
    }

    public void eliminar(Integer id){
        descuentoRepository.deleteById(id);
    }
}