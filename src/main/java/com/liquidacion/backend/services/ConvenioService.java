package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.*;
import com.liquidacion.backend.entities.*;
import com.liquidacion.backend.mappers.ConvenioMapper;
import com.liquidacion.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConvenioService {
    private final CategoriaRepository categoriaRepo;
    private final EmpleadoRepository empleadoRepo;
    private final BonificacionAreaLyFRepository bonAreaRepo;
    private final BonificacionFijaRepository bonifFijaRepo;
    private final ZonasUocraRepository zonaRepo;
    private final CategoriasZonasUocraRepository catZonaRepo;
    private final GremioRepository gremioRepo;
    private final CategoriasZonasUocraRepository categoriasZonasUocraRepository;

    public List<ConvenioResumenDTO> getResumenConvenios(){
        List<ConvenioResumenDTO> resumenConvenios = new ArrayList<>();

        resumenConvenios.add(new ConvenioResumenDTO(
                "LUZ Y FUERZA",
                empleadoRepo.countByGremioAndEstado(gremioRepo.findByNombre("LUZ_Y_FUERZA"), EstadoEmpleado.ACTIVO),
                categoriaRepo.countByIdCategoriaBetween(1,18),
                "Convenio para empleados de Luz y Fuerza",
                "lyf"
        ));

        resumenConvenios.add((new ConvenioResumenDTO(
                "UOCRA",
                empleadoRepo.countByGremioAndEstado(gremioRepo.findByNombre("UOCRA"), EstadoEmpleado.ACTIVO),
                categoriaRepo.countByIdCategoriaGreaterThan(18),
                "Convenio para empleados de la construcción (UOCRA)",
                "uocra"
        )));

        return resumenConvenios;
    }

    public ConvenioDTO getConvenioLyf() {
        ConvenioDTO dto = new ConvenioDTO();
        dto.setNombreConvenio("LUZ Y FUERZA");

        dto.setCategorias(categoriaRepo.findByIdCategoriaBetween(1, 18)
                .stream()
                .map(ConvenioMapper::toCategoriaDTO)
                .collect(Collectors.toList()));

        dto.setBonificacionesAreas(bonAreaRepo.findAll()
                .stream()
                .map(ConvenioMapper::toBonificacionAreaDTO)
                .collect(Collectors.toList()));

        dto.setBonificacionesFijas(bonifFijaRepo.findAll()
                .stream()
                .map(ConvenioMapper::toBonificacionFijaDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    public ConvenioDTO getConvenioUocra() {
        ConvenioDTO convenioDTO = new ConvenioDTO();
        convenioDTO.setNombreConvenio("UOCRA");

        // Traer todas las zonas de UOCRA
        List<ZonasUocra> zonas = zonaRepo.findAll();

        List<ZonaDTO> zonasDTO = zonas.stream().map(zona -> {
            // Traer las relaciones categoría-zona
            List<CategoriasZonasUocra> categoriaZonaList = catZonaRepo.findByZona(zona);

            List<CategoriaZonaUocraDTO> categoriasDTO = categoriaZonaList.stream().map(cz -> {
                Categoria categoria = cz.getCategoria();

                CategoriaZonaUocraDTO dto = new CategoriaZonaUocraDTO();
                dto.setId(cz.getId());
                dto.setIdCategoria(categoria.getIdCategoria());
                dto.setNombreCategoria(categoria.getNombre());
                dto.setBasico(cz.getBasico());
                return dto;
            }).collect(Collectors.toList());

            ZonaDTO zonaDTO = new ZonaDTO();
            zonaDTO.setIdZona(zona.getIdZona());
            zonaDTO.setNombre(zona.getNombre());
            zonaDTO.setCategorias(categoriasDTO);
            return zonaDTO;
        }).collect(Collectors.toList());

        convenioDTO.setZonas(zonasDTO);
        convenioDTO.setCategorias(Collections.emptyList());
        convenioDTO.setBonificacionesAreas(Collections.emptyList());
        convenioDTO.setBonificacionesFijas(Collections.emptyList());

        return convenioDTO;
    }

    //Actualiza el básico para Luz y Fuerza
    @Transactional
    public void actualizarBasicoLyF(List<ActualizarBasicoDTO> lista){
        for(ActualizarBasicoDTO dto : lista) {
            Categoria entidad = categoriaRepo.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

            entidad.setBasico(dto.getBasico());
            categoriaRepo.save(entidad);
        }
    }

    //Actualiza el básico para UOCRA
    @Transactional
    public void actualizarBasicoUocra(List<ActualizarBasicoDTO> lista){
        for(ActualizarBasicoDTO dto : lista) {
            CategoriasZonasUocra entidad = categoriasZonasUocraRepository
                    .findByCategoria_IdCategoriaAndZona_IdZona(dto.getIdCategoria(), dto.getIdZona())
                    .orElseThrow(() -> new RuntimeException("Relacion categoria-zona no encontrada"));
            entidad.setBasico(dto.getBasico());
            categoriasZonasUocraRepository.save(entidad);
        }
    }
}