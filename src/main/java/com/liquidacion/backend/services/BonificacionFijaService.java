package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.BonificacionFijaDTO;
import com.liquidacion.backend.entities.BonificacionFija;
import com.liquidacion.backend.repository.BonificacionFijaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BonificacionFijaService {
    private final BonificacionFijaRepository bonFijaRepo;

    @Transactional(readOnly = true)
    public List<BonificacionFijaDTO> listar() {
        return bonFijaRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BonificacionFijaDTO buscar(Integer id) {
        return toDTO(bonFijaRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Bonificación fija no encontrada")));
    }

    public BonificacionFijaDTO crear(BonificacionFijaDTO bonFijaDTO) {
        BonificacionFija bonFija = new BonificacionFija();
        bonFija.setNombre(bonFijaDTO.getNombre());
        bonFija.setPorcentaje(bonFijaDTO.getPorcentaje());
        return toDTO(bonFijaRepo.save(bonFija));
    }

    public BonificacionFijaDTO actualizar(Integer id, BonificacionFijaDTO bonFijaDTO) {
        BonificacionFija bonFija = bonFijaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bonificación fija no encontrada"));
        bonFija.setNombre(bonFijaDTO.getNombre());
        bonFija.setPorcentaje(bonFijaDTO.getPorcentaje());
        return toDTO(bonFijaRepo.save(bonFija));
    }

    public void eliminar(Integer id) {
        bonFijaRepo.deleteById(id);
    }

    private BonificacionFijaDTO toDTO(BonificacionFija bonFija) {
        return new BonificacionFijaDTO(
                bonFija.getIdBonificacionFija(),
                bonFija.getNombre(),
                bonFija.getPorcentaje()
        );
    }
}
