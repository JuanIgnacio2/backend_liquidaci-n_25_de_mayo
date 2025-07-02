package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.LiquidacionSueldoDTO;
import com.liquidacion.backend.DTO.PagoSueldoResumenDTO;
import com.liquidacion.backend.DTO.PagoSueldoDetalleDTO;
import com.liquidacion.backend.entities.PagoSueldo;
import com.liquidacion.backend.services.LiquidacionSueldosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/liquidaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LiquidacionController {

    private final LiquidacionSueldosService liquidacionSueldosService;

    @GetMapping
    public ResponseEntity<List<PagoSueldoResumenDTO>> listarPagos() {
        List<PagoSueldoResumenDTO> pagos = liquidacionSueldosService.listarTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoSueldoDetalleDTO> obtenerDetallePago(@PathVariable Integer idPago) {
        PagoSueldoDetalleDTO detalle = liquidacionSueldosService.obtenerPagoConDetalle(idPago);
        return ResponseEntity.ok(detalle);
    }

    @PostMapping
    public ResponseEntity<PagoSueldo> liquidarSueldo(@Valid @RequestBody LiquidacionSueldoDTO dto) {
        LocalDate fechaPago = LocalDate.now();
        PagoSueldo pago = liquidacionSueldosService.liquidarSueldo(dto, fechaPago);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/empleado/{legajo}")
    public List<PagoSueldo> listarPagosPorEmpleado(@PathVariable Integer legajo){
        return liquidacionSueldosService.listarPagosPorEmpleado(legajo);
    }
}
