package dev.suel.checkpointjavanv1alura.web.reserva.data;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaCadastroData(

        @NotNull(message = "Id da Sala não pode ser nulo.")
        Long sala,

        @NotNull(message = "Id do usuário não pode ser nulo.")
        Long usuario,

        @FutureOrPresent(message = "Data de início inválida.")
        LocalDateTime dataInicio,

        @FutureOrPresent(message = "Data de fim inválida.")
        LocalDateTime dataFim
) {
}
