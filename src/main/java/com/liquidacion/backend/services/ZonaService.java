package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ZonaDTO;
import com.liquidacion.backend.mappers.ZonaMapper;
import com.liquidacion.backend.repository.CategoriasZonasUocraRepository;
import com.liquidacion.backend.repository.ZonasUocraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private final ZonasUocraRepository zonaRepo;
    private final CategoriasZonasUocraRepository catZonaRepo;

    public List<ZonaDTO> listarZonasConCategorias() {
        return zonaRepo.findAll()
                .stream()
                .map(zona -> ZonaMapper.toZonaDTO(
                        zona,
                        catZonaRepo.findByZonaIdZona(zona.getIdZona())
                ))
                .collect(Collectors.toList());
    }
}
