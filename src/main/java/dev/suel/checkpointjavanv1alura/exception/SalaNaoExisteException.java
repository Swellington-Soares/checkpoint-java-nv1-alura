package dev.suel.checkpointjavanv1alura.exception;

public class SalaNaoExisteException extends BusinessArgumentException {
    public SalaNaoExisteException(Long salaId) {
        super("Sala com ID " + salaId + " não encontrada.");
    }
}
