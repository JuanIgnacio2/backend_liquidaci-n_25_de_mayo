package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.EstadoEmpleado;
import com.liquidacion.backend.entities.Gremio;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private List<Integer> idAreas;

    @NotBlank
    private String sexo;

    @NotNull
    private Integer idGremio;

    private Integer idZonaUocra;

    private EstadoEmpleado estado;

    private List<EmpleadoConceptoDTO> conceptosAsignados;
}