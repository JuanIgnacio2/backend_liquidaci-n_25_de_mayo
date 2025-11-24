package com.liquidacion.backend.repository;

import com.liquidacion.backend.entities.ConceptosLyF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptosLyFRepository extends JpaRepository<ConceptosLyF, Integer> {
}
