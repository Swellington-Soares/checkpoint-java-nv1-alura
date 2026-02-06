package dev.suel.checkpointjavanv1alura.web.sala.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SalaCadastroData(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Positive(message = "Apenas valores positivos são permitidos")
        @Min(value = 1, message = "O mínimo é 1.")
        int capacidade
) {
}
