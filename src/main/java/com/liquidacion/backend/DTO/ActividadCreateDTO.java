package com.liquidacion.backend.DTO;

import com.liquidacion.backend.entities.ReferenciaTipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadCreateDTO {
    private String usuario;
    private String accion;
    private String descripcion;
    private ReferenciaTipo referenciaTipo;
    private Integer referenciaId;
}


