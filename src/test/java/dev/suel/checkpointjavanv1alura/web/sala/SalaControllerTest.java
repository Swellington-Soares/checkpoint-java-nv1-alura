package dev.suel.checkpointjavanv1alura.web.sala;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaService;
import dev.suel.checkpointjavanv1alura.web.sala.data.SalaCadastroData;
import dev.suel.checkpointjavanv1alura.web.sala.data.SalaInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SalaController.class)
class SalaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SalaService salaService;

    @MockitoBean
    private SalaMapper salaMapper;

    @Test
    void getAll_deveRetornar200EMapearSlice() throws Exception {
        var s1 = salaComId(1L);
        var s2 = salaComId(2L);

        Page<Sala> slice = new PageImpl<>(
                List.of(s1, s2),
                PageRequest.of(0, 2, Sort.by("id").ascending()),
                2
        );

        given(salaService.getAll(any(Pageable.class))).willReturn(slice);
        given(salaMapper.toInfoResponse(s1)).willReturn(infoResponse(1L));
        given(salaMapper.toInfoResponse(s2)).willReturn(infoResponse(2L));

        mvc.perform(get("/api/v1/salas")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(1));

        then(salaService).should().getAll(any(Pageable.class));
        then(salaMapper).should().toInfoResponse(s1);
        then(salaMapper).should().toInfoResponse(s2);
    }

    @Test
    void show_deveRetornar200EBody() throws Exception {
        var sala = salaComId(7L);
        var response = infoResponse(7L);

        given(salaService.findById(7L)).willReturn(sala);
        given(salaMapper.toInfoResponse(sala)).willReturn(response);

        mvc.perform(get("/api/v1/salas/{id}", 7L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nome").value("Sala 7"))
                .andExpect(jsonPath("$.capacidade").value(12))
                .andExpect(jsonPath("$.dataCadastro").value("2026-01-01"))
                .andExpect(jsonPath("$.reservada").value("N"));

        then(salaService).should().findById(7L);
        then(salaMapper).should().toInfoResponse(sala);
    }

    @Test
    void delete_deveRetornar204EChamarService() throws Exception {
        mvc.perform(delete("/api/v1/salas/{id}", 9L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(salaService).should().deletarPeloId(9L);
        then(salaMapper).shouldHaveNoInteractions();
    }

    @Test
    void cadastrarNova_deveRetornar201LocationEBody() throws Exception {
        var sala = salaComId(10L);
        var response = infoResponse(10L);

        given(salaService.registrarNovaSala(eq("Sala de Reunião"), eq(20))).willReturn(sala);
        given(salaMapper.toInfoResponse(sala)).willReturn(response);

        var body = """
                {
                  "nome": "Sala de Reunião",
                  "capacidade": 20
                }
                """;

        mvc.perform(post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/salas/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Sala 10"))
                .andExpect(jsonPath("$.capacidade").value(12))
                .andExpect(jsonPath("$.dataCadastro").value("2026-01-01"))
                .andExpect(jsonPath("$.reservada").value("N"));

        then(salaService).should().registrarNovaSala(eq("Sala de Reunião"), eq(20));
        then(salaMapper).should().toInfoResponse(sala);
    }

    @Test
    void cadastrarNova_quandoNomeBlank_deveRetornar400() throws Exception {
        var body = """
                {
                  "nome": "   ",
                  "capacidade": 1
                }
                """;

        mvc.perform(post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(salaService).shouldHaveNoInteractions();
        then(salaMapper).shouldHaveNoInteractions();
    }

    @Test
    void cadastrarNova_quandoCapacidadeZero_deveRetornar400() throws Exception {
        var body = """
                {
                  "nome": "Sala X",
                  "capacidade": 0
                }
                """;

        mvc.perform(post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(salaService).shouldHaveNoInteractions();
        then(salaMapper).shouldHaveNoInteractions();
    }

    @Test
    void cadastrarNova_quandoCapacidadeNegativa_deveRetornar400() throws Exception {
        var body = """
                {
                  "nome": "Sala X",
                  "capacidade": -5
                }
                """;

        mvc.perform(post("/api/v1/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        then(salaService).shouldHaveNoInteractions();
        then(salaMapper).shouldHaveNoInteractions();
    }

    private Sala salaComId(Long id) {
        var s = new Sala();
        s.setId(id);
        s.setNome("Sala " + id);
        s.setCapacidade(12);
        return s;
    }

    private SalaInfoResponse infoResponse(Long id) {
        return new SalaInfoResponse(
                id,
                "Sala " + id,
                "2026-01-01",
                12,
                "N"
        );
    }
}
