package com.liquidacion.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaZonaUocraCreateDTO {
    @NotNull
    private Integer idCategoria;
    
    @NotNull
    private Integer idZona;
    
    @NotNull
    private BigDecimal basico;
}



