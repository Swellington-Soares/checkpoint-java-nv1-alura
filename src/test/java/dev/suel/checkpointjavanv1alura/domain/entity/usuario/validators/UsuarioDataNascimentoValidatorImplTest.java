package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

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
class UsuarioDataNascimentoValidatorImplTest {
    @Mock
    private Usuario usuario;

    private final UsuarioDataNascimentoValidatorImpl validator = new UsuarioDataNascimentoValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoDataNascimentoForNull() {
        given(usuario.getDataNascimento()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Data de nascimento é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoDataNascimentoForNoFuturo() {
        var today = LocalDate.of(2026, 2, 5);
        given(usuario.getDataNascimento()).willReturn(LocalDate.of(2026, 2, 6));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(today);

            var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

            assertEquals("Data de nascimento inválida.", ex.getMessage());
        }
    }

    @Test
    void execute_deveLancarExcecao_quandoUsuarioTiverMenosDe18Anos() {
        var today = LocalDate.of(2026, 2, 5);
        given(usuario.getDataNascimento()).willReturn(LocalDate.of(2010, 1, 1));

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(today);

            var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

            assertEquals("Usuário tem menos de 18 anos.", ex.getMessage());
        }
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoUsuarioTiverExatamente18AnosPeloAno() {
        given(usuario.getDataNascimento()).willReturn(LocalDate.of(2008, 1, 1));
        given(usuario.isMaiorDeIdade()).willReturn(true);
        assertDoesNotThrow(() -> validator.execute(usuario));
    }

}