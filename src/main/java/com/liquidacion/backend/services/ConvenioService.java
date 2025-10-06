package com.liquidacion.backend.services;

import com.liquidacion.backend.DTO.ConvenioFilaDTO;
import com.liquidacion.backend.entities.Area;
import com.liquidacion.backend.entities.BonificacionAreaLyF;
import com.liquidacion.backend.entities.Categoria;
import com.liquidacion.backend.repository.AreaRepository;
import com.liquidacion.backend.repository.BonificacionAreaLyFRepository;
import com.liquidacion.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConvenioService {
    private final CategoriaRepository categoriaRepo;
    private final BonificacionAreaLyFRepository bonAreaRepo;
    private final AreaRepository areaRepo;

    public List<ConvenioFilaDTO> obtenerConvenios() {
        Categoria categoria11 = categoriaRepo.findById(11)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        BigDecimal basicoReferencia = categoria11.getBasico();

        List<Area> areas = areaRepo.findAll();
        List<Categoria> categorias = categoriaRepo.findAll();
        List<BonificacionAreaLyF> todasBonificaciones = bonAreaRepo.findAll();

        List<ConvenioFilaDTO> convenios = new ArrayList<>();

        for(Categoria cat : categorias) {
            ConvenioFilaDTO fila = new ConvenioFilaDTO();
            fila.setNombreCategoria(cat.getNombre());
            fila.setBasico(cat.getBasico());

            Map<String, BigDecimal> mapaMontos = new HashMap<>();

            for(Area area : areas) {
                //Buscamos bonificación por categoria y área
                Optional<BonificacionAreaLyF> bon = todasBonificaciones.stream()
                        .filter(b->b.getCategoria().getIdCategoria().equals(cat.getIdCategoria()) &&
                                b.getArea().getIdArea().equals(area.getIdArea()))
                        .findFirst();

                if(bon.isPresent()) {
                    BigDecimal porcentaje = bon.get().getPorcentaje();
                    BigDecimal monto = basicoReferencia.multiply(porcentaje).divide(BigDecimal.valueOf(100));
                    mapaMontos.put(area.getNombre(), monto);
                }
            }
            fila.setMontosPorArea(mapaMontos);
            convenios.add(fila);
        }
        return convenios;
    }
}
