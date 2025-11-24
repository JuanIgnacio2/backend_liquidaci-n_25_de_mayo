package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "conceptos_lyf")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptosLyF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conceptos_lyf")
    private Integer idConceptosLyf;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Si representa % sobre el básico -> mantenemos porcentaje.
    // Si representa monto fijo -> conviene renombrar a "monto".
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal porcentaje;
}