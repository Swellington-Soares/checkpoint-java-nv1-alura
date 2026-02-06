package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.springframework.stereotype.Component;

@Component
public class ReservaUsuarioValidatorImpl implements IReservaValidator {
    @Override
    public void execute(Reserva reserva) {
        Usuario usuario = reserva.getUsuario();
        if (usuario == null || usuario.getId() == null)
            throw new BusinessArgumentException("Usuário inválido ou não foi cadastrado corretamente.");
    }

}
