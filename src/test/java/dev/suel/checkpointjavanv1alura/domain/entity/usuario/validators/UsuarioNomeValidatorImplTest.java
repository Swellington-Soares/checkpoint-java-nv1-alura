package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.BusinessArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UsuarioNomeValidatorImplTest {
    @Mock
    private Usuario usuario;

    private final UsuarioNomeValidatorImpl validator = new UsuarioNomeValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoNomeForNull() {
        given(usuario.getNome()).willReturn(null);

        var ex = assertThrows(BusinessArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Nome é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoNomeForVazio() {
        given(usuario.getNome()).willReturn("");

        var ex = assertThrows(BusinessArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Nome é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoNomeTrimForMaiorQue20() {
        given(usuario.getNome()).willReturn("SwellingtonSoaresABCACVF");

        var ex = assertThrows(BusinessArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Nome muito longo. Máximo de 20 caracteres permitidos.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoNomeForApenasLetras() {
        given(usuario.getNome()).willReturn("Joao");
        assertDoesNotThrow(() -> validator.execute(usuario));
    }

    @Test
    void execute_deveLancarExcecao_quandoNomeNaoForApenasLetras_eEstiverNoLimite() {
        given(usuario.getNome()).willReturn("Joao1");
        assertThrows(BusinessArgumentException.class, () -> validator.execute(usuario));
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoNomeTrimTiverExatamente20Caracteres() {
        given(usuario.getNome()).willReturn("SwellingtonSoaresABC");
        assertDoesNotThrow(() -> validator.execute(usuario));
    }
}