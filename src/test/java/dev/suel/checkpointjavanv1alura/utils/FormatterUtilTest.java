package dev.suel.checkpointjavanv1alura.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatterUtilTest {

    @Test
    void formatarDataHora_deveFormatarCorretamente() {
        var data = LocalDateTime.of(2030, 1, 1, 10, 30);

        var result = FormatterUtil.formatarDataHora(data);

        assertEquals("01/01/2030 10:30", result);
    }

    @Test
    void formatarDataNascimento_deveFormatarCorretamente() {
        var data = LocalDate.of(2000, 5, 20);

        var result = FormatterUtil.formatarDataNascimento(data);

        assertEquals("20/05/2000", result);
    }

    @Test
    void formatarCPF_quandoCpfValido_deveFormatarComMascara() {
        var result = FormatterUtil.formatarCPF("52998224725");

        assertEquals("529.982.247-25", result);
    }

    @Test
    void formatarCPF_quandoCpfComMascara_deveRemoverEFormatarNovamente() {
        var result = FormatterUtil.formatarCPF("529.982.247-25");

        assertEquals("529.982.247-25", result);
    }

    @Test
    void formatarCPF_quandoCpfNulo_deveLancar() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> FormatterUtil.formatarCPF(null)
        );

        assertEquals("CPF não pode ser nulo.", ex.getMessage());
    }

    @Test
    void formatarCPF_quandoCpfComTamanhoInvalido_deveLancar() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> FormatterUtil.formatarCPF("123")
        );

        assertEquals("CPF deve conter 11 dígitos.", ex.getMessage());
    }

    @Test
    void formatarNumeroTelefone_quandoNulo_deveLancar() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> FormatterUtil.formatarNumeroTelefone(null)
        );

        assertEquals("Telefone não pode ser null.", ex.getMessage());
    }


    @Test
    void formatarNumeroTelefone_quandoContemCaracteresNaoNumericos_deveLimparEFormatar() {
        var result = FormatterUtil.formatarNumeroTelefone("+55 (81) 99999-9999");

        assertEquals("+55 (81) 9 9999 9999", result);
    }

    @Test
    void formatarNumeroTelefone_quandoQuantidadeDigitosInvalida_deveLancar() {
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> FormatterUtil.formatarNumeroTelefone("1234567")
        );

        assertEquals("Telefone deve ter 12 e 14 dígitos.", ex.getMessage());
    }
}
