package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.PhoneNumberValidator;
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
class UsuarioTelefoneValidatorImplTest {
    @Mock
    private Usuario usuario;

    private final UsuarioTelefoneValidatorImpl validator = new UsuarioTelefoneValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoTelefoneForNull() {
        given(usuario.getTelefone()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Telefone é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoTelefoneForVazio() {
        given(usuario.getTelefone()).willReturn("");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("Telefone é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveChamarPhoneNumberValidator_quandoTelefoneForInformado() {
        given(usuario.getTelefone()).willReturn("11999998888");

        try (MockedStatic<PhoneNumberValidator> mocked = mockStatic(PhoneNumberValidator.class)) {
            validator.execute(usuario);

            mocked.verify(() -> PhoneNumberValidator.validateOrThrow("11999998888"));
        }
    }

    @Test
    void execute_devePropagarExcecao_quandoPhoneNumberValidatorLancarErro() {
        given(usuario.getTelefone()).willReturn("telefone_invalido");

        try (MockedStatic<PhoneNumberValidator> mocked = mockStatic(PhoneNumberValidator.class)) {
            mocked.when(() -> PhoneNumberValidator.validateOrThrow("telefone_invalido"))
                    .thenThrow(new IllegalArgumentException("Telefone inválido."));

            var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

            assertEquals("Telefone inválido.", ex.getMessage());
        }
    }
}