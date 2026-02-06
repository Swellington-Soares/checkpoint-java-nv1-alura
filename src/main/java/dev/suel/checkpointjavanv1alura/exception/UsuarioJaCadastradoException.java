package dev.suel.checkpointjavanv1alura.exception;

public class UsuarioJaCadastradoException extends RuntimeException {
    public UsuarioJaCadastradoException() {
        super("Já existe um usuário cadastrado com o mesmo e-mail ou CPF");
    }
}
