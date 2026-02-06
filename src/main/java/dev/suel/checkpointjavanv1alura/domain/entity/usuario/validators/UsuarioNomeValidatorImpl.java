package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioNomeValidatorImpl implements IUsuarioValidator{
    @Override
    public void execute(Usuario usuario) {
        var nome = usuario.getNome();
        if (nome == null || nome.isEmpty())
            throw new BusinessArgumentException("Nome é obrigatório.");

        if (nome.trim().length() > 20)
            throw new BusinessArgumentException("Nome muito longo. Máximo de 20 caracteres permitidos.");

        if (!nome.trim().matches("[a-zA-Z]+"))
            throw new BusinessArgumentException("Nome inválido, apenas letras são permitidas.");
    }

}
