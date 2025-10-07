package com.liquidacion.backend.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para actualizar básicos en los gremios
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarBasicoDTO {
    private Integer idCategoria;
    private Integer idZona; // solo se usa para UOCRA
    private BigDecimal basico;
}
