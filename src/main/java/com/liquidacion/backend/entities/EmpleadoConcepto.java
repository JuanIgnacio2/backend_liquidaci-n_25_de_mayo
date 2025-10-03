package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleado_conceptos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoConcepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private Integer legajo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_concepto")
    private TipoConcepto tipoConcepto;

    @Column(nullable = false)
    private Integer idReferencia;

    private Integer unidades = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legajo")
    private Empleado empleado;
}
