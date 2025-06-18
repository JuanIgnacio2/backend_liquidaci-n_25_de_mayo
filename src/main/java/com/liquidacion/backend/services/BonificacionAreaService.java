package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.BonificacionArea;
import com.liquidacion.backend.repository.BonificacionAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonificacionAreaService {
    private final BonificacionAreaRepository bonificacionAreaRepository;

    public List<BonificacionArea> listar(){
        return bonificacionAreaRepository.findAll();
    }

    public BonificacionArea buscar(Integer id){
        return bonificacionAreaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificacion no encontrada"));
    }

    public BonificacionArea guardar(BonificacionArea bonificacionArea){
        return bonificacionAreaRepository.save(bonificacionArea);
    }

    public BonificacionArea actualizar(Integer id, BonificacionArea bonificacionActualizada){
        BonificacionArea bonificacion = buscar(id);
        bonificacion.setArea(bonificacionActualizada.getArea());
        bonificacion.setCategoria(bonificacionActualizada.getCategoria());
        bonificacion.setPorcentaje(bonificacionActualizada.getPorcentaje());
        return bonificacionAreaRepository.save(bonificacion);
    }

    public void eliminar(Integer id){
        bonificacionAreaRepository.deleteById(id);
    }

    public List<BonificacionArea> obtenerPorCategoriaYArea(Integer categoriaId, Integer areaId){
        return bonificacionAreaRepository.findByCategoria_IdCategoriaAndArea_Id(categoriaId, areaId);
    }
}
