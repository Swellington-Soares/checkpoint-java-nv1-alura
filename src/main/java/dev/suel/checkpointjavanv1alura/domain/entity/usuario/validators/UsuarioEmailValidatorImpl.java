package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.EmailValidator;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEmailValidatorImpl implements IUsuarioValidator {
    @Override
    public void execute(Usuario usuario) {
        var email = usuario.getEmail();

        if (email == null || email.isBlank())
            throw new BusinessArgumentException("E-mail é obrigatório.");

        EmailValidator.validateOrThrow(email);
    }


}
