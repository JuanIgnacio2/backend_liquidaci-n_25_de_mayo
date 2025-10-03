package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.EmpleadoCreateDTO;
import com.liquidacion.backend.DTO.EmpleadoListDTO;
import com.liquidacion.backend.DTO.EmpleadoUpdateDTO;
import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.services.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoListDTO>> listarEmpleados() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoListDTO> getbyLegajo(@PathVariable Integer legajo) {
        return ResponseEntity.ok(empleadoService.obtenerPorLegajo(legajo));
    }

    @PostMapping
    public ResponseEntity<Empleado> registrarEmpleado(@Valid @RequestBody EmpleadoCreateDTO dto){
        return ResponseEntity.ok(empleadoService.guardar(dto));
    }

    @PutMapping("/{legajo}")
    public Empleado actualizarEmpleado(@PathVariable Integer legajo, @Valid @RequestBody EmpleadoUpdateDTO dto) {
        return empleadoService.actualizar(legajo, dto);
    }

    @DeleteMapping("/{legajo}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer legajo) {
        empleadoService.eliminar(legajo);
        return ResponseEntity.noContent().build();
    }
}
