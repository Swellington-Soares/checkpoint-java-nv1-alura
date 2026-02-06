package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.CancelamentoDeReservaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReservaTest {
    @Test
    void withId_deveGerarIdNaoNuloEDiferenteEntreChamadas() {
        var r1 = Reserva.withId();
        var r2 = Reserva.withId();

        assertNotNull(r1.getId());
        assertNotNull(r2.getId());
        assertNotEquals(r1.getId(), r2.getId());
    }

    @Test
    void cancelar_quandoSituacaoNaoAtiva_deveLancarExcecao() {
        var reserva = novaReservaBase();
        reserva.setSituacao(SituacaoReserva.CANCELADA);
        reserva.setDataInicio(LocalDateTime.now().minusDays(1));

        var ex = assertThrows(CancelamentoDeReservaException.class, reserva::cancelar);
        assertEquals("A reserva não se encontra ativa.", ex.getMessage());
    }

    @Test
    void cancelar_quandoSituacaoNula_deveLancarNullPointerException() {
        var reserva = novaReservaBase();
        reserva.setSituacao(null);
        reserva.setDataInicio(LocalDateTime.now().minusDays(1));

        assertThrows(NullPointerException.class, reserva::cancelar);
    }

    @Test
    void cancelar_quandoMaisDeTresDiasPeloToDays_deveLancarExcecao() {
        var reserva = novaReservaBase();
        reserva.setSituacao(SituacaoReserva.ATIVA);
        reserva.setDataInicio(LocalDateTime.now().minusDays(4));

        var ex = assertThrows(CancelamentoDeReservaException.class, reserva::cancelar);
        assertEquals("O cancelamento da reserva é permitido somente até 3 dias após a data de início.", ex.getMessage());
    }

    @Test
    void cancelar_quandoDentroDeTresDias_deveCancelarERegistrarDataCancelamento() {
        var reserva = novaReservaBase();
        reserva.setSituacao(SituacaoReserva.ATIVA);
        reserva.setDataInicio(LocalDateTime.now().minusDays(2));
        reserva.setDataCancelamento(null);

        var antes = LocalDateTime.now();
        reserva.cancelar();
        var depois = LocalDateTime.now();

        assertEquals(SituacaoReserva.CANCELADA, reserva.getSituacao());
        assertNotNull(reserva.getDataCancelamento());
        assertFalse(reserva.getDataCancelamento().isBefore(antes));
        assertFalse(reserva.getDataCancelamento().isAfter(depois));
    }

    @Test
    void cancelar_quandoExatamenteTresDias_devePermitirPorContaDaCondicaoMaiorQueTres() {
        var reserva = novaReservaBase();
        reserva.setSituacao(SituacaoReserva.ATIVA);
        reserva.setDataInicio(LocalDateTime.now().minusDays(3));
        reserva.setDataCancelamento(null);

        reserva.cancelar();

        assertEquals(SituacaoReserva.CANCELADA, reserva.getSituacao());
        assertNotNull(reserva.getDataCancelamento());
    }

    @Test
    void cancelar_quandoTresDiasEAlgumasHoras_aindaPodePassarPorContaDoToDaysTruncado() {
        var reserva = novaReservaBase();
        reserva.setSituacao(SituacaoReserva.ATIVA);
        reserva.setDataInicio(LocalDateTime.now().minusDays(2).minusHours(23));
        reserva.setDataCancelamento(null);

        reserva.cancelar();

        assertEquals(SituacaoReserva.CANCELADA, reserva.getSituacao());
        assertNotNull(reserva.getDataCancelamento());
    }

    private Reserva novaReservaBase() {
        var usuario = mock(Usuario.class);
        var sala = mock(Sala.class);
        var reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setSala(sala);
        reserva.setDataFim(LocalDateTime.now().plusDays(1));
        return reserva;
    }

}