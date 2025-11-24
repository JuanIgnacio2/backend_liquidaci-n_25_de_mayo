package com.liquidacion.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "id_pago", nullable = false)
    @JsonIgnore
    private PagoSueldo pago;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_concepto", nullable = false)
    private TipoConcepto tipoConcepto;

    // Referencia a Bonificación Fija
    @ManyToOne
    @JoinColumn(name = "id_bonificacion_fija")
    private ConceptosLyF conceptosLyF;

    // Referencia a Bonificación Área Luz y Fuerza
    @ManyToOne
    @JoinColumn(name = "id_bonificacion_area")
    private BonificacionAreaLyF bonificacionAreaLyF;

    // Referencia a Categoría (Luz y Fuerza)
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Referencia a Categoría-Zona (UOCRA)
    @ManyToOne
    @JoinColumn(name = "id_categoria_zona_uocra")
    private CategoriasZonasUocra categoriaZonaUocra;

    // Referencia a Descuento
    @ManyToOne
    @JoinColumn(name = "id_descuento")
    private Descuentos descuento;

    // Cantidad de unidades (si aplica)
    private Integer unidades = 1;

    // Monto unitario (si aplica)
    @Column(name = "monto", precision = 12, scale = 2)
    private BigDecimal montoUnitario;

    // Total del concepto para este pago
    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;
}