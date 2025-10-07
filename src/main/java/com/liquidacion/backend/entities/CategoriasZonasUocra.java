package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "categoria_zona_uocra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriasZonasUocra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_zona", nullable = false)
    private ZonasUocra zona;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basico;
}
