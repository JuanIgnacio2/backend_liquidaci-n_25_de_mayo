package com.liquidacion.backend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.liquidacion.backend.entities.EstadoEmpleado;
import com.liquidacion.backend.entities.Gremio;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmpleadoUpdateDTO {
    private int legajo;
    private String nombre;
    private String apellido;
    private String cuil;
    private LocalDate inicioActividad;
    private String domicilio;
    private String banco;
    private Integer idCategoria;
    private List<Integer> idAreas;
    private String sexo;
    private Gremio gremio;
    private Integer idZonaUocra; // Opcional (UOCRA)

    private EstadoEmpleado estado; // Se puede cambiar en update

    private List<EmpleadoConceptoDTO> conceptosAsignados;
}