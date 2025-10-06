package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "gremio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gremio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gremio")
    private Integer idGremio;

    private String nombre;

    @OneToMany(mappedBy = "gremio")
    private List<Empleado> empleados;
}
