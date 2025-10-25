package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.BonificacionAreaDTO;
import com.liquidacion.backend.entities.BonificacionAreaLyF;
import com.liquidacion.backend.repository.AreaRepository;
import com.liquidacion.backend.repository.BonificacionAreaLyFRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BonificacionAreaLyFService {
    private final BonificacionAreaLyFRepository bonificacionAreaRepository;
    private final AreaRepository areaRepo;
    private final CategoriaRepository categoriaRepo;

    @Transactional(readOnly = true)
    public List<BonificacionAreaDTO> listarPorArea(Integer idArea) {
        return bonificacionAreaRepository.findByArea_IdArea(idArea)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BonificacionAreaLyF buscar(Integer id){
        return bonificacionAreaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificacion no encontrada"));
    }

    private BonificacionAreaDTO toDTO(BonificacionAreaLyF bonificacionArea){
        return new BonificacionAreaDTO(
                bonificacionArea.getIdBonificacionVariable(),
                bonificacionArea.getArea().getIdArea(),
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

    public BonificacionAreaLyF actualizar(Integer id, BonificacionAreaLyF bonificacionActualizada){
        BonificacionAreaLyF bonificacion = buscar(id);
        bonificacion.setArea(bonificacionActualizada.getArea());
        bonificacion.setCategoria(bonificacionActualizada.getCategoria());
        bonificacion.setPorcentaje(bonificacionActualizada.getPorcentaje());
        return bonificacionAreaRepository.save(bonificacion);
    }

    public BigDecimal obtenerPorcentaje(Integer categoriaId, Integer areaId){
        List<BonificacionAreaLyF> bonificaciones = bonificacionAreaRepository.findByArea_IdAreaAndCategoria_IdCategoria(categoriaId, areaId);

        if(bonificaciones.isEmpty()){
            return BigDecimal.ZERO;
        }
        return bonificaciones.stream()
                .map(BonificacionAreaLyF::getPorcentaje)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
