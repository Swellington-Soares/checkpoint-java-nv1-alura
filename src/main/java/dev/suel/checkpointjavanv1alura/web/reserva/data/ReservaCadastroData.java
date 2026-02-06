package dev.suel.checkpointjavanv1alura.web.reserva.data;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaCadastroData(

        @NotNull(message = "Id da Sala não pode ser nulo.")
        Long sala,

        @NotNull(message = "Id do usuário não pode ser nulo.")
        Long usuario,

        @NotNull(message = "A data de início é obrigatória.")
        @FutureOrPresent(message = "Data de início inválida.")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        @Future(message = "Data de fim inválida.")
        LocalDateTime dataFim
) {
}
