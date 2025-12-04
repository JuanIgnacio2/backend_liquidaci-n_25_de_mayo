package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ActividadCreateDTO;
import com.liquidacion.backend.DTO.ActividadDTO;
import com.liquidacion.backend.entities.Actividad;
import com.liquidacion.backend.entities.ReferenciaTipo;
import com.liquidacion.backend.repository.ActividadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;

    public ActividadDTO registrarActividad(ActividadCreateDTO dto) {
        Actividad actividad = new Actividad();
        actividad.setUsuario(dto.getUsuario());
        actividad.setAccion(dto.getAccion());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setReferenciaTipo(dto.getReferenciaTipo());
        actividad.setReferenciaId(dto.getReferenciaId());

        Actividad guardada = actividadRepository.save(actividad);
        return toDTO(guardada);
    }

    public ActividadDTO registrarActividad(
            String usuario,
            String accion,
            String descripcion,
            ReferenciaTipo referenciaTipo,
            Integer referenciaId
    ) {
        ActividadCreateDTO dto = new ActividadCreateDTO(
                usuario,
                accion,
                descripcion,
                referenciaTipo,
                referenciaId
        );
        return registrarActividad(dto);
    }

    public List<ActividadDTO> listarReciente(Integer limite) {
        if (limite == null || limite <= 0) {
            limite = 20;
        }

        List<Actividad> base = limite <= 20
                ? actividadRepository.findTop20ByOrderByFechaDesc()
                : actividadRepository.findTop100ByOrderByFechaDesc();

        return base.stream()
                .limit(limite)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarPorTipo(ReferenciaTipo tipo) {
        return actividadRepository.findByReferenciaTipoOrderByFechaDesc(tipo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarPorUsuario(String usuario) {
        return actividadRepository.findByUsuarioOrderByFechaDesc(usuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ActividadDTO toDTO(Actividad actividad) {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdActividad(actividad.getIdActividad());
        dto.setFecha(actividad.getFecha());
        dto.setUsuario(actividad.getUsuario());
        dto.setAccion(actividad.getAccion());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setReferenciaTipo(actividad.getReferenciaTipo());
        dto.setReferenciaId(actividad.getReferenciaId());
        return dto;
    }
}


