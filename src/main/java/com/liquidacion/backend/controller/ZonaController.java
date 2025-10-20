package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ZonaDTO;
import com.liquidacion.backend.services.ZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaService zonaService;

    @GetMapping
    public ResponseEntity<List<ZonaDTO>> listarZonas() {
        return ResponseEntity.ok(zonaService.listarZonasConCategorias());
    }
}
