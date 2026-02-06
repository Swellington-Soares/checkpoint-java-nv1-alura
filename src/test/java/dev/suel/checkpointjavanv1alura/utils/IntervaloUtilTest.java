package dev.suel.checkpointjavanv1alura.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IntervaloUtilTest {

    @Test
    void estaDentro_quandoDataNoMeioDoIntervalo_deveRetornarTrue() {
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);
        var data = LocalDateTime.of(2030, 1, 1, 11, 0);

        assertTrue(IntervaloUtil.estaDentro(data, inicio, fim));
    }

    @Test
    void estaDentro_quandoDataIgualAoInicio_deveRetornarTrue() {
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);

        assertTrue(IntervaloUtil.estaDentro(inicio, inicio, fim));
    }

    @Test
    void estaDentro_quandoDataIgualAoFim_deveRetornarTrue() {
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);

        assertTrue(IntervaloUtil.estaDentro(fim, inicio, fim));
    }

    @Test
    void estaDentro_quandoDataAntesDoInicio_deveRetornarFalse() {
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);
        var data = LocalDateTime.of(2030, 1, 1, 9, 59);

        assertFalse(IntervaloUtil.estaDentro(data, inicio, fim));
    }

    @Test
    void estaDentro_quandoDataDepoisDoFim_deveRetornarFalse() {
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);
        var data = LocalDateTime.of(2030, 1, 1, 12, 1);

        assertFalse(IntervaloUtil.estaDentro(data, inicio, fim));
    }

    @Test
    void estaDentro_quandoInicioMaiorQueFim_deveRetornarFalse() {
        var inicio = LocalDateTime.of(2030, 1, 1, 12, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 10, 0);
        var data = LocalDateTime.of(2030, 1, 1, 11, 0);

        assertFalse(IntervaloUtil.estaDentro(data, inicio, fim));
    }
}
