package dev.suel.checkpointjavanv1alura.web.usuario.data;

import java.time.LocalDate;

public record UsuarioUpdateData(
        String cpf,
        String nome,
        String sobrenome,
        LocalDate dataNascimento,
        String telefone
) {
}
