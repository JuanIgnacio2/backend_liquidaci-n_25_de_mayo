package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleado_zona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoZona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación UNO A UNO con Empleado
    @OneToOne
    @JoinColumn(name = "legajo_empleado", nullable = false, unique = true)
    private Empleado empleado;

    // Relación MUCHOS A UNO con la tabla zonas_uocra
    @ManyToOne
    @JoinColumn(name = "id_zona", nullable = false)
    private ZonasUocra zona;
}
