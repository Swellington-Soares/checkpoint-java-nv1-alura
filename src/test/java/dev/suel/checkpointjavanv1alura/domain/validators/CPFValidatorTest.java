package dev.suel.checkpointjavanv1alura.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFValidatorTest {

    @Test
    void isValid_quandoCpfNulo_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid(null));
    }

    @Test
    void isValid_quandoCpfVazio_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid(""));
    }

    @Test
    void isValid_quandoCpfComMenosDe11Digitos_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid("1234567890"));
    }

    @Test
    void isValid_quandoCpfComMaisDe11Digitos_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid("123456789012"));
    }

    @Test
    void isValid_quandoCpfComTodosDigitosIguais_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid("00000000000"));
        assertFalse(CPFValidator.isValid("11111111111"));
        assertFalse(CPFValidator.isValid("99999999999"));
    }

    @Test
    void isValid_quandoCpfValido_deveRetornarTrue() {
        assertTrue(CPFValidator.isValid("52998224725"));
        assertTrue(CPFValidator.isValid("16899535009"));
    }

    @Test
    void isValid_quandoCpfValidoComMascara_deveRetornarTrue() {
        assertTrue(CPFValidator.isValid("529.982.247-25"));
    }

    @Test
    void isValid_quandoCpfInvalidoComDigitosVerificadoresErrados_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid("52998224724"));
        assertFalse(CPFValidator.isValid("16899535000"));
    }

    @Test
    void isValid_quandoCpfContemLetras_deveRetornarFalse() {
        assertFalse(CPFValidator.isValid("52998ABC725"));
    }

    @Test
    void validateOrThrow_quandoCpfValido_naoDeveLancar() {
        assertDoesNotThrow(() -> CPFValidator.validateOrThrow("52998224725"));
    }

    @Test
    void validateOrThrow_quandoCpfInvalido_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> CPFValidator.validateOrThrow("12345678900")
        );

        assertEquals("CPF inválido.", ex.getMessage());
    }

    @Test
    void validateOrThrow_quandoCpfNulo_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> CPFValidator.validateOrThrow(null)
        );

        assertEquals("CPF inválido.", ex.getMessage());
    }
}
