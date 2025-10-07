package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.BonificacionFijaDTO;
import com.liquidacion.backend.services.BonificacionFijaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonificaciones-fijas")
@RequiredArgsConstructor
public class BonificacionFijaController {
    private final BonificacionFijaService bonFijaServ;

    @GetMapping
    public ResponseEntity<List<BonificacionFijaDTO>> listar(){
        return ResponseEntity.ok(bonFijaServ.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BonificacionFijaDTO> buscar(@PathVariable int id){
        return ResponseEntity.ok(bonFijaServ.buscar(id));
    }

    @PostMapping
    public ResponseEntity<BonificacionFijaDTO> crear(@Valid @RequestBody BonificacionFijaDTO bonFijaDTO){
        return ResponseEntity.ok(bonFijaServ.crear(bonFijaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BonificacionFijaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody BonificacionFijaDTO bonFijaDTO){
        return ResponseEntity.ok(bonFijaServ.actualizar(id,bonFijaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BonificacionFijaDTO> eliminar(@PathVariable Integer id){
        bonFijaServ.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
