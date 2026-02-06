package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.PhoneNumberValidator;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioTelefoneValidatorImpl implements IUsuarioValidator {
    @Override
    public void execute(Usuario usuario) {
        var telefone =  usuario.getTelefone();

        if (telefone == null || telefone.isEmpty())
            throw new BusinessArgumentException("Telefone é obrigatório.");

        PhoneNumberValidator.validateOrThrow(telefone);
    }


}
