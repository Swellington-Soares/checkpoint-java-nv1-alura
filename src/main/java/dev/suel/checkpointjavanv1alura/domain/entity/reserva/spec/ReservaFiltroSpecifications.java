package dev.suel.checkpointjavanv1alura.domain.entity.reserva.spec;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ReservaFiltroSpecifications {
    private ReservaFiltroSpecifications() {
    }

    public static Specification<Reserva> comSituacao(SituacaoReserva situacao) {
        return (root, query, cb) -> situacao == null ? null : cb.equal(root.get("situacao"), situacao);
    }

    public static Specification<Reserva> comUsuarioId(Long usuarioId) {
        return (root, query, cb) -> usuarioId == null ? null : cb.equal(root.get("usuario").get("id"), usuarioId);
    }

    public static Specification<Reserva> comSalaId(Long salaId) {
        return (root, query, cb) -> salaId == null ? null : cb.equal(root.get("sala").get("id"), salaId);
    }

    public static Specification<Reserva> inicioEntre(LocalDateTime de, LocalDateTime ate) {
        return (root, query, cb) -> {
            if (de == null && ate == null) return null;
            if (de != null && ate != null) return cb.between(root.get("dataInicio"), de, ate);
            if (de != null) return cb.greaterThanOrEqualTo(root.get("dataInicio"), de);
            return cb.lessThanOrEqualTo(root.get("dataInicio"), ate);
        };
    }
}
