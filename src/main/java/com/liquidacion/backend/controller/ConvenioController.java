package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ConvenioFilaDTO;
import com.liquidacion.backend.services.ConvenioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/convenios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConvenioController {
    private final ConvenioService convenioService;

    @GetMapping
    public ResponseEntity<List<ConvenioFilaDTO>> listarConvenios() {
        return ResponseEntity.ok(convenioService.obtenerConvenios());
    }
}
