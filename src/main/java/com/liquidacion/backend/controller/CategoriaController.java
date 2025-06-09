package com.liquidacion.backend.controller;

import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> getAll(){
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public Categoria getById(@PathVariable Integer id){
        return categoriaService.buscarCategoriaPorId(id);
    }

    @PostMapping
    public Categoria create(@RequestBody Categoria categoria){
        return categoriaService.guardarCategoria(categoria);
    }

    //@PutMapping("/{id}")

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        categoriaService.eliminarCategoria(id);
    }
}
