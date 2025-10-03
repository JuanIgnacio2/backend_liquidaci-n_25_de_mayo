package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.EmpleadoConceptoDTO;
import com.liquidacion.backend.services.EmpleadoConceptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleado-conceptos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmpleadoConceptoController {

    private final EmpleadoConceptoService empleadoConceptoService;

    @PostMapping
    public ResponseEntity<EmpleadoConceptoDTO> asignar(@RequestBody EmpleadoConceptoDTO dto){
        return ResponseEntity.ok(empleadoConceptoService.asignarConcepto(dto));
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoConceptoDTO>> listar(){
        return ResponseEntity.ok(empleadoConceptoService.buscarTodos());
    }

    @GetMapping("/por-legajo/{legajo}")
    public ResponseEntity<List<EmpleadoConceptoDTO>> listarPorLegajo(@PathVariable Integer legajo){
        return ResponseEntity.ok(empleadoConceptoService.findByEmpleado_Legajo(legajo));
    }

    @PutMapping("/modificar")
    public ResponseEntity<EmpleadoConceptoDTO> modificar(@RequestBody EmpleadoConceptoDTO dto){
        return empleadoConceptoService.modificar(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
