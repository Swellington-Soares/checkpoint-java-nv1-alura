package dev.suel.checkpointjavanv1alura.domain.entity.sala;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}