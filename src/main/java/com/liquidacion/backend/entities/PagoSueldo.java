package com.liquidacion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private BigDecimal total_bruto;
    private BigDecimal total_descuentos;
    private BigDecimal total_neto;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "legajo")
    private Empleado empleado;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<PagoConcepto> pagoConceptos = new ArrayList<>();
}