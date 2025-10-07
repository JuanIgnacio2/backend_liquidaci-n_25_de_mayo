package com.liquidacion.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "conceptos")
public class Concepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_concepto;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConcepto tipo;
}