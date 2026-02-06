package dev.suel.checkpointjavanv1alura.domain.entity.sala.validators;


import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import org.springframework.stereotype.Component;

@Component
public class SalaNomeValidatorImpl implements ISalaValidator {
    @Override
    public void execute(Sala sala) {
        var nome =  sala.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome deve ser informado.");
        }
    }
}
