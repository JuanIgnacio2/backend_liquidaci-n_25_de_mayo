package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.CategoriaCreateDTO;
import com.liquidacion.backend.DTO.CategoriaListDTO;
import com.liquidacion.backend.DTO.CategoriaUpdateDTO;
import com.liquidacion.backend.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaListDTO>> listarCategorias(){
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaListDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaListDTO> crearCategoria(@Valid @RequestBody CategoriaCreateDTO dto) {
        return ResponseEntity.ok(categoriaService.crearCategoria(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaListDTO> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaUpdateDTO dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        categoriaService.eliminarCategoria(id);
    }
}