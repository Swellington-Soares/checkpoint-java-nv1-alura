package dev.suel.checkpointjavanv1alura.exception;

public class UsuarioNaoExisteException extends ResourceNotFoundException {
    public UsuarioNaoExisteException(Long usuarioId) {
        super("Nenhum usuário com ID " + usuarioId + " foi encontrado.");
    }
}
