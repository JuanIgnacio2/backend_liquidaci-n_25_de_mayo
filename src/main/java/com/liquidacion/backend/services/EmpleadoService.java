package com.liquidacion.backend.services;

import com.liquidacion.backend.entities.Empleado;
import com.liquidacion.backend.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

    public List<Empleado> listar(){
        return empleadoRepository.findAll();
    }

    public Empleado obtenerPorLegajo(Integer legajo){
        return empleadoRepository.findById(legajo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public Empleado guardar(Empleado empleado){
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(Integer legajo, Empleado empleadoActualizado){
        Empleado empleado = obtenerPorLegajo(legajo);
        empleado.setNombre(empleadoActualizado.getNombre());
        empleado.setApellido(empleadoActualizado.getApellido());
        empleado.setCuil(empleadoActualizado.getCuil());
        empleado.setInicioActividad(empleadoActualizado.getInicioActividad());
        empleado.setDomicilio(empleadoActualizado.getDomicilio());
        empleado.setBanco(empleadoActualizado.getBanco());
        empleado.setCategoria(empleadoActualizado.getCategoria());
        empleado.setSexo(empleadoActualizado.getSexo());
        return empleadoRepository.save(empleado);
    }

    public void eliminar(Integer legajo){
        empleadoRepository.deleteById(legajo);
    }
}
