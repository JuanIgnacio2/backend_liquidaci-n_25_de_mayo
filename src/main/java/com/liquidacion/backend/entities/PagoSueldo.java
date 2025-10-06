package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pagos_sueldos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PagoSueldo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
    @Column(name = "periodo_pago")
    private String periodoPago;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "legajo")
    private Empleado empleado;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoConcepto> conceptos;
}