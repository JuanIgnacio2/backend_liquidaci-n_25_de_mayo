package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer legajo;

    private String nombre;
    private String apellido;
    private String cuil;

    @Column(name = "inicio_actividad")
    private LocalDate inicioActividad;

    private String domicilio;
    private String banco;

    @ManyToOne
    @JoinColumn(name="id_Categoria")
    private Categoria categoria;

    private String sexo;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<PagoSueldo> pagos;

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;
}
