package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.LiquidacionSueldoDTO;
import com.liquidacion.backend.DTO.PagoSueldoDTO;
import com.liquidacion.backend.DTO.PagoSueldoDetalleDTO;
import com.liquidacion.backend.entities.PagoSueldo;
import com.liquidacion.backend.services.LiquidacionSueldosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
public class LiquidacionController {

    private final LiquidacionSueldosService liquidacionSueldosService;

    @GetMapping
    public ResponseEntity<List<PagoSueldoDTO>> listarPagos() {
        List<PagoSueldoDTO> pagos = liquidacionSueldosService.listarTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("(/{idPago})")
    public ResponseEntity<PagoSueldoDetalleDTO> obtenerDetallePago(@PathVariable Integer idPago) {
        PagoSueldoDetalleDTO detalle = liquidacionSueldosService.obtenerPagoConDetalle(idPago);
        return ResponseEntity.ok(detalle);
    }

    @PostMapping
    public ResponseEntity<PagoSueldo> liquidarSueldo(@RequestBody LiquidacionSueldoDTO dto) {
        LocalDate fechaPago = LocalDate.now();
        PagoSueldo pago = liquidacionSueldosService.liquidarSueldo(dto, fechaPago);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoSueldo> obtenerPago(@PathVariable Integer idPago){
        return ResponseEntity.of(liquidacionSueldosService.obtenerPagoPorId(Long.valueOf(idPago)));
    }

    @GetMapping("/empleado/{legajo}")
    public List<PagoSueldo> listarPagosPorEmpleado(@PathVariable Integer legajo){
        return liquidacionSueldosService.listarPagosPorEmpleado(legajo);
    }
}
