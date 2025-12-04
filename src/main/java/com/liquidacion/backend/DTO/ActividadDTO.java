package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.ReferenciaTipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadDTO {
    private Long idActividad;
    private LocalDateTime fecha;
    private String usuario;
    private String accion;
    private String descripcion;
    private ReferenciaTipo referenciaTipo;
    private Integer referenciaId;
}


