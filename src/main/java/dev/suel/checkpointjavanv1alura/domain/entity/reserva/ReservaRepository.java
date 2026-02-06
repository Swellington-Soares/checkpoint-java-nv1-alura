package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

    @Query("""
             SELECT r FROM Reserva r
             WHERE r.sala.id = :salaId
             AND r.dataInicio < :dataFim
             AND r.dataFim > :dataInicio
             AND r.situacao = 'ATIVA'
            """)
    List<Reserva> buscarConflitosPorSala(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("""
             SELECT r FROM Reserva r
             WHERE r.usuario.id = :usuarioId
             AND r.dataInicio < :dataFim
             AND r.dataFim > :dataInicio
             AND r.situacao = 'ATIVA'
            """)
    List<Reserva> buscarConflitosPorUsuario(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("""
             SELECT r FROM Reserva r
             WHERE (r.usuario.id = :usuarioId OR r.sala.id = :salaId)
             AND r.dataInicio < :dataFim
             AND r.dataFim > :dataInicio
             AND r.situacao = 'ATIVA'
            """)
    List<Reserva> buscarConflitosPorSalaOuUsuario(Long salaId, Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim);

}