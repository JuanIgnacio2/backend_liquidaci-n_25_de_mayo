package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.EmpleadoConceptoDTO;
import com.liquidacion.backend.DTO.EmpleadoCreateDTO;
import com.liquidacion.backend.DTO.EmpleadoListDTO;
import com.liquidacion.backend.DTO.EmpleadoUpdateDTO;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.repository.AreaRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import com.liquidacion.backend.repository.EmpleadoConceptoRepository;
import com.liquidacion.backend.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .map(this::mapToListDTO)
                .collect(Collectors.toList());
    }

    public EmpleadoListDTO obtenerPorLegajo(Integer legajo){
        Empleado empleado = empleadoRepository.findById(legajo).get();
        return mapToListDTO(empleado);
    }

    private EmpleadoListDTO mapToListDTO(Empleado e){
        EmpleadoListDTO dto = new EmpleadoListDTO();
        //Info básica
        dto.setLegajo(e.getLegajo());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setCuil(e.getCuil());

        //Info complementaria
        dto.setInicioActividad(e.getInicioActividad());
        dto.setDomicilio(e.getDomicilio());
        dto.setBanco(e.getBanco());
        dto.setSexo(e.getSexo());
        dto.setEstado(e.getEstado());

        // Evitamos NPE si aún no hay área / categoría
        if(e.getCategoria() != null) {
            dto.setIdCategoria(e.getCategoria().getIdCategoria());
            dto.setCategoria(e.getCategoria() != null ? e.getCategoria().getNombre() : null);
        }
        if(e.getAreas() != null) {
            dto.setIdAreas(e.getAreas().stream()
                    .map(Area::getId)
                    .collect(Collectors.toList()));

            dto.setNombreAreas(e.getAreas().stream()
                    .map(Area::getNombre)
                    .collect(Collectors.toList()));
        }

        dto.setGremio(e.getGremio());

        if(e.getConceptos() != null){
            List<EmpleadoConceptoDTO> conceptosDTO = e.getConceptos().stream().map(c -> {
                EmpleadoConceptoDTO cDTO = new EmpleadoConceptoDTO();
                cDTO.setId(c.getId());
                cDTO.setLegajo(e.getLegajo());
                cDTO.setTipoConcepto(String.valueOf(c.getTipoConcepto()));
                cDTO.setIdReferencia(c.getIdReferencia());
                cDTO.setUnidades(c.getUnidades());
                return cDTO;
            }).collect(Collectors.toList());
            dto.setConceptosAsignados(conceptosDTO);
        }

        return dto;
    }

    public Empleado guardar(EmpleadoCreateDTO dto){
        if (dto.getLegajo() == null)
            throw new IllegalArgumentException("El legajo no puede ser null");

        if(empleadoRepository.existsById(dto.getLegajo())){
            throw new RuntimeException("El empleado ya existe");
        }

        Empleado e = new Empleado();
        e.setLegajo(dto.getLegajo());
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setCuil(dto.getCuil());
        e.setInicioActividad(dto.getInicioActividad());
        e.setDomicilio(dto.getDomicilio());
        e.setBanco(dto.getBanco());
        e.setCategoria(catRepo.getById(dto.getIdCategoria()));
        e.setSexo(dto.getSexo());
        e.setGremio(dto.getGremio());

        if(dto.getIdArea() != null && !dto.getIdArea().isEmpty()) {
            List<Area> areas = areaRepo.findAllById(dto.getIdArea());
            e.setAreas(areas);
        }

        Empleado empleadoGuardado = empleadoRepository.save(e);

        //Guardar conceptos si lo hay
        if(dto.getConceptosAsignados() != null && !dto.getConceptosAsignados().isEmpty()) {
            dto.getConceptosAsignados().forEach(cdto -> {
                EmpleadoConcepto concepto = new EmpleadoConcepto();
                concepto.setEmpleado(empleadoGuardado);
                concepto.setTipoConcepto(TipoConcepto.valueOf(cdto.getTipoConcepto()));
                concepto.setIdReferencia(cdto.getIdReferencia());
                concepto.setUnidades(cdto.getUnidades() != null ? cdto.getUnidades() : 1);
                conceptoRepo.save(concepto);
            });
        }
        return empleadoGuardado;
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

        if (dto.getEstado() != null) {
            empleado.setEstado(dto.getEstado());
        }

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

    public void eliminar(Integer legajo){
        empleadoRepository.deleteById(legajo);
    }
}
