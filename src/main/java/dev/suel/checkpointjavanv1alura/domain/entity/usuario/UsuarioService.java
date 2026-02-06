package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators.IUsuarioValidator;
import dev.suel.checkpointjavanv1alura.exception.UsuarioJaCadastradoException;
import dev.suel.checkpointjavanv1alura.exception.UsuarioNaoExisteException;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final List<IUsuarioValidator> usuarioValidatorList;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public Usuario findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoExisteException(usuarioId));
    }

    @Transactional
    public Usuario cadastrarUsuario(UsuarioCadastroData data) {
        var usuario = usuarioMapper.cadastroDataToUsuario(data);
        usuarioValidatorList.forEach(usuarioValidator -> usuarioValidator.execute(usuario));

        if (usuarioRepository.existsByCpfOrEmailAllIgnoreCase(usuario.getCpf(), usuario.getEmail()))
            throw new UsuarioJaCadastradoException();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void atualizar(Long id, UsuarioUpdateData data) {
        var usuario = usuarioRepository.findById(id).orElseThrow();
        usuarioMapper.atualizacaoParcial(usuario, data);
        usuarioValidatorList.forEach(usuarioValidator -> usuarioValidator.execute(usuario));
    }

    @Transactional(readOnly = true)
    public Page<Usuario> getAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public void deletarUsuarioPorId(Long id) {
        usuarioRepository.deleteById(id);
    }
}
