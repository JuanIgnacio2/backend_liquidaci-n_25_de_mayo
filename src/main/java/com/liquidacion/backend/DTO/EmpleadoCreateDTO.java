package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.Gremio;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class EmpleadoCreateDTO {
    @NotNull
    private Integer legajo;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String cuil;

    @NotBlank
    private LocalDate inicioActividad;

    private String domicilio;
    private String banco;

    @NotNull
    private Integer idCategoria;
    @NotNull
    private Integer idArea;
    @NotBlank
    private String sexo;
    @NotNull
    private Gremio gremio;
}
