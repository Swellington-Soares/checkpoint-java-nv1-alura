package dev.suel.checkpointjavanv1alura.domain.entity.sala.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class SalaCapacidadeValidatorImpl implements ISalaValidator {
    @Override
    public void execute(Sala sala) {
        var capacidade = sala.getCapacidade();
        if (capacidade <= 0)
            throw new BusinessArgumentException("Capacidade deve ser positivo e maior que 0.");
    }
}
