package com.liquidacion.backend.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardLiquidacionesDTO {
    private BigDecimal totalBrutoMes;
    private BigDecimal totalNetoMes;

    private Integer cantidadEmpleados;
    private Integer cantidadLiquidacionesHechas;
    private Integer cantidadLiquidacionesPendientes;
}
