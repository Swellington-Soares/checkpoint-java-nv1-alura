package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class ReservaSalaValidatorImpl implements IReservaValidator {
    @Override
    public void execute(Reserva reserva) {
        Sala sala = reserva.getSala();
        if (sala == null || sala.getId() == null)
            throw new BusinessArgumentException("Sala inválida ou não foi cadastrada corretamente.");
    }


}
