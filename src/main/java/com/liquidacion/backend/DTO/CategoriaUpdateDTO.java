package com.liquidacion.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CategoriaUpdateDTO {
    @NotBlank
    private String nombreCategoria;
    @NotNull
    @Positive
    private BigDecimal basico;
}