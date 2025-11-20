package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ActualizarBasicoDTO;
import com.liquidacion.backend.DTO.CategoriaZonaUocraDTO;
import com.liquidacion.backend.DTO.ConvenioDTO;
import com.liquidacion.backend.DTO.ConvenioResumenDTO;
import com.liquidacion.backend.services.ConvenioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/convenios")
@RequiredArgsConstructor
public class ConvenioController {
    private final ConvenioService convenioService;

    @GetMapping
    public ResponseEntity<List<ConvenioResumenDTO>> getResumenConvenios() {
        return ResponseEntity.ok(convenioService.getResumenConvenios());
    }

    @GetMapping("/count")
    public long getGremios(){
        return convenioService.contarGremios();
    }

    @GetMapping("/lyf")
    public ResponseEntity<ConvenioDTO> getConvenioLyf() {
        return ResponseEntity.ok(convenioService.getConvenioLyf());
    }

    @GetMapping("/uocra")
    public ResponseEntity<ConvenioDTO> getConvenioUocra() {
        return ResponseEntity.ok(convenioService.getConvenioUocra());
    }

    @PutMapping("/lyf/basico")
    public ResponseEntity<String> actualizarbasicoLyF(@RequestBody List<ActualizarBasicoDTO> lista) {
        convenioService.actualizarBasicoLyF(lista);
        return ResponseEntity.ok("Básico actualizado correctamente para LUZ Y FUERZA");
    }

    @PutMapping("/uocra/basico")
    public ResponseEntity<String> actualizarbasicoUocra(@RequestBody List<ActualizarBasicoDTO> lista) {
        convenioService.actualizarBasicoUocra(lista);
        return ResponseEntity.ok("Básico actualizado correctamente para UOCRA");
    }

    @GetMapping("/uocra/basico/{idCategoria}/{idZona}")
    public ResponseEntity<CategoriaZonaUocraDTO> obtenerBasicoUocra(@PathVariable Integer idCategoria, @PathVariable Integer idZona) {
        return ResponseEntity.ok(convenioService.obtenerBasicoPorCategoriaYZona(idCategoria, idZona));
    }
}