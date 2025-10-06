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

    @Column(nullable = false, unique = true)
    private String nombre;

    // Si representa % sobre el básico -> mantenemos porcentaje.
    // Si representa monto fijo -> conviene renombrar a "monto".
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal porcentaje;
}