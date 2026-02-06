package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSobrenomeValidatorImpl implements IUsuarioValidator{
    @Override
    public void execute(Usuario usuario) {
        var sobrenome = usuario.getSobrenome();
        if (sobrenome == null || sobrenome.isEmpty())
            throw new BusinessArgumentException("Sobrenome é obrigatório.");

        if (sobrenome.length() > 20)
            throw new BusinessArgumentException("Sobrenome muito longo. Máximo de 20 caracteres permitidos.");

        if (!sobrenome.matches("[a-zA-Z]+"))
            throw new BusinessArgumentException("Sobrenome inválido, apenas letras são permitidas.");
    }


}
