package dev.suel.checkpointjavanv1alura.web.usuario;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioService;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioInfoResponse;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    @Test
    void getAll_deveRetornar200EMapearSlice() throws Exception {
        var u1 = usuarioComId(1L);
        var u2 = usuarioComId(2L);

        Page<Usuario> slice = new PageImpl<>(
                List.of(u1, u2),
                PageRequest.of(0, 2, Sort.by("id").ascending()),
                2
        );

        given(usuarioService.getAll(any(Pageable.class))).willReturn(slice);
        given(usuarioMapper.toInfoResponse(u1)).willReturn(infoResponse(1L));
        given(usuarioMapper.toInfoResponse(u2)).willReturn(infoResponse(2L));

        mvc.perform(get("/api/v1/usuarios")
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

        then(usuarioService).should().getAll(any(Pageable.class));
        then(usuarioMapper).should().toInfoResponse(u1);
        then(usuarioMapper).should().toInfoResponse(u2);
    }

    @Test
    void cadastrarUsuario_deveRetornar201LocationEBody() throws Exception {
        var usuario = usuarioComId(10L);
        usuario.setSobrenome("Soares");
        var response = infoResponse(10L);


        given(usuarioService.cadastrarUsuario(any(UsuarioCadastroData.class))).willReturn(usuario);
        given(usuarioMapper.toInfoResponse(usuario)).willReturn(response);

        var body = """
                {
                  "cpf": "09455164019",
                  "nome": "Suel",
                  "sobrenome": "Soares",
                  "dataNascimento": "2000-01-01",
                  "email": "suel@email.com",
                  "telefone": "+5581999999999"
                }
                """;

        mvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/usuarios/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Suel Soares"))
                .andExpect(jsonPath("$.cpf").value("09455164019"))
                .andExpect(jsonPath("$.dataNascimento").value("2000-01-01"))
                .andExpect(jsonPath("$.email").value("suel@email.com"))
                .andExpect(jsonPath("$.telefone").value("+5599999999999"));

        then(usuarioService).should().cadastrarUsuario(any(UsuarioCadastroData.class));
        then(usuarioMapper).should().toInfoResponse(usuario);
    }

    @Test
    void cadastrarUsuario_quandoCpfInvalido_deveRetornar400() throws Exception {
        var body = """
                {
                  "cpf": "123",
                  "nome": "Suel",
                  "sobrenome": "Soares",
                  "dataNascimento": "2000-01-01",
                  "email": "suel@email.com",
                  "telefone": "81999999999"
                }
                """;

        mvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        then(usuarioService).shouldHaveNoInteractions();
        then(usuarioMapper).shouldHaveNoInteractions();
    }

    @Test
    void cadastrarUsuario_quandoDataNascimentoFutura_deveRetornar400() throws Exception {
        var body = """
                {
                  "cpf": "52998224725",
                  "nome": "Suel",
                  "sobrenome": "Soares",
                  "dataNascimento": "2999-01-01",
                  "email": "suel@email.com",
                  "telefone": "81999999999"
                }
                """;

        mvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        then(usuarioService).shouldHaveNoInteractions();
        then(usuarioMapper).shouldHaveNoInteractions();
    }

    @Test
    void show_deveRetornar200EBody() throws Exception {
        var usuario = usuarioComId(7L);
        var response = infoResponse(7L);

        given(usuarioService.findById(7L)).willReturn(usuario);
        given(usuarioMapper.toInfoResponse(usuario)).willReturn(response);

        mvc.perform(get("/api/v1/usuarios/{id}", 7L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nome").value("Suel Soares"))
                .andExpect(jsonPath("$.cpf").value("09455164019"))
                .andExpect(jsonPath("$.dataNascimento").value("2000-01-01"))
                .andExpect(jsonPath("$.email").value("suel@email.com"))
                .andExpect(jsonPath("$.telefone").value("+5599999999999"));

        then(usuarioService).should().findById(7L);
        then(usuarioMapper).should().toInfoResponse(usuario);
    }

    @Test
    void atualizar_deveRetornar200EChamarService() throws Exception {
        var body = """
                {
                  "cpf": "52998224725",
                  "nome": "NovoNome",
                  "sobrenome": "Soares",
                  "dataNascimento": "2000-01-01",
                  "telefone": "81999999999"
                }
                """;

        mvc.perform(patch("/api/v1/usuarios/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        then(usuarioService).should().atualizar(eq(3L), any(UsuarioUpdateData.class));
        then(usuarioMapper).shouldHaveNoInteractions();
    }

    @Test
    void atualizar_quandoJsonInvalidoParaLocalDate_deveRetornar400() throws Exception {
        var body = """
                {
                  "cpf": "52998224725",
                  "nome": "NovoNome",
                  "sobrenome": "Soares",
                  "dataNascimento": "data-invalida",
                  "telefone": "81999999999"
                }
                """;

        mvc.perform(patch("/api/v1/usuarios/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        then(usuarioService).shouldHaveNoInteractions();
    }

    @Test
    void excluir_deveRetornar204EChamarService() throws Exception {
        mvc.perform(delete("/api/v1/usuarios/{id}", 9L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        then(usuarioService).should().deletarUsuarioPorId(9L);
        then(usuarioMapper).shouldHaveNoInteractions();
    }

    private Usuario usuarioComId(Long id) {
        var u = new Usuario();
        u.setId(id);
        u.setCpf("09455164019");
        u.setNome("Suel");
        u.setSobrenome("Soares");
        u.setEmail("suel@email.com");
        u.setTelefone("+5581999999999");
        u.setDataNascimento(LocalDate.of(2000, 1, 1));
        return u;
    }

    private UsuarioInfoResponse infoResponse(Long id) {
        return new UsuarioInfoResponse(
                id,
                "Suel Soares",
                "09455164019",
                "2000-01-01",
                "suel@email.com",
                "+5599999999999"
        );
    }
}
