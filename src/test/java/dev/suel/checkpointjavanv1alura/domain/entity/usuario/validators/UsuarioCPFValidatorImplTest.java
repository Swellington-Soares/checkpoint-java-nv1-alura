package dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.validators.CPFValidator;
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
class UsuarioCPFValidatorImplTest {

    @Mock
    private Usuario usuario;

    private final UsuarioCPFValidatorImpl validator = new UsuarioCPFValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoCpfForNull() {
        given(usuario.getCpf()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("CPF é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoCpfForVazio() {
        given(usuario.getCpf()).willReturn("");

        var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

        assertEquals("CPF é obrigatório.", ex.getMessage());
    }

    @Test
    void execute_deveChamarCPFValidator_quandoCpfForInformado() {
        given(usuario.getCpf()).willReturn("12345678909");

        try (MockedStatic<CPFValidator> mocked = mockStatic(CPFValidator.class)) {
            validator.execute(usuario);

            mocked.verify(() -> CPFValidator.validateOrThrow("12345678909"));
        }
    }

    @Test
    void execute_devePropagarExcecao_quandoCPFValidatorLancarErro() {
        given(usuario.getCpf()).willReturn("cpf_invalido");

        try (MockedStatic<CPFValidator> mocked = mockStatic(CPFValidator.class)) {
            mocked.when(() -> CPFValidator.validateOrThrow("cpf_invalido"))
                    .thenThrow(new IllegalArgumentException("CPF inválido."));

            var ex = assertThrows(IllegalArgumentException.class, () -> validator.execute(usuario));

            assertEquals("CPF inválido.", ex.getMessage());
        }
    }

}