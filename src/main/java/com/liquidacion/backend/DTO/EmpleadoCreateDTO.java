package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.EstadoEmpleado;
import com.liquidacion.backend.entities.Gremio;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

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

    @NotNull
    private LocalDate inicioActividad;

    private String domicilio;
    private String banco;

    @NotNull
    private Integer idCategoria;

    @NotEmpty
    private List<Integer> idArea;
    @NotBlank
    private String sexo;
    @NotNull
    private Gremio gremio;

    private EstadoEmpleado estado;

    private List<EmpleadoConceptoDTO> conceptosAsignados;
}
