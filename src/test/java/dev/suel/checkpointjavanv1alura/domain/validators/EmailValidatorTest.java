package dev.suel.checkpointjavanv1alura.domain.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void isValid_quandoEmailNulo_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid(null));
    }

    @Test
    void isValid_quandoEmailVazio_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid(""));
    }

    @Test
    void isValid_quandoEmailApenasEspacos_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("   "));
    }

    @Test
    void isValid_quandoEmailValidoSimples_deveRetornarTrue() {
        assertTrue(EmailValidator.isValid("suel@email.com"));
    }

    @Test
    void isValid_quandoEmailValidoComPlusUnderscorePontoETraco_deveRetornarTrue() {
        assertTrue(EmailValidator.isValid("nome.sobrenome+tag_1-email@sub.dominio.com"));
    }

    @Test
    void isValid_quandoEmailValidoComEspacosNasPontas_deveRetornarTrue() {
        assertTrue(EmailValidator.isValid("  suel@email.com  "));
    }

    @Test
    void isValid_quandoSemArroba_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suelemail.com"));
    }

    @Test
    void isValid_quandoSemUsuario_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("@email.com"));
    }

    @Test
    void isValid_quandoSemDominio_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suel@"));
    }

    @Test
    void isValid_quandoSemTld_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suel@email"));
    }

    @Test
    void isValid_quandoTldComUmCaractere_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suel@email.c"));
    }

    @Test
    void isValid_quandoPossuiEspacoNoMeio_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suel@em ail.com"));
        assertFalse(EmailValidator.isValid("su el@email.com"));
    }

    @Test
    void isValid_quandoPossuiDoisArrobas_deveRetornarFalse() {
        assertFalse(EmailValidator.isValid("suel@@email.com"));
        assertFalse(EmailValidator.isValid("suel@email@com"));
    }

    @Test
    void validateOrThrow_quandoEmailValido_naoDeveLancar() {
        assertDoesNotThrow(() -> EmailValidator.validateOrThrow("suel@email.com"));
    }

    @Test
    void validateOrThrow_quandoEmailInvalido_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> EmailValidator.validateOrThrow("suel@email")
        );

        assertEquals("E-mail inválido.", ex.getMessage());
    }

    @Test
    void validateOrThrow_quandoEmailNulo_deveLancarIllegalArgumentException() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> EmailValidator.validateOrThrow(null)
        );

        assertEquals("E-mail inválido.", ex.getMessage());
    }
}
