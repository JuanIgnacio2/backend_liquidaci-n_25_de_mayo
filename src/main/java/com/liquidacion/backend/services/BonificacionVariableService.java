package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.BonificacionVariable;
import com.liquidacion.backend.repository.BonificacionVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BonificacionVariableService {
    private final BonificacionVariableRepository bonificacionVariableRepository;

    public List<BonificacionVariable> listar(){
        return bonificacionVariableRepository.findAll();
    }

    public BonificacionVariable buscar(Integer id){
        return bonificacionVariableRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificacion no encontrada"));
    }

    public BonificacionVariable guardar(BonificacionVariable bonificacionVariable){
        return bonificacionVariableRepository.save(bonificacionVariable);
    }

    public BonificacionVariable actualizar(Integer id, BonificacionVariable bonificacionActualizada){
        BonificacionVariable bonificacion = buscar(id);
        bonificacion.setArea(bonificacionActualizada.getArea());
        bonificacion.setCategoria(bonificacionActualizada.getCategoria());
        bonificacion.setPorcentaje(bonificacionActualizada.getPorcentaje());
        return bonificacionVariableRepository.save(bonificacion);
    }

    public void eliminar(Integer id){
        bonificacionVariableRepository.deleteById(id);
    }

    public List<BonificacionVariable> obtenerPorCategoriaYArea(Integer categoriaId, Integer areaId){
        return bonificacionVariableRepository.findByCategoria_IdAndArea_Id(categoriaId, areaId);
    }
}
