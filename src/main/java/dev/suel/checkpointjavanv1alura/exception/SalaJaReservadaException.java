package dev.suel.checkpointjavanv1alura.exception;

public class SalaJaReservadaException extends BusinessArgumentException {
    public SalaJaReservadaException() {
        super("Já existe uma reserva para o período informado");
    }
}
