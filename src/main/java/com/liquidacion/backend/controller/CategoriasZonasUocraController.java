package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.CategoriaZonaUocraCreateDTO;
import com.liquidacion.backend.DTO.CategoriaZonaUocraDTO;
import com.liquidacion.backend.services.CategoriasZonasUocraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-zonas-uocra")
@RequiredArgsConstructor
public class CategoriasZonasUocraController {
    private final CategoriasZonasUocraService categoriasZonasUocraService;

    @GetMapping
    public ResponseEntity<List<CategoriaZonaUocraDTO>> listarTodos() {
        return ResponseEntity.ok(categoriasZonasUocraService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaZonaUocraDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriasZonasUocraService.obtenerPorId(id));
    }

    @GetMapping("/categoria/{idCategoria}/zona/{idZona}")
    public ResponseEntity<CategoriaZonaUocraDTO> obtenerPorCategoriaYZona(
            @PathVariable Integer idCategoria,
            @PathVariable Integer idZona) {
        return ResponseEntity.ok(categoriasZonasUocraService.obtenerPorCategoriaYZona(idCategoria, idZona));
    }

    @GetMapping("/zona/{idZona}")
    public ResponseEntity<List<CategoriaZonaUocraDTO>> listarPorZona(@PathVariable Integer idZona) {
        return ResponseEntity.ok(categoriasZonasUocraService.listarPorZona(idZona));
    }

    @PostMapping
    public ResponseEntity<CategoriaZonaUocraDTO> crear(@Valid @RequestBody CategoriaZonaUocraCreateDTO dto) {
        CategoriaZonaUocraDTO creado = categoriasZonasUocraService.crear(
                dto.getIdCategoria(),
                dto.getIdZona(),
                dto.getBasico()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaZonaUocraDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody BigDecimal basico) {
        return ResponseEntity.ok(categoriasZonasUocraService.actualizar(id, basico));
    }

    @PutMapping("/categoria/{idCategoria}/zona/{idZona}")
    public ResponseEntity<CategoriaZonaUocraDTO> actualizarPorCategoriaYZona(
            @PathVariable Integer idCategoria,
            @PathVariable Integer idZona,
            @RequestBody BigDecimal basico) {
        return ResponseEntity.ok(categoriasZonasUocraService.actualizarPorCategoriaYZona(idCategoria, idZona, basico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriasZonasUocraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/categoria/{idCategoria}/zona/{idZona}")
    public ResponseEntity<Void> eliminarPorCategoriaYZona(
            @PathVariable Integer idCategoria,
            @PathVariable Integer idZona) {
        categoriasZonasUocraService.eliminarPorCategoriaYZona(idCategoria, idZona);
        return ResponseEntity.noContent().build();
    }
}
