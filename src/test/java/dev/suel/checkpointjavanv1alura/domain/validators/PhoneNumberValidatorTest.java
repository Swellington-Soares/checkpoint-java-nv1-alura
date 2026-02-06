package dev.suel.checkpointjavanv1alura.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberValidatorTest {

    @Test
    void isValid_quandoPhoneNulo_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid(null));
    }

    @Test
    void isValid_quandoPhoneVazio_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid(""));
    }

    @Test
    void isValid_quandoPhoneApenasEspacos_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("   "));
    }

    @Test
    void isValid_quandoPhoneValidoMinimo_deveRetornarTrue() {
        assertTrue(PhoneNumberValidator.isValid("+558899999999"));
    }

    @Test
    void isValid_quandoPhoneValidoMaximo_deveRetornarTrue() {
        assertTrue(PhoneNumberValidator.isValid("+5588999999999"));
    }

    @Test
    void isValid_quandoPhoneValidoBrasil_deveRetornarTrue() {
        assertTrue(PhoneNumberValidator.isValid("+5511999999999"));
    }

    @Test
    void isValid_quandoPhoneComEspacosNasPontas_deveRetornarTrue() {
        assertTrue(PhoneNumberValidator.isValid("  +5511999999999  "));
    }

    @Test
    void isValid_quandoNaoComecaComMais_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("5511999999999"));
    }

    @Test
    void isValid_quandoComecaComZeroDepoisDoMais_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+0123456789"));
    }

    @Test
    void isValid_quandoCurtoDemais_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+1234567"));
    }

    @Test
    void isValid_quandoLongoDemais_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+1234567890123456"));
    }

    @Test
    void isValid_quandoContemLetras_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+55ABC99999999"));
    }

    @Test
    void isValid_quandoContemEspacoNoMeio_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+55 11999999999"));
    }

    @Test
    void isValid_quandoContemHifenOuParenteses_deveRetornarFalse() {
        assertFalse(PhoneNumberValidator.isValid("+55(11)999999999"));
        assertFalse(PhoneNumberValidator.isValid("+55-11999999999"));
    }

    @Test
    void validateOrThrow_quandoPhoneValido_naoDeveLancar() {
        assertDoesNotThrow(() -> PhoneNumberValidator.validateOrThrow("+5511999999999"));
    }

    @Test
    void validateOrThrow_quandoPhoneInvalido_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> PhoneNumberValidator.validateOrThrow("81999999999")
        );

        assertEquals(
                "Número de telefone inválido. Use o formato internacional, exemplo: +5511999999999",
                ex.getMessage()
        );
    }

    @Test
    void validateOrThrow_quandoPhoneNulo_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> PhoneNumberValidator.validateOrThrow(null)
        );

        assertEquals(
                "Número de telefone inválido. Use o formato internacional, exemplo: +5511999999999",
                ex.getMessage()
        );
    }
}
