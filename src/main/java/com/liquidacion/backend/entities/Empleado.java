package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @Column(name = "legajo")
    private Integer legajo;   // PK (sin autoincremento porque en tu SQL lo querías empezar desde 1001)

    private String nombre;
    private String apellido;
    private String cuil;

    @Column(name = "inicio_actividad")
    private LocalDate inicioActividad;

    private String domicilio;
    private String banco;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private Categoria categoria;

    private String sexo;

    @ManyToOne
    @JoinColumn(name = "id_gremio", nullable = false)
    private Gremio gremio;

    @ManyToOne
    @JoinColumn(name = "id_zona")
    private ZonasUocra zona; // solo aplica a UOCRA

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<PagoSueldo> pagos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "empleado_areas",
            joinColumns = @JoinColumn(name = "legajo"),
            inverseJoinColumns = @JoinColumn(name = "id_area")
    )
    private List<Area> areas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEmpleado estado = EstadoEmpleado.ACTIVO;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmpleadoConcepto> conceptos = new ArrayList<>();
}