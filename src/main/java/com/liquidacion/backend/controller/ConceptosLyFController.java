package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ConceptosLyFDTO;
import com.liquidacion.backend.services.ConceptosLyFService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conceptos_lyf")
@RequiredArgsConstructor
public class ConceptosLyFController {
    private final ConceptosLyFService concepLyFServ;

    @GetMapping
    public ResponseEntity<List<ConceptosLyFDTO>> listar(){
        return ResponseEntity.ok(concepLyFServ.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> buscar(@PathVariable int id){
        return ResponseEntity.ok(concepLyFServ.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ConceptosLyFDTO> crear(@Valid @RequestBody ConceptosLyFDTO concepLyFDTO){
        return ResponseEntity.ok(concepLyFServ.crear(concepLyFDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ConceptosLyFDTO concepLyFDTO){
        return ResponseEntity.ok(concepLyFServ.actualizar(id,concepLyFDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> eliminar(@PathVariable Integer id){
        concepLyFServ.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
