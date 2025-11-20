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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void eliminarTodosPorEmpleado(Integer legajo) {
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con legajo: " + legajo));
        empleadoConceptoRepository.deleteAllByEmpleado(empleado);
    }

    @Transactional
    public void reemplazarConceptos(Integer legajo, List<EmpleadoConceptoDTO> conceptosDTO) {
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con legajo: " + legajo));
        
        // Eliminar todos los conceptos existentes
        empleadoConceptoRepository.deleteAllByEmpleado(empleado);
        
        // Agregar los nuevos conceptos
        if (conceptosDTO != null && !conceptosDTO.isEmpty()) {
            conceptosDTO.forEach(cdto -> {
                EmpleadoConcepto concepto = new EmpleadoConcepto();
                concepto.setEmpleado(empleado);
                concepto.setTipoConcepto(TipoConcepto.valueOf(cdto.getTipoConcepto()));
                concepto.setIdReferencia(cdto.getIdReferencia());
                concepto.setUnidades(cdto.getUnidades() != null ? cdto.getUnidades() : 1);
                empleadoConceptoRepository.save(concepto);
            });
        }
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