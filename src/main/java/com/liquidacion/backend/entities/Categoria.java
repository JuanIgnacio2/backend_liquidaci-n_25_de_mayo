package com.liquidacion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Para Luz y Fuerza tiene valor real.
    // Para UOCRA se setea en 0 y se consulta en basicos_uocra.
    @Column(name = "basico", nullable = false, precision = 10, scale = 2)
    private BigDecimal basico;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Empleado> empleados;
}