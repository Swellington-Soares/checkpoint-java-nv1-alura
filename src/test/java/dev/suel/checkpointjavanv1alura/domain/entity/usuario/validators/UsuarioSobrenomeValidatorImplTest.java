package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

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
    void execute_deveLancarExcecao_quandoSobrenomeForApenasLetras() {
        given(usuario.getSobrenome()).willReturn("Silva");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Sobrenome inválido, apenas letras são permitidas.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoSobrenomeNaoForApenasLetras_eEstiverNoLimite() {
        given(usuario.getSobrenome()).willReturn("Silva1");

        assertDoesNotThrow(() -> validator.execute(usuario));
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoSobrenomeTiverExatamente20Caracteres() {
        given(usuario.getSobrenome()).willReturn("12345678901234567890");

        assertDoesNotThrow(() -> validator.execute(usuario));
    }
}