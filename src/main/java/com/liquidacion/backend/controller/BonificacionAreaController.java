package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.BonificacionAreaDTO;
import com.liquidacion.backend.services.BonificacionAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bonificaciones-variables")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BonificacionAreaController {

    private final BonificacionAreaService bonAreaService;

    @GetMapping("/area/{idArea}")
    public ResponseEntity<List<BonificacionAreaDTO>> obtenerPorArea(@PathVariable Integer idArea){
        return ResponseEntity.ok(bonAreaService.listarPorArea(idArea));
    }

    @GetMapping("/area/{idArea}/categoria/{idCat}")
    public ResponseEntity<BigDecimal> porcentaje(
            @PathVariable Integer idArea,
            @PathVariable Integer idCat){
        BigDecimal porcentaje = bonAreaService.obtenerPorcentaje(idArea, idCat);
        return ResponseEntity.ok(porcentaje);
    }

    /*@PostMapping
    public ResponseEntity<BonificacionAreaDTO> crear(@Valid @RequestBody BonificacionAreaDTO bonificacionAreaDTO){
        return ResponseEntity.ok(bonAreaService.crear(dto));
    }*/
}
