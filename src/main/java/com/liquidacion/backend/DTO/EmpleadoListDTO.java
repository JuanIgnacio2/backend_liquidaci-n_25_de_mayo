package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.EstadoEmpleado;
import com.liquidacion.backend.entities.Gremio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    private EstadoEmpleado estado;
    private Gremio gremio;

    private List<EmpleadoConceptoDTO> conceptosAsignados;
}
