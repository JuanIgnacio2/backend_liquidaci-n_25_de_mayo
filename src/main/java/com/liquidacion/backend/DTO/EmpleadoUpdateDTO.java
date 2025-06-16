package com.liquidacion.backend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.liquidacion.backend.entities.Gremio;
import lombok.Data;

import java.time.LocalDate;

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
    private Integer idArea;
    private String sexo;
    private Gremio gremio;
}
