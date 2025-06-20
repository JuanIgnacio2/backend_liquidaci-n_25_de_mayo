package com.liquidacion.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConceptoInputDTO {
    @NotBlank(message = "tipoConcepto es obligatorio")
    private String tipoConcepto;
    private Integer idReferencia;
    private Integer unidades;
}
