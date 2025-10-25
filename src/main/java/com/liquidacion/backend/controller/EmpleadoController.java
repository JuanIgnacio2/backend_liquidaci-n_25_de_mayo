package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.EmpleadoCreateDTO;
import com.liquidacion.backend.DTO.EmpleadoListDTO;
import com.liquidacion.backend.DTO.EmpleadoUpdateDTO;
import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.services.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoListDTO>> listarEmpleados() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<EmpleadoListDTO>> listarActivos() {
        return ResponseEntity.ok(empleadoService.listarActivos());
    }

    @GetMapping("/dados-de-baja")
    public ResponseEntity<List<EmpleadoListDTO>> listarDadosDeBaja() {
        return ResponseEntity.ok(empleadoService.listarDadosDeBaja());
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoListDTO> getbyLegajo(@PathVariable Integer legajo) {
        return ResponseEntity.ok(empleadoService.obtenerPorLegajo(legajo));
    }

    @PostMapping
    public ResponseEntity<EmpleadoListDTO> registrarEmpleado(@Valid @RequestBody EmpleadoCreateDTO dto){
        return ResponseEntity.ok(empleadoService.guardar(dto));
    }

    @PutMapping("/{legajo}")
    public Empleado actualizarEmpleado(@PathVariable Integer legajo, @Valid @RequestBody EmpleadoUpdateDTO dto) {
        return empleadoService.actualizar(legajo, dto);
    }

    @PutMapping("/{legajo}/estado")
    public ResponseEntity<EmpleadoListDTO> cambiarEstado(@PathVariable Integer legajo) {
        return ResponseEntity.ok(empleadoService.cambiarEstado(legajo));
    }

    @GetMapping("/count/activos")
    public long getEmpleadosActivos(){
        return empleadoService.contarEmpleadosActivos();
    }
}
