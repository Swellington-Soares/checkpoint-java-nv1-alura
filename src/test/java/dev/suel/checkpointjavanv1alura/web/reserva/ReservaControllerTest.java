package dev.suel.checkpointjavanv1alura.web.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.ReservaMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.ReservaService;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;
import dev.suel.checkpointjavanv1alura.web.reserva.data.ReservaCanceladaResponse;
import dev.suel.checkpointjavanv1alura.web.reserva.data.ReservaInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ReservaService reservaService;

    @MockitoBean
    private ReservaMapper reservaMapper;

    @Test
    void getAll_deveRetornar200EMapearSlice() throws Exception {
        var r1 = reservaComId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        var r2 = reservaComId(UUID.fromString("22222222-2222-2222-2222-222222222222"));

        Page<Reserva> slice = new PageImpl<>(
                List.of(r1, r2),
                PageRequest.of(0, 2, Sort.by("id").ascending()),
                2
        );

        given(reservaService.findAll(any(Pageable.class))).willReturn(slice);
        given(reservaMapper.toInfoResponse(r1)).willReturn(infoResponse(r1.getId(), false));
        given(reservaMapper.toInfoResponse(r2)).willReturn(infoResponse(r2.getId(), false));

        mvc.perform(get("/api/v1/reservas")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("11111111-1111-1111-1111-111111111111"))
                .andExpect(jsonPath("$.content[1].id").value("22222222-2222-2222-2222-222222222222"))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(1));

        then(reservaService).should().findAll(any(Pageable.class));
        then(reservaMapper).should().toInfoResponse(r1);
        then(reservaMapper).should().toInfoResponse(r2);
    }

    @Test
    void fazerReserva_deveRetornar200EBody() throws Exception {
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        var reserva = reservaComId(id);

        given(reservaService.reservarSala(eq(1L), eq(2L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(reserva);
        given(reservaMapper.toInfoResponse(reserva)).willReturn(infoResponse(id, false));

        var body = """
                {
                  "sala": 1,
                  "usuario": 2,
                  "dataInicio": "2030-01-01T10:00:00",
                  "dataFim": "2030-01-01T12:00:00"
                }
                """;

        mvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(jsonPath("$.situacao").value("ATIVA"))
                .andExpect(jsonPath("$.cancelado").value(false))
                .andExpect(jsonPath("$.usuario").exists())
                .andExpect(jsonPath("$.usuario.cpf").value("52998224725"))
                .andExpect(jsonPath("$.sala").exists())
                .andExpect(jsonPath("$.sala.id").value(1))
                .andExpect(jsonPath("$.dataInicio").value("2030-01-01T10:00:00"))
                .andExpect(jsonPath("$.dataFim").value("2030-01-01T12:00:00"));

        then(reservaService).should().reservarSala(eq(1L), eq(2L), any(LocalDateTime.class), any(LocalDateTime.class));
        then(reservaMapper).should().toInfoResponse(reserva);
    }

    @Test
    void fazerReserva_quandoSalaNula_deveRetornar400() throws Exception {
        var body = """
                {
                  "sala": null,
                  "usuario": 2,
                  "dataInicio": "2030-01-01T10:00:00",
                  "dataFim": "2030-01-01T12:00:00"
                }
                """;

        mvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(reservaService).shouldHaveNoInteractions();
        then(reservaMapper).shouldHaveNoInteractions();
    }

    @Test
    void fazerReserva_quandoUsuarioNulo_deveRetornar400() throws Exception {
        var body = """
                {
                  "sala": 1,
                  "usuario": null,
                  "dataInicio": "2030-01-01T10:00:00",
                  "dataFim": "2030-01-01T12:00:00"
                }
                """;

        mvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(reservaService).shouldHaveNoInteractions();
        then(reservaMapper).shouldHaveNoInteractions();
    }

    @Test
    void fazerReserva_quandoDataInicioNoPassado_deveRetornar400() throws Exception {
        var body = """
                {
                  "sala": 1,
                  "usuario": 2,
                  "dataInicio": "2000-01-01T10:00:00",
                  "dataFim": "2030-01-01T12:00:00"
                }
                """;

        mvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(reservaService).shouldHaveNoInteractions();
        then(reservaMapper).shouldHaveNoInteractions();
    }

    @Test
    void cancelar_deveRetornar200EBody() throws Exception {
        var id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        var reserva = reservaComId(id);

        given(reservaService.cancelarReserva(eq(id), eq("sem necessidade"))).willReturn(reserva);
        given(reservaMapper.toInfoCancelamento(reserva)).willReturn(
                new ReservaCanceladaResponse(id, "2030-01-01T10:05:00", "sem necessidade")
        );

        var body = """
                {
                  "motivoCancelamento": "sem necessidade"
                }
                """;

        mvc.perform(patch("/api/v1/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                .andExpect(jsonPath("$.dataCancelamento").value("2030-01-01T10:05:00"))
                .andExpect(jsonPath("$.motivoCancelamento").value("sem necessidade"));

        then(reservaService).should().cancelarReserva(eq(id), eq("sem necessidade"));
        then(reservaMapper).should().toInfoCancelamento(reserva);
    }

    @Test
    void showDetail_deveRetornar200EBody() throws Exception {
        var id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
        var reserva = reservaComId(id);

        given(reservaService.getById(id)).willReturn(reserva);
        given(reservaMapper.toInfoResponse(reserva)).willReturn(infoResponse(id, false));

        mvc.perform(get("/api/v1/reservas/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cccccccc-cccc-cccc-cccc-cccccccccccc"))
                .andExpect(jsonPath("$.situacao").value("ATIVA"))
                .andExpect(jsonPath("$.cancelado").value(false));

        then(reservaService).should().getById(id);
        then(reservaMapper).should().toInfoResponse(reserva);
    }

    @Test
    void delete_deveRetornar204EChamarService() throws Exception {
        var id = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

        mvc.perform(delete("/api/v1/reservas/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(reservaService).should().delete(id);
        then(reservaMapper).shouldHaveNoInteractions();
    }

    private Reserva reservaComId(UUID id) {
        var r = new Reserva();
        r.setId(id);
        r.setSituacao(SituacaoReserva.ATIVA);
        r.setDataInicio(LocalDateTime.of(2030, 1, 1, 10, 0));
        r.setDataFim(LocalDateTime.of(2030, 1, 1, 12, 0));
        return r;
    }

    private ReservaInfoResponse infoResponse(UUID id, boolean cancelado) {
        return new ReservaInfoResponse(
                id,
                new ReservaInfoResponse.UsuarioInfo("52998224725", "Suel", "suel@email.com", "81999999999"),
                new ReservaInfoResponse.SalaInfo(1L, "Sala 1", 20),
                "2030-01-01T10:00:00",
                "2030-01-01T12:00:00",
                SituacaoReserva.ATIVA,
                cancelado
        );
    }
}
