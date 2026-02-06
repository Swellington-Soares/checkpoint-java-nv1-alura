package dev.suel.checkpointjavanv1alura.web.reserva.data;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;

import java.util.UUID;

public record ReservaInfoResponse(
        UUID id,
        UsuarioInfo usuario,
        SalaInfo sala,
        String dataInicio,
        String dataFim,
        SituacaoReserva situacao
) {
    public record UsuarioInfo(
            String cpf,
            String nome,
            String email,
            String telefone
    ) {
    }

    public record SalaInfo(
            Long id,
            String nome,
            int capacidade
    ) {}
}
