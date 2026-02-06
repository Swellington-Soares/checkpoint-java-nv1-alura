package dev.suel.checkpointjavanv1alura.exception;

import java.util.UUID;

public class ReservaNaoExisteException extends ResourceNotFoundException {
    public ReservaNaoExisteException(UUID id) {
        super("Reserva com ID " + id + " não encontrada.");
    }
}
