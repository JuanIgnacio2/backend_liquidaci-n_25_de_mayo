package com.liquidacion.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pago_conceptos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoConcepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_concepto")
    private Integer idPagoConcepto;

    @ManyToOne
    @JoinColumn(name = "id_pago")
    private PagoSueldo pago;

    @Column(name = "tipo_concepto")
    @Enumerated(EnumType.STRING)
    private TipoConcepto tipoConcepto;

    @Column(name = "id_referencia", nullable = true)
    private Integer idReferencia;

    private Integer unidades;

    @Column(name = "monto_unitario")
    private BigDecimal montoUnitario;

    private BigDecimal total;
}
