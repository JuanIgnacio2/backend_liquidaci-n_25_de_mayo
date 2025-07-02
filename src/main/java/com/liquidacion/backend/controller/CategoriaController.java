package com.liquidacion.backend.controller;

import com.liquidacion.backend.DTO.CategoriaCreateDTO;
import com.liquidacion.backend.DTO.CategoriaListDTO;
import com.liquidacion.backend.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaListDTO> getAll(){
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public CategoriaListDTO getById(@PathVariable Integer id){
        return categoriaService.buscarCategoriaPorId(id);
    }

    @PostMapping
    public CategoriaListDTO create(@RequestBody CategoriaCreateDTO dto){
        return categoriaService.crearCategoria(dto);
    }

    @PutMapping("/{id}")
    public CategoriaListDTO update(@PathVariable Integer id, @RequestBody CategoriaCreateDTO dto){
        return categoriaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        categoriaService.eliminarCategoria(id);
    }
}
