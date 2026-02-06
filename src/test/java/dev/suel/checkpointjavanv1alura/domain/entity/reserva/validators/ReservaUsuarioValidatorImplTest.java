package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservaUsuarioValidatorImplTest {

    @Mock
    private Reserva reserva;

    @Mock
    private Usuario usuario;

    private final ReservaUsuarioValidatorImpl validator = new ReservaUsuarioValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoUsuarioForNull() {
        given(reserva.getUsuario()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Usuário inválido ou não foi cadastrado corretamente.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoUsuarioIdForNull() {
        given(reserva.getUsuario()).willReturn(usuario);
        given(usuario.getId()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Usuário inválido ou não foi cadastrado corretamente.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoUsuarioForValido() {
        given(reserva.getUsuario()).willReturn(usuario);
        given(usuario.getId()).willReturn(1L);

        assertDoesNotThrow(() -> validator.execute(reserva));
    }
}
