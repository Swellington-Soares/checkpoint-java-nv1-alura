package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservaDataValidatorImpl implements  IReservaValidator {
    @Override
    public void execute(Reserva reserva) {
        var dataInicio = reserva.getDataInicio();
        var dataFim = reserva.getDataFim();

        if (dataInicio == null || dataInicio.isBefore(LocalDateTime.now()))
            throw new BusinessArgumentException("Data de início inválida.");

        if (dataFim == null || dataFim.isBefore(LocalDateTime.now()))
            throw  new BusinessArgumentException("Data de fim inválida.");

        if (dataInicio.isAfter(dataFim))
            throw new BusinessArgumentException("Data início não pode ser depois do fim.");

    }

}
