package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.ActividadCreateDTO;
import com.liquidacion.backend.DTO.ActividadDTO;
import com.liquidacion.backend.entities.ReferenciaTipo;
import com.liquidacion.backend.services.ActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividad")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    @PostMapping
    public ResponseEntity<ActividadDTO> registrarActividad(@RequestBody ActividadCreateDTO dto) {
        ActividadDTO creada = actividadService.registrarActividad(dto);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/reciente")
    public ResponseEntity<List<ActividadDTO>> listarReciente(
            @RequestParam(name = "limite", required = false, defaultValue = "20") Integer limite
    ) {
        return ResponseEntity.ok(actividadService.listarReciente(limite));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ActividadDTO>> listarPorTipo(@PathVariable("tipo") ReferenciaTipo tipo) {
        return ResponseEntity.ok(actividadService.listarPorTipo(tipo));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<ActividadDTO>> listarPorUsuario(@PathVariable("usuario") String usuario) {
        return ResponseEntity.ok(actividadService.listarPorUsuario(usuario));
    }
}


