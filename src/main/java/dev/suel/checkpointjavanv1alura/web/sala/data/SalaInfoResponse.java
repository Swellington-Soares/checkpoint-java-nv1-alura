package dev.suel.checkpointjavanv1alura.web.sala.data;

public record SalaInfoResponse(
        Long id,
        String nome,
        String dataCadastro,
        int capacidade,
        String reservada
) {
}
