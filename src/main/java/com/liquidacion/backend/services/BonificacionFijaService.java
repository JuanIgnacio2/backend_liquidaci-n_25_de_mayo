package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.BonificacionFija;
import com.liquidacion.backend.repository.BonificacionFijaRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class BonificacionFijaService {
    private BonificacionFijaRepository bonificacionFijaRepository;

    public List<BonificacionFija> listar(){
        return bonificacionFijaRepository.findAll();
    }

    public BonificacionFija buscar(int id){
        return bonificacionFijaRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Bonificación fija no encontrada"));
    }

    public BonificacionFija guardar(BonificacionFija bonificacion){
        return bonificacionFijaRepository.save(bonificacion);
    }

    public BonificacionFija actualizar(Integer id, BonificacionFija actualizada){
        BonificacionFija bonificacion = buscar(id);
        bonificacion.setNombre(actualizada.getNombre());
        bonificacion.setPorcentaje(actualizada.getPorcentaje());
        return bonificacionFijaRepository.save(bonificacion);
    }

    public void eliminar(Integer id){
        bonificacionFijaRepository.deleteById(id);
    }
}
