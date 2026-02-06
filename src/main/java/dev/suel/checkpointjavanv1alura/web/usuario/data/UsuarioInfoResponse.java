package dev.suel.checkpointjavanv1alura.web.usuario.data;

public record UsuarioInfoResponse(
        Long id,
        String nome,
        String cpf,
        String dataNascimento,
        String email,
        String telefone
) {
}
