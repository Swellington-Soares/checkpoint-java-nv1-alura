package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void cadastroDataToUsuario_deveCopiarCamposDoDtoParaEntity() {
        var data = new UsuarioCadastroData(
                "52998224725",
                "Suel",
                "Soares",
                LocalDate.of(2000, 1, 1),
                "suel@email.com",
                "+5581999999999"
        );

        var usuario = mapper.cadastroDataToUsuario(data);

        assertNotNull(usuario);
        assertEquals("52998224725", usuario.getCpf());
        assertEquals("Suel", usuario.getNome());
        assertEquals("Soares", usuario.getSobrenome());
        assertEquals(LocalDate.of(2000, 1, 1), usuario.getDataNascimento());
        assertEquals("suel@email.com", usuario.getEmail());
        assertEquals("+5581999999999", usuario.getTelefone());
    }

    @Test
    void toInfoResponse_deveMapearFormatandoCpfDataNascimentoETelefone() {
        var usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNome("Suel");
        usuario.setSobrenome("Soares");
        usuario.setCpf("52998224725");
        usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
        usuario.setEmail("suel@email.com");
        usuario.setTelefone("+5581999999999");

        var response = mapper.toInfoResponse(usuario);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals(usuario.getNomeCompleto(), response.nome());
        assertEquals(FormatterUtil.formatarCPF("52998224725"), response.cpf());
        assertEquals(FormatterUtil.formatarDataNascimento(LocalDate.of(2000, 1, 1)), response.dataNascimento());
        assertEquals("suel@email.com", response.email());
        assertEquals(FormatterUtil.formatarNumeroTelefone("+5581999999999"), response.telefone());
    }

    @Test
    void atualizacaoParcial_quandoCamposNaoNulos_deveSobrescrever() {
        var usuario = new Usuario();
        usuario.setNome("Antigo");
        usuario.setSobrenome("SobrenomeAntigo");
        usuario.setCpf("11111111111");
        usuario.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuario.setTelefone("81900000000");

        var data = new UsuarioUpdateData(
                "52998224725",
                "Novo",
                "Soares",
                LocalDate.of(2000, 1, 1),
                "81999999999"
        );

        mapper.atualizacaoParcial(usuario, data);

        assertEquals("Novo", usuario.getNome());
        assertEquals("Soares", usuario.getSobrenome());
        assertEquals("52998224725", usuario.getCpf());
        assertEquals(LocalDate.of(2000, 1, 1), usuario.getDataNascimento());
        assertEquals("81999999999", usuario.getTelefone());
    }

    @Test
    void atualizacaoParcial_quandoCamposNulos_deveManterValoresAtuais() {
        var usuario = new Usuario();
        usuario.setNome("Atual");
        usuario.setSobrenome("AtualSobrenome");
        usuario.setCpf("52998224725");
        usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
        usuario.setTelefone("81999999999");

        var data = new UsuarioUpdateData(
                null,
                null,
                null,
                null,
                null
        );

        mapper.atualizacaoParcial(usuario, data);

        assertEquals("Atual", usuario.getNome());
        assertEquals("AtualSobrenome", usuario.getSobrenome());
        assertEquals("52998224725", usuario.getCpf());
        assertEquals(LocalDate.of(2000, 1, 1), usuario.getDataNascimento());
        assertEquals("81999999999", usuario.getTelefone());
    }

    @Test
    void atualizacaoParcial_quandoAlgunsCamposNulos_deveAtualizarSomenteOsPreenchidos() {
        var usuario = new Usuario();
        usuario.setNome("Atual");
        usuario.setSobrenome("AtualSobrenome");
        usuario.setCpf("52998224725");
        usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
        usuario.setTelefone("81999999999");

        var data = new UsuarioUpdateData(
                null,
                "NovoNome",
                null,
                null,
                "81988887777"
        );

        mapper.atualizacaoParcial(usuario, data);

        assertEquals("NovoNome", usuario.getNome());
        assertEquals("AtualSobrenome", usuario.getSobrenome());
        assertEquals("52998224725", usuario.getCpf());
        assertEquals(LocalDate.of(2000, 1, 1), usuario.getDataNascimento());
        assertEquals("81988887777", usuario.getTelefone());
    }
}
