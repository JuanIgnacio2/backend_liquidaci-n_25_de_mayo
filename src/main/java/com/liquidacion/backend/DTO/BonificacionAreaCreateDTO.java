package com.liquidacion.backend.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BonificacionAreaCreateDTO {
    @NotNull private Integer idArea;
    @NotNull private Integer idCategoria;
    @NotNull @Positive
    private BigDecimal porcentaje;
}
