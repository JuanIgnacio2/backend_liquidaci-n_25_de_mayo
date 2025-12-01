package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ConceptosLyFDTO;
import com.liquidacion.backend.DTO.ConceptosUocraDTO;
import com.liquidacion.backend.services.ConceptosUocraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conceptos_uocra")
@RequiredArgsConstructor
public class ConceptosUocraController {
    private final ConceptosUocraService conUocraServ;

    @GetMapping
    public ResponseEntity<List<ConceptosUocraDTO>> listar(){
        return ResponseEntity.ok(conUocraServ.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConceptosUocraDTO> buscar(@PathVariable int id){
        return ResponseEntity.ok(conUocraServ.buscar(id));
    }

    @PostMapping
    public ResponseEntity<ConceptosUocraDTO> crear(@Valid @RequestBody ConceptosUocraDTO conCepUocraDTO){
        return ResponseEntity.ok(conUocraServ.crear(conCepUocraDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConceptosUocraDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ConceptosUocraDTO conCepUocraDTO){
        return ResponseEntity.ok(conUocraServ.actualizar(id,conCepUocraDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConceptosLyFDTO> eliminar(@PathVariable Integer id){
        conUocraServ.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
