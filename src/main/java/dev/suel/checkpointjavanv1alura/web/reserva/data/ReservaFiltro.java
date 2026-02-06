package dev.suel.checkpointjavanv1alura.web.reserva.data;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;

import java.time.LocalDateTime;

public record ReservaFiltro(
        SituacaoReserva situacao,
        Long usuarioId,
        Long salaId,
        LocalDateTime inicioDe,
        LocalDateTime inicioAte
) {
}
