package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bonificaciones_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonificacionVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bonificacion_variable")
    private int idBonificacionVariable;

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    private BigDecimal porcentaje;
}
