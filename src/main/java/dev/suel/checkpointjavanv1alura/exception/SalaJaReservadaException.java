package dev.suel.checkpointjavanv1alura.exception;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;

public class SalaJaReservadaException extends RuntimeException {
    public SalaJaReservadaException() {
        super("Já existe uma reserva para o período informado");
    }
}
