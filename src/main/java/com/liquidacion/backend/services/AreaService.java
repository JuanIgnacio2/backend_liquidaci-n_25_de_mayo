package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.Area;
import com.liquidacion.backend.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaRepository areaRepository;

    public List<Area> listar() {
        return areaRepository.findAll();
    }

    public Area obtenerPorId(Integer id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));
    }

    public Area guardar(Area area) {
        return areaRepository.save(area);
    }

    public Area actualizar(Integer id, Area areaActualizada) {
        Area area = obtenerPorId(id);
        area.setNombre(areaActualizada.getNombre());
        return areaRepository.save(area);
    }

    public void eliminar(Integer id) {
        areaRepository.deleteById(id);
    }
}
