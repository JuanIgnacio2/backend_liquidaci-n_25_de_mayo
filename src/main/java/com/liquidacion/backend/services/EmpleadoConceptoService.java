package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.EmpleadoConceptoDTO;
import com.liquidacion.backend.entities.EmpleadoConcepto;
import com.liquidacion.backend.entities.TipoConcepto;
import com.liquidacion.backend.repository.EmpleadoConceptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoConceptoService {

    private final EmpleadoConceptoRepository empleadoConceptoRepository;

    public EmpleadoConceptoDTO asignarConcepto(EmpleadoConceptoDTO dto) {
        EmpleadoConcepto ec = new EmpleadoConcepto();
        ec.setLegajo(dto.getLegajo());
        ec.setTipoConcepto(TipoConcepto.valueOf(dto.getTipoConcepto()));
        ec.setIdReferencia(dto.getIdReferencia());
        ec.setUnidades(dto.getUnidades());
        empleadoConceptoRepository.save(ec);
        return dto;
    }

    public List<EmpleadoConceptoDTO> buscarTodos() {
        return empleadoConceptoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmpleadoConceptoDTO> buscarPorLegajo(Integer legajo) {
        return empleadoConceptoRepository.findByLegajo(legajo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpleadoConceptoDTO> modificar(EmpleadoConceptoDTO dto) {
        return empleadoConceptoRepository.findById(dto.getId()).map(e->{
            e.setTipoConcepto(TipoConcepto.valueOf(dto.getTipoConcepto()));
            e.setIdReferencia(dto.getIdReferencia());
            e.setUnidades(dto.getUnidades());
            return toDTO(empleadoConceptoRepository.save(e));
        });
    }

    private EmpleadoConceptoDTO toDTO(EmpleadoConcepto e) {
        EmpleadoConceptoDTO dto = new EmpleadoConceptoDTO();
        dto.setId(e.getId());
        dto.setLegajo(e.getLegajo());
        dto.setTipoConcepto(String.valueOf(e.getTipoConcepto()));
        dto.setIdReferencia(e.getIdReferencia());
        dto.setUnidades(e.getUnidades());
        return dto;
    }
}
