package com.liquidacion.backend.controller;

import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> listarEmpleados() {
        return empleadoService.listar();
    }

    @GetMapping("/{legajo}")
    public Empleado getbyLegajo(@PathVariable Integer legajo) {
        return empleadoService.obtenerPorLegajo(legajo);
    }

    @PostMapping
    public Empleado registrarEmpleado(@RequestBody Empleado empleado){
        return empleadoService.guardar(empleado);
    }

    @PutMapping("/{legajo}")
    public Empleado actualizarEmpleado(@PathVariable Integer legajo, @RequestBody Empleado empleado){
        return empleadoService.actualizar(legajo, empleado);
    }

    @DeleteMapping("/{legajo}")
    public void eliminarEmpleado(@PathVariable Integer legajo) {
        empleadoService.eliminar(legajo);
    }
}
