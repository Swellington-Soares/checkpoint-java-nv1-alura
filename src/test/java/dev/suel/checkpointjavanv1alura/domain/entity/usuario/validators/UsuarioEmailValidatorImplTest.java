package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;


import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class UsuarioEmailValidatorImplTest {
    @Mock
    private Usuario usuario;

    private final UsuarioEmailValidatorImpl validator = new UsuarioEmailValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoEmailForNull() {
        given(usuario.getEmail()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("E-mail é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoEmailForBlank() {
        given(usuario.getEmail()).willReturn("   ");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("E-mail é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveChamarEmailValidator_quandoEmailForInformado() {
        given(usuario.getEmail()).willReturn("teste@dominio.com");

        try (MockedStatic<EmailValidator> mocked = mockStatic(EmailValidator.class)) {
            validator.execute(usuario);

            mocked.verify(() -> EmailValidator.validateOrThrow("teste@dominio.com"));
        }
    }

    @Test
    void execute_devePropagarExcecao_quandoEmailValidatorLancarErro() {
        given(usuario.getEmail()).willReturn("email_invalido");

        try (MockedStatic<EmailValidator> mocked = mockStatic(EmailValidator.class)) {
            mocked.when(() -> EmailValidator.validateOrThrow("email_invalido"))
                    .thenThrow(new IllegalArgumentException("E-mail inválido."));

            var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

            assertEquals("E-mail inválido.", ex.getMessage());
        }
    }
}