package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsuarioDataNascimentoValidatorImpl implements IUsuarioValidator{

    @Override
    public void execute(Usuario usuario) {
        var dataNascimento = usuario.getDataNascimento();

        if (dataNascimento == null)
            throw new BusinessArgumentException("Data de nascimento é obrigatório.");

        if (dataNascimento.isAfter(LocalDate.now()))
            throw new BusinessArgumentException("Data de nascimento inválida.");

        if (!usuario.isMaiorDeIdade())
            throw new BusinessArgumentException("Usuário tem menos de 18 anos.");
    }


}
