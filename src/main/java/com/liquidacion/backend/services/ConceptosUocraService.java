package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ConceptosLyFDTO;
import com.liquidacion.backend.DTO.ConceptosUocraCreateDTO;
import com.liquidacion.backend.DTO.ConceptosUocraDTO;
import com.liquidacion.backend.entities.ConceptosLyF;
import com.liquidacion.backend.entities.ConceptosUocra;
import com.liquidacion.backend.repository.ConceptosUocraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptosUocraService {
    private final ConceptosUocraRepository concepUocraRepository;

    @Transactional(readOnly = true)
    public List<ConceptosUocraDTO> listar() {
        return concepUocraRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConceptosUocraDTO buscar(Integer id) {
        return toDTO(concepUocraRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificación fija no encontrada")));
    }

    public ConceptosUocraDTO crear(ConceptosUocraDTO conUocra) {
        ConceptosUocra conceptoUocra = new ConceptosUocra();
        conceptoUocra.setNombre(conUocra.getNombre());
        conceptoUocra.setPorcentaje(conUocra.getPorcentaje());
        return toDTO(concepUocraRepository.save(conceptoUocra));
    }

    public ConceptosUocraDTO actualizar(Integer id, ConceptosUocraDTO conUocra) {
        ConceptosUocra conceptoUocra = concepUocraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bonificación fija no encontrada"));
        conceptoUocra.setNombre(conUocra.getNombre());
        conceptoUocra.setPorcentaje(conUocra.getPorcentaje());
        return toDTO(concepUocraRepository.save(conceptoUocra));
    }

    public void eliminar(Integer id) {
        concepUocraRepository.deleteById(id);
    }

    private ConceptosUocraDTO toDTO(ConceptosUocra conUocra) {
        return new ConceptosUocraDTO(
                conUocra.getIdConceptosUocra(),
                conUocra.getNombre(),
                conUocra.getPorcentaje()
        );
    }
}
