package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.EmpleadoCreateDTO;
import com.liquidacion.backend.DTO.EmpleadoListDTO;
import com.liquidacion.backend.DTO.EmpleadoUpdateDTO;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.mappers.EmpleadoMapper;
import com.liquidacion.backend.repository.AreaRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import com.liquidacion.backend.repository.EmpleadoConceptoRepository;
import com.liquidacion.backend.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final CategoriaRepository catRepo;
    private final AreaRepository areaRepo;
    private final EmpleadoConceptoRepository conceptoRepo;

    public List<EmpleadoListDTO> listarTodos(){
        return empleadoRepository.findAll()
                .stream()
                .map(EmpleadoMapper::toListDTO)
                .collect(Collectors.toList());
    }

    public EmpleadoListDTO obtenerPorLegajo(Integer legajo){
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return EmpleadoMapper.toListDTO(empleado);
    }

    public EmpleadoListDTO guardar(EmpleadoCreateDTO dto){
        if (dto.getLegajo() == null)
            throw new IllegalArgumentException("El legajo no puede ser null");

        if(empleadoRepository.existsById(dto.getLegajo())){
            throw new RuntimeException("El empleado ya existe");
        }

        Categoria categoria = catRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("El categoria no existe"));

        List<Area> areas = dto.getIdAreas() != null ? areaRepo.findAllById(dto.getIdAreas()) : null;

        Empleado empleado = EmpleadoMapper.toEntity(dto, categoria, areas);

        Empleado guardado = empleadoRepository.save(empleado);

        if(dto.getConceptosAsignados() != null){
            dto.getConceptosAsignados().forEach(cdto -> {
                EmpleadoConcepto concepto = new EmpleadoConcepto();
                concepto.setEmpleado(guardado);
                concepto.setTipoConcepto(TipoConcepto.valueOf(cdto.getTipoConcepto()));
                concepto.setIdReferencia(cdto.getIdReferencia());
                concepto.setUnidades(cdto.getUnidades() != null ? cdto.getUnidades() : 1);
                conceptoRepo.save(concepto);
            });
        }

        return EmpleadoMapper.toListDTO(guardado);
    }

    public Empleado actualizar(Integer legajo, EmpleadoUpdateDTO dto){
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if (dto.getNombre() != null)           empleado.setNombre(dto.getNombre());
        if (dto.getApellido() != null)         empleado.setApellido(dto.getApellido());
        if (dto.getCuil() != null)             empleado.setCuil(dto.getCuil());
        if (dto.getInicioActividad() != null)  empleado.setInicioActividad(dto.getInicioActividad());
        if (dto.getDomicilio() != null)        empleado.setDomicilio(dto.getDomicilio());
        if (dto.getBanco() != null)            empleado.setBanco(dto.getBanco());

        if (dto.getIdCategoria() != null) {
            Categoria categoria = catRepo.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            empleado.setCategoria(categoria);
        }

        if (dto.getIdAreas() != null && !dto.getIdAreas().isEmpty()) {
            List<Area> area = areaRepo.findAllById(dto.getIdAreas());
            empleado.setAreas(area);
        }

        if (dto.getEstado() != null)           empleado.setEstado(dto.getEstado());
        if (dto.getSexo() != null)             empleado.setSexo(dto.getSexo());
        if (dto.getGremio() != null)           empleado.setGremio(dto.getGremio());

        Empleado actualizado = empleadoRepository.save(empleado);

        //Reemplazar conceptos si se mandaron
        if(dto.getConceptosAsignados() != null) {
            conceptoRepo.deleteAll(actualizado.getConceptos());

            dto.getConceptosAsignados().forEach(cdto -> {
                EmpleadoConcepto concepto = new EmpleadoConcepto();
                concepto.setEmpleado(actualizado);
                concepto.setTipoConcepto(TipoConcepto.valueOf(cdto.getTipoConcepto()));
                concepto.setIdReferencia(cdto.getIdReferencia());
                concepto.setUnidades(cdto.getUnidades() != null ? cdto.getUnidades() : 1);
                conceptoRepo.save(concepto);
            });
        }
        return actualizado;
    }

    public EmpleadoListDTO cambiarEstado(Integer legajo){
        Empleado empleado = empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        if(empleado.getEstado() == EstadoEmpleado.ACTIVO){
            empleado.setEstado(EstadoEmpleado.DADO_DE_BAJA);
        } else{
            empleado.setEstado(EstadoEmpleado.ACTIVO);
            empleado.setInicioActividad(LocalDate.now());
        }

        empleadoRepository.save(empleado);
        return EmpleadoMapper.toListDTO(empleado);
    }

    public List<EmpleadoListDTO> listarActivos(){
        return empleadoRepository.findByEstado(EstadoEmpleado.ACTIVO)
                .stream()
                .map(EmpleadoMapper::toListDTO)
                .collect(Collectors.toList());
    }

    public List<EmpleadoListDTO> listarDadosDeBaja(){
        return empleadoRepository.findByEstado(EstadoEmpleado.DADO_DE_BAJA)
                .stream()
                .map(EmpleadoMapper::toListDTO)
                .collect(Collectors.toList());
    }
}