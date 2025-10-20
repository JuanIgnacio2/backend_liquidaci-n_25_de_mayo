package com.liquidacion.backend.mappers;

import com.liquidacion.backend.DTO.EmpleadoConceptoDTO;
import com.liquidacion.backend.DTO.EmpleadoCreateDTO;
import com.liquidacion.backend.DTO.EmpleadoListDTO;
import com.liquidacion.backend.DTO.EmpleadoUpdateDTO;
import com.liquidacion.backend.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class EmpleadoMapper {

    public static EmpleadoListDTO toListDTO(Empleado e) {
        EmpleadoListDTO dto = new EmpleadoListDTO();
        dto.setLegajo(e.getLegajo());
        dto.setNombre(e.getNombre());
        dto.setApellido(e.getApellido());
        dto.setCuil(e.getCuil());
        dto.setDomicilio(e.getDomicilio());
        dto.setInicioActividad(e.getInicioActividad());
        dto.setBanco(e.getBanco());
        dto.setSexo(e.getSexo());
        dto.setEstado(e.getEstado());

        if(e.getCategoria() != null){
            dto.setIdCategoria(e.getCategoria().getIdCategoria());
            dto.setCategoria(e.getCategoria().getNombre());
        }

        if(e.getAreas() != null){
            dto.setIdAreas(e.getAreas().stream().map(Area::getIdArea).collect(Collectors.toList()));
            dto.setNombreAreas(e.getAreas().stream().map(Area::getNombre).collect(Collectors.toList()));
        }

        dto.setGremio(GremioMapper.toDTO(e.getGremio()));

        if(e.getConceptos() != null){
            dto.setConceptosAsignados(
                    e.getConceptos().stream()
                            .map(EmpleadoMapper::toConceptoDTO)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public static Empleado toEntity(
            EmpleadoCreateDTO dto,
            Categoria categoria,
            Gremio gremio,
            List<Area> areas,
            ZonasUocra zona) {

        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setDomicilio(dto.getDomicilio());
        empleado.setCuil(dto.getCuil());
        empleado.setEstado(dto.getEstado());
        empleado.setSexo(dto.getSexo());
        empleado.setInicioActividad(dto.getInicioActividad());
        empleado.setCategoria(categoria);
        empleado.setGremio(gremio);
        empleado.setBanco(dto.getBanco());

        if (areas != null) {
            empleado.setAreas(areas);
        }

        if (zona != null) {
            empleado.setZona(zona);
        }

        return empleado;
    }



    public static void updateEntity(Empleado e, EmpleadoUpdateDTO dto, Categoria categoria, List<Area> areas) {
        if (dto.getNombre() != null) e.setNombre(dto.getNombre());
        if (dto.getApellido() != null) e.setApellido(dto.getApellido());
        if (dto.getCuil() != null) e.setCuil(dto.getCuil());
        if (dto.getInicioActividad() != null) e.setInicioActividad(dto.getInicioActividad());
        if (dto.getDomicilio() != null) e.setDomicilio(dto.getDomicilio());
        if (dto.getBanco() != null) e.setBanco(dto.getBanco());
        if (categoria != null) e.setCategoria(categoria);
        if (areas != null && !areas.isEmpty()) e.setAreas(areas);
        if (dto.getEstado() != null) e.setEstado(dto.getEstado());
        if (dto.getSexo() != null) e.setSexo(dto.getSexo());
        if (dto.getGremio() != null) e.setGremio(dto.getGremio());
    }

    public static EmpleadoConceptoDTO toConceptoDTO(EmpleadoConcepto c) {
        EmpleadoConceptoDTO dto = new EmpleadoConceptoDTO();
        dto.setId_empleado_concepto(c.getId_empleado_concepto());
        dto.setLegajo(c.getEmpleado().getLegajo());
        dto.setTipoConcepto(c.getTipoConcepto().name());
        dto.setIdReferencia(c.getIdReferencia());
        dto.setUnidades(c.getUnidades());
        return dto;
    }
}
