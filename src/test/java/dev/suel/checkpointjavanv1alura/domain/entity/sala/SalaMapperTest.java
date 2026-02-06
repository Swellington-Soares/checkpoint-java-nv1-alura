package dev.suel.checkpointjavanv1alura.domain.entity.sala;

import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SalaMapperTest {

    private final SalaMapper mapper = new SalaMapper();

    @Test
    void toInfoResponse_deveMapearCamposEReservadaNao() {
        var dataRegistro = LocalDateTime.of(2030, 1, 1, 10, 0);

        var sala = mock(Sala.class);
        given(sala.getId()).willReturn(1L);
        given(sala.getNome()).willReturn("Sala 1");
        given(sala.getCapacidade()).willReturn(20);
        given(sala.getDataRegistro()).willReturn(dataRegistro);
        given(sala.isReservada()).willReturn(false);

        var result = mapper.toInfoResponse(sala);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Sala 1", result.nome());
        assertEquals(20, result.capacidade());
        assertEquals("Não", result.reservada());
        assertEquals(FormatterUtil.formatarDataHora(dataRegistro), result.dataCadastro());
    }

    @Test
    void toInfoResponse_deveMapearReservadaSimQuandoSalaReservada() {
        var dataRegistro = LocalDateTime.of(2030, 2, 1, 9, 0);

        var sala = mock(Sala.class);
        given(sala.getId()).willReturn(2L);
        given(sala.getNome()).willReturn("Sala 2");
        given(sala.getCapacidade()).willReturn(10);
        given(sala.getDataRegistro()).willReturn(dataRegistro);
        given(sala.isReservada()).willReturn(true);

        var result = mapper.toInfoResponse(sala);

        assertNotNull(result);
        assertEquals(2L, result.id());
        assertEquals("Sala 2", result.nome());
        assertEquals(10, result.capacidade());
        assertEquals("Sim", result.reservada());
        assertEquals(FormatterUtil.formatarDataHora(dataRegistro), result.dataCadastro());
    }

    @Test
    void toInfoResponse_quandoDataRegistroNula_deveLancarOuRetornarNuloDeAcordoComFormatter() {
        var sala = mock(Sala.class);
        given(sala.getId()).willReturn(3L);
        given(sala.getNome()).willReturn("Sala 3");
        given(sala.getCapacidade()).willReturn(5);
        given(sala.getDataRegistro()).willReturn(null);
        given(sala.isReservada()).willReturn(false);

        try {
            var result = mapper.toInfoResponse(sala);
            assertNotNull(result);
            assertNull(result.dataCadastro());
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }
    }
}
