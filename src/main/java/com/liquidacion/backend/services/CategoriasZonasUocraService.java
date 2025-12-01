package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.CategoriaZonaUocraDTO;
import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.entities.CategoriasZonasUocra;
import com.liquidacion.backend.entities.ZonasUocra;
import com.liquidacion.backend.repository.CategoriasZonasUocraRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import com.liquidacion.backend.repository.ZonasUocraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriasZonasUocraService {
    private final CategoriasZonasUocraRepository categoriasZonasUocraRepository;
    private final CategoriaRepository categoriaRepository;
    private final ZonasUocraRepository zonasUocraRepository;

    @Transactional(readOnly = true)
    public List<CategoriaZonaUocraDTO> listarTodos() {
        return categoriasZonasUocraRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaZonaUocraDTO obtenerPorId(Integer id) {
        CategoriasZonasUocra categoriaZona = categoriasZonasUocraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación categoría-zona no encontrada"));
        return toDTO(categoriaZona);
    }

    @Transactional(readOnly = true)
    public CategoriaZonaUocraDTO obtenerPorCategoriaYZona(Integer idCategoria, Integer idZona) {
        CategoriasZonasUocra categoriaZona = categoriasZonasUocraRepository
                .findByCategoria_IdCategoriaAndZona_IdZona(idCategoria, idZona)
                .orElseThrow(() -> new RuntimeException("Relación categoría-zona no encontrada"));
        return toDTO(categoriaZona);
    }

    @Transactional(readOnly = true)
    public List<CategoriaZonaUocraDTO> listarPorZona(Integer idZona) {
        return categoriasZonasUocraRepository.findByZonaIdZona(idZona)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaZonaUocraDTO> listarPorZonaEntity(ZonasUocra zona) {
        return categoriasZonasUocraRepository.findByZona(zona)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaZonaUocraDTO crear(Integer idCategoria, Integer idZona, BigDecimal basico) {
        // Validar que la categoría existe
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Validar que la zona existe
        ZonasUocra zona = zonasUocraRepository.findById(idZona)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        // Validar que no exista ya la relación
        if (categoriasZonasUocraRepository.findByCategoriaAndZona(categoria, zona).isPresent()) {
            throw new RuntimeException("Ya existe una relación para esta categoría y zona");
        }

        CategoriasZonasUocra categoriaZona = new CategoriasZonasUocra();
        categoriaZona.setCategoria(categoria);
        categoriaZona.setZona(zona);
        categoriaZona.setBasico(basico);

        CategoriasZonasUocra guardada = categoriasZonasUocraRepository.save(categoriaZona);
        return toDTO(guardada);
    }

    @Transactional
    public CategoriaZonaUocraDTO actualizar(Integer id, BigDecimal basico) {
        CategoriasZonasUocra categoriaZona = categoriasZonasUocraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación categoría-zona no encontrada"));

        categoriaZona.setBasico(basico);
        CategoriasZonasUocra actualizada = categoriasZonasUocraRepository.save(categoriaZona);
        return toDTO(actualizada);
    }

    @Transactional
    public CategoriaZonaUocraDTO actualizarPorCategoriaYZona(Integer idCategoria, Integer idZona, BigDecimal basico) {
        CategoriasZonasUocra categoriaZona = categoriasZonasUocraRepository
                .findByCategoria_IdCategoriaAndZona_IdZona(idCategoria, idZona)
                .orElseThrow(() -> new RuntimeException("Relación categoría-zona no encontrada"));

        categoriaZona.setBasico(basico);
        CategoriasZonasUocra actualizada = categoriasZonasUocraRepository.save(categoriaZona);
        return toDTO(actualizada);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!categoriasZonasUocraRepository.existsById(id)) {
            throw new RuntimeException("Relación categoría-zona no encontrada");
        }
        categoriasZonasUocraRepository.deleteById(id);
    }

    @Transactional
    public void eliminarPorCategoriaYZona(Integer idCategoria, Integer idZona) {
        CategoriasZonasUocra categoriaZona = categoriasZonasUocraRepository
                .findByCategoria_IdCategoriaAndZona_IdZona(idCategoria, idZona)
                .orElseThrow(() -> new RuntimeException("Relación categoría-zona no encontrada"));
        categoriasZonasUocraRepository.delete(categoriaZona);
    }

    private CategoriaZonaUocraDTO toDTO(CategoriasZonasUocra categoriaZona) {
        CategoriaZonaUocraDTO dto = new CategoriaZonaUocraDTO();
        dto.setIdCategoria(categoriaZona.getCategoria().getIdCategoria());
        dto.setNombreCategoria(categoriaZona.getCategoria().getNombre());
        dto.setId_zona(categoriaZona.getZona().getIdZona());
        dto.setNombreZona(categoriaZona.getZona().getNombre());
        dto.setBasico(categoriaZona.getBasico());
        return dto;
    }
}
