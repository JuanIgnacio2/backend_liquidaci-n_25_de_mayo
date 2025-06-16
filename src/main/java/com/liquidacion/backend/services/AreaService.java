package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.AreaCreateDTO;
import com.liquidacion.backend.DTO.AreaListDTO;
import com.liquidacion.backend.entities.Area;
import com.liquidacion.backend.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public List<AreaListDTO> listarAreas() {
        return areaRepository.findAll()
                .stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AreaListDTO obtenerPorId(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area no encontrada"));
        return toListDTO(area);
    }

    public AreaListDTO crear(AreaCreateDTO dto) {
        Area area = new Area();
        area.setNombre(dto.getNombre());
        return toListDTO(areaRepository.save(area));
    }

    public AreaListDTO actualizar(Integer id, AreaCreateDTO dto) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area no encontrada"));
        area.setNombre(dto.getNombre());
        return toListDTO(areaRepository.save(area));
    }

    public void eliminar(Integer id) {
        areaRepository.deleteById(id);
    }

    private AreaListDTO toListDTO(Area area) {
        return new AreaListDTO(area.getId(), area.getNombre());
    }
}