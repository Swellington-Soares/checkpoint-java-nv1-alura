package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UsuarioSobrenomeValidatorImplTest {
    @Mock
    private Usuario usuario;

    private final UsuarioSobrenomeValidatorImpl validator = new UsuarioSobrenomeValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoSobrenomeForNull() {
        given(usuario.getSobrenome()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Sobrenome é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoSobrenomeForVazio() {
        given(usuario.getSobrenome()).willReturn("");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Sobrenome é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoSobrenomeForMaiorQue20() {
        given(usuario.getSobrenome()).willReturn("123456789012345678901");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Sobrenome muito longo. Máximo de 20 caracteres permitidos.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoSobrenomeForApenasLetras() {
        given(usuario.getSobrenome()).willReturn("Silva");
        assertDoesNotThrow(() -> validator.execute(usuario));
    }


    @Test
    void execute_naoDeveLancarExcecao_quandoSobrenomeTiverExatamente20Caracteres() {
        given(usuario.getSobrenome()).willReturn("SantosSilvaSantosSil");

        assertDoesNotThrow(() -> validator.execute(usuario));
    }
}