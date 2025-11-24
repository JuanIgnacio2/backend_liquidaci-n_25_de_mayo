package com.liquidacion.backend.controller;

import com.liquidacion.backend.entities.Descuentos;
import com.liquidacion.backend.services.DescuentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/descuento")
@RequiredArgsConstructor
public class DescuentoController {
    private final DescuentoService descuentoService;

    @GetMapping
    public ResponseEntity<List<Descuentos>> listar(){
        return ResponseEntity.ok(descuentoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Descuentos> buscar(@PathVariable int id){
        return ResponseEntity.ok(descuentoService.obtenerPorId(id));
    }
}
