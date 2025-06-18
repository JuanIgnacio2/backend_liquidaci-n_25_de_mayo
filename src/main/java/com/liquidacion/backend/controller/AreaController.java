package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.AreaCreateDTO;
import com.liquidacion.backend.DTO.AreaListDTO;
import com.liquidacion.backend.services.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    public List<AreaListDTO> listar(){
        return areaService.listarAreas();
    }

    @GetMapping("/{id}")
    public AreaListDTO get(@PathVariable Integer id){
        return areaService.obtenerPorId(id);
    }

    @PostMapping
    public AreaListDTO create(@Valid @RequestBody AreaCreateDTO dto){
        return areaService.crear(dto);
    }

    @PutMapping("/{id}")
    public AreaListDTO update(@PathVariable Integer id, @Valid @RequestBody AreaCreateDTO dto){
        return areaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        areaService.eliminar(id);
    }
}
