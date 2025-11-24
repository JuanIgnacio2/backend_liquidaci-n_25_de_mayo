package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ConceptosLyFDTO;
import com.liquidacion.backend.entities.ConceptosLyF;
import com.liquidacion.backend.repository.ConceptosLyFRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConceptosLyFService {
    private final ConceptosLyFRepository ConcepLyfRepo;

    @Transactional(readOnly = true)
    public List<ConceptosLyFDTO> listar() {
        return ConcepLyfRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConceptosLyFDTO buscar(Integer id) {
        return toDTO(ConcepLyfRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificación fija no encontrada")));
    }

    public ConceptosLyFDTO crear(ConceptosLyFDTO bonFijaDTO) {
        ConceptosLyF bonFija = new ConceptosLyF();
        bonFija.setNombre(bonFijaDTO.getNombre());
        bonFija.setPorcentaje(bonFijaDTO.getPorcentaje());
        return toDTO(ConcepLyfRepo.save(bonFija));
    }

    public ConceptosLyFDTO actualizar(Integer id, ConceptosLyFDTO bonFijaDTO) {
        ConceptosLyF bonFija = ConcepLyfRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bonificación fija no encontrada"));
        bonFija.setNombre(bonFijaDTO.getNombre());
        bonFija.setPorcentaje(bonFijaDTO.getPorcentaje());
        return toDTO(ConcepLyfRepo.save(bonFija));
    }

    public void eliminar(Integer id) {
        ConcepLyfRepo.deleteById(id);
    }

    private ConceptosLyFDTO toDTO(ConceptosLyF bonFija) {
        return new ConceptosLyFDTO(
                bonFija.getIdConceptosLyf(),
                bonFija.getNombre(),
                bonFija.getPorcentaje()
        );
    }
}
