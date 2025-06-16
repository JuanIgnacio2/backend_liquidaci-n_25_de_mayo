package com.liquidacion.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AreaCreateDTO {
    @NotBlank
    private String nombre;
}
