package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bonificaciones_fijas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonificacionFija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bonificacion_fija")
    private Integer idBonificacionFija;

    private String nombre;
    private BigDecimal porcentaje;
}
