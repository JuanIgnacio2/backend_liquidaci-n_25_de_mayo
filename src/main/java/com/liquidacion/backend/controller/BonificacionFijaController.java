package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ConceptosLyFDTO;
import com.liquidacion.backend.services.ConceptosLyFService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bonificaciones-fijas")
@RequiredArgsConstructor
public class BonificacionFijaController {
    private final ConceptosLyFService bonFijaServ;

    @GetMapping
    public ResponseEntity<List<ConceptosLyFDTO>> listar(){
        return ResponseEntity.ok(bonFijaServ.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> buscar(@PathVariable int id){
        return ResponseEntity.ok(bonFijaServ.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ConceptosLyFDTO> crear(@Valid @RequestBody ConceptosLyFDTO bonFijaDTO){
        return ResponseEntity.ok(bonFijaServ.crear(bonFijaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ConceptosLyFDTO bonFijaDTO){
        return ResponseEntity.ok(bonFijaServ.actualizar(id,bonFijaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> eliminar(@PathVariable Integer id){
        bonFijaServ.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
