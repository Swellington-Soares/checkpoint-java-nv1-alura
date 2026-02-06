package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import dev.suel.checkpointjavanv1alura.utils.FormatterUtil;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioInfoResponse;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario cadastroDataToUsuario(UsuarioCadastroData usuarioData) {
        var usuario = new Usuario();
        usuario.setCpf(usuarioData.cpf());
        usuario.setEmail(usuarioData.email());
        usuario.setNome(usuarioData.nome());
        usuario.setSobrenome(usuarioData.sobrenome());
        usuario.setTelefone(usuarioData.telefone());
        usuario.setDataNascimento(usuarioData.dataNascimento());
        return usuario;
    }

    public UsuarioInfoResponse toInfoResponse(Usuario usuario) {
        return new UsuarioInfoResponse(
                usuario.getId(),
                usuario.getNomeCompleto(),
                FormatterUtil.formatarCPF(usuario.getCpf()),
                FormatterUtil.formatarDataNascimento(usuario.getDataNascimento()),
                usuario.getEmail(),
                FormatterUtil.formatarNumeroTelefone(usuario.getTelefone())
        );
    }

    public void atualizacaoParcial(Usuario usuario, UsuarioUpdateData data) {
        usuario.setNome(data.nome() != null ? data.nome() : usuario.getNome());
        usuario.setSobrenome(data.sobrenome() != null ? data.sobrenome() : usuario.getSobrenome());
        usuario.setCpf(data.cpf() != null ? data.cpf() : usuario.getCpf());
        usuario.setDataNascimento(data.dataNascimento() != null ? data.dataNascimento() : usuario.getDataNascimento());
        usuario.setTelefone(data.telefone() != null ? data.telefone() : usuario.getTelefone());
    }

}
