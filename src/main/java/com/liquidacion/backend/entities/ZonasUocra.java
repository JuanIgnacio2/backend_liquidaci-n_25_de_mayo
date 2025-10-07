package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "zonas_uocra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZonasUocra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;

    @Column(nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "zona")
    private List<CategoriasZonasUocra> categoriasZonas;
}
