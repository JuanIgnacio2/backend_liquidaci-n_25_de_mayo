package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.EmpleadoConceptoDTO;
import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.entities.EmpleadoConcepto;
import com.liquidacion.backend.entities.TipoConcepto;
import com.liquidacion.backend.repository.EmpleadoConceptoRepository;
import com.liquidacion.backend.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoConceptoService {

    private final EmpleadoConceptoRepository empleadoConceptoRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public EmpleadoConceptoDTO asignarConcepto(EmpleadoConceptoDTO dto) {
        EmpleadoConcepto ec = new EmpleadoConcepto();
        Empleado empleado = empleadoRepository.findById(dto.getLegajo())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con legajo: " + dto.getLegajo()));

        ec.setEmpleado(empleado);
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

    public List<EmpleadoConceptoDTO> findByEmpleado_Legajo(Integer legajo) {
        return empleadoConceptoRepository.findByEmpleado_Legajo(legajo)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpleadoConceptoDTO> modificar(EmpleadoConceptoDTO dto) {
        return empleadoConceptoRepository.findById(dto.getIdEmpleadoConcepto()).map(e->{
            e.setTipoConcepto(TipoConcepto.valueOf(dto.getTipoConcepto()));
            e.setIdReferencia(dto.getIdReferencia());
            e.setUnidades(dto.getUnidades());
            return toDTO(empleadoConceptoRepository.save(e));
        });
    }

    private EmpleadoConceptoDTO toDTO(EmpleadoConcepto e) {
        EmpleadoConceptoDTO dto = new EmpleadoConceptoDTO();
        dto.setIdEmpleadoConcepto(e.getId_empleado_concepto());
        dto.setLegajo(e.getEmpleado().getLegajo());
        dto.setTipoConcepto(String.valueOf(e.getTipoConcepto()));
        dto.setIdReferencia(e.getIdReferencia());
        dto.setUnidades(e.getUnidades());
        return dto;
    }
}