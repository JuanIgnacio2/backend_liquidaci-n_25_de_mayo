package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.Gremio;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmpleadoListDTO {
    private Integer legajo;
    private String nombre;
    private String apellido;
    private String cuil;
    private LocalDate inicioActividad;
    private String domicilio;
    private String banco;
    private Integer idCategoria;
    private String categoria;
    private String sexo;
    private List<Integer> idAreas;
    private List<String> nombreAreas;
    private Gremio gremio;
}
