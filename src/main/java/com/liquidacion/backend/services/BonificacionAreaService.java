package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.BonificacionAreaDTO;
import com.liquidacion.backend.entities.BonificacionArea;
import com.liquidacion.backend.repository.AreaRepository;
import com.liquidacion.backend.repository.BonificacionAreaRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BonificacionAreaService {
    private final BonificacionAreaRepository bonificacionAreaRepository;
    private final AreaRepository areaRepo;
    private final CategoriaRepository categoriaRepo;

    @Transactional(readOnly = true)
    public List<BonificacionAreaDTO> listarPorArea(Integer idArea) {
        return bonificacionAreaRepository.findByArea_Id(idArea)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BonificacionArea buscar(Integer id){
        return bonificacionAreaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificacion no encontrada"));
    }

    private BonificacionAreaDTO toDTO(BonificacionArea bonificacionArea){
        return new BonificacionAreaDTO(
                bonificacionArea.getIdBonificacionVariable(),
                bonificacionArea.getArea().getId(),
                bonificacionArea.getArea().getNombre(),
                bonificacionArea.getCategoria().getIdCategoria(),
                bonificacionArea.getCategoria().getNombre(),
                bonificacionArea.getPorcentaje()
        );
    }

    /*public BonificacionAreaDTO crear(BonificacionAreaDTO dto){
        BonificacionArea bonArea = new BonificacionArea();
        bonArea.setArea(areaRepo.findById(dto.getIdArea()))
                .orElseThrow(()->new RuntimeException("Área no encontrada"));
        bonArea.setCategoria(categoriaRepo.findById(dto.getIdCategoria()))
                .orElseThrow(()->new RuntimeException("Categoria no encontrada"));
        bonArea.setPorcentaje(dto.getPorcentaje());
        return toDTO(bonificacionAreaRepository.save(bonArea));
    }*/

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

    public BigDecimal obtenerPorcentaje(Integer categoriaId, Integer areaId){
        return bonificacionAreaRepository.findByArea_IdAndCategoria_IdCategoria(categoriaId, areaId)
                .map(BonificacionArea::getPorcentaje)
                .orElseThrow(()-> new RuntimeException("No se puede obtener porcentaje."));
    }
}
