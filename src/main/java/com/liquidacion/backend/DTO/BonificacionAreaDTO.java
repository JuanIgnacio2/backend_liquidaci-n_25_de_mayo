package com.liquidacion.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionAreaDTO {
    private Integer id;
    private Integer idArea;
    private String nombreArea;
    private Integer idCategoria;
    private String nombreCategoria;
    private BigDecimal porcentaje;
}
