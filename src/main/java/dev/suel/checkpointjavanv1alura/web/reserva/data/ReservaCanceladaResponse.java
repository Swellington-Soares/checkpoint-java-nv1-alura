package dev.suel.checkpointjavanv1alura.web.reserva.data;

import java.util.UUID;

public record ReservaCanceladaResponse(
        UUID id,
        String dataCancelamento,
        String motivoCancelamento
) {
}
