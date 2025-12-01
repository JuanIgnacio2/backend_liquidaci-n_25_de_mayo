package com.liquidacion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer legajo;

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

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    @JsonIgnore
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

    // Relación con empleado_zona (1 solo registro por empleado)
    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmpleadoZona empleadoZona;
}