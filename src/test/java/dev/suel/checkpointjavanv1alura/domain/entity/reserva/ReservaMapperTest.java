package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ReservaMapperTest {

    private final ReservaMapper mapper = new ReservaMapper();

    @Test
    void toInfoResponse_deveMapearTudoEIndicarCanceladoFalseQuandoDataCancelamentoNula() {
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        var inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        var fim = LocalDateTime.of(2030, 1, 1, 12, 0);

        var usuario = mock(Usuario.class);
        given(usuario.getCpf()).willReturn("52998224725");
        given(usuario.getNomeCompleto()).willReturn("Suel Soares");
        given(usuario.getEmail()).willReturn("suel@email.com");
        given(usuario.getTelefone()).willReturn("81999999999");

        var sala = mock(Sala.class);
        given(sala.getId()).willReturn(1L);
        given(sala.getNome()).willReturn("Sala 1");
        given(sala.getCapacidade()).willReturn(20);

        var reserva = new Reserva();
        reserva.setId(id);
        reserva.setUsuario(usuario);
        reserva.setSala(sala);
        reserva.setDataInicio(inicio);
        reserva.setDataFim(fim);
        reserva.setSituacao(SituacaoReserva.ATIVA);
        reserva.setDataCancelamento(null);

        var result = mapper.toInfoResponse(reserva);

        assertNotNull(result);
        assertEquals(id, result.id());

        assertNotNull(result.usuario());
        assertEquals("52998224725", result.usuario().cpf());
        assertEquals("Suel Soares", result.usuario().nome());
        assertEquals("suel@email.com", result.usuario().email());
        assertEquals("81999999999", result.usuario().telefone());

        assertNotNull(result.sala());
        assertEquals(1L, result.sala().id());
        assertEquals("Sala 1", result.sala().nome());
        assertEquals(20, result.sala().capacidade());

        assertEquals(FormatterUtil.formatarDataHora(inicio), result.dataInicio());
        assertEquals(FormatterUtil.formatarDataHora(fim), result.dataFim());

        assertEquals(SituacaoReserva.ATIVA, result.situacao());

    }

    @Test
    void toInfoResponse_deveIndicarCanceladoTrueQuandoDataCancelamentoNaoNula() {
        var id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

        var usuario = mock(Usuario.class);
        given(usuario.getCpf()).willReturn("11111111111");
        given(usuario.getNomeCompleto()).willReturn("Nome Completo");
        given(usuario.getEmail()).willReturn("email@email.com");
        given(usuario.getTelefone()).willReturn("81900000000");

        var sala = mock(Sala.class);
        given(sala.getId()).willReturn(2L);
        given(sala.getNome()).willReturn("Sala 2");
        given(sala.getCapacidade()).willReturn(10);

        var reserva = new Reserva();
        reserva.setId(id);
        reserva.setUsuario(usuario);
        reserva.setSala(sala);
        reserva.setDataInicio(LocalDateTime.of(2030, 2, 1, 9, 0));
        reserva.setDataFim(LocalDateTime.of(2030, 2, 1, 10, 0));
        reserva.setSituacao(SituacaoReserva.CANCELADA);
        reserva.setDataCancelamento(LocalDateTime.of(2030, 2, 1, 8, 30));

        var result = mapper.toInfoResponse(reserva);

     
        assertEquals(SituacaoReserva.CANCELADA, result.situacao());
    }

    @Test
    void toInfoCancelamento_deveMapearIdDataCancelamentoFormatadaEMotivo() {
        var id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
        var cancelamento = LocalDateTime.of(2030, 3, 1, 10, 5);

        var reserva = new Reserva();
        reserva.setId(id);
        reserva.setDataCancelamento(cancelamento);
        reserva.setMotivoCancelamento("sem necessidade");

        var result = mapper.toInfoCancelamento(reserva);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(FormatterUtil.formatarDataHora(cancelamento), result.dataCancelamento());
        assertEquals("sem necessidade", result.motivoCancelamento());
    }

    @Test
    void toInfoCancelamento_quandoDataCancelamentoNula_deveLancarOuRetornarNuloDeAcordoComFormatter() {
        var reserva = new Reserva();
        reserva.setId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
        reserva.setDataCancelamento(null);
        reserva.setMotivoCancelamento("x");

        try {
            var result = mapper.toInfoCancelamento(reserva);
            assertNotNull(result);
            assertNull(result.dataCancelamento());
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }
    }
}
