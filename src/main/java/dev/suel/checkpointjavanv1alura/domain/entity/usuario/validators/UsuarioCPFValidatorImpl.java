package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.CPFValidator;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioCPFValidatorImpl implements  IUsuarioValidator {
    @Override
    public void execute(Usuario usuario) {
        var cpf = usuario.getCpf();

        if (cpf == null || cpf.isEmpty())
            throw new BusinessArgumentException("CPF é obrigatório.");

        CPFValidator.validateOrThrow(cpf);
    }


}
