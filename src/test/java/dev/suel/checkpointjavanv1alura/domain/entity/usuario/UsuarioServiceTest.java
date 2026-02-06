package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.validators.IUsuarioValidator;
import dev.suel.checkpointjavanv1alura.exception.UsuarioJaCadastradoException;
import dev.suel.checkpointjavanv1alura.exception.UsuarioNaoExisteException;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioMapper usuarioMapper;
    private IUsuarioValidator v1;
    private IUsuarioValidator v2;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioMapper = mock(UsuarioMapper.class);
        v1 = mock(IUsuarioValidator.class);
        v2 = mock(IUsuarioValidator.class);

        usuarioService = new UsuarioService(
                usuarioRepository,
                List.of(v1, v2),
                usuarioMapper
        );
    }

    @Test
    void findById_quandoExiste_deveRetornarUsuario() {
        var usuario = new Usuario();
        usuario.setId(1L);

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));

        var result = usuarioService.findById(1L);

        assertSame(usuario, result);
        then(usuarioRepository).should().findById(1L);
    }

    @Test
    void findById_quandoNaoExiste_deveLancar() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(UsuarioNaoExisteException.class, () -> usuarioService.findById(99L));

        then(usuarioRepository).should().findById(99L);
    }

    @Test
    void cadastrarUsuario_quandoNaoExiste_deveValidarESalvar() {
        var data = new UsuarioCadastroData(
                "52998224725",
                "Suel",
                "Soares",
                LocalDate.of(2000, 1, 1),
                "suel@email.com",
                "81999999999"
        );

        var usuario = new Usuario();
        usuario.setCpf("52998224725");
        usuario.setEmail("suel@email.com");

        var salvo = new Usuario();
        salvo.setId(10L);

        given(usuarioMapper.cadastroDataToUsuario(data)).willReturn(usuario);
        given(usuarioRepository.existsByCpfOrEmailAllIgnoreCase("52998224725", "suel@email.com")).willReturn(false);
        given(usuarioRepository.save(usuario)).willReturn(salvo);

        var result = usuarioService.cadastrarUsuario(data);

        assertSame(salvo, result);
        then(usuarioMapper).should().cadastroDataToUsuario(data);
        then(v1).should().execute(usuario);
        then(v2).should().execute(usuario);
        then(usuarioRepository).should().existsByCpfOrEmailAllIgnoreCase("52998224725", "suel@email.com");
        then(usuarioRepository).should().save(usuario);
    }

    @Test
    void cadastrarUsuario_quandoCpfOuEmailJaExiste_deveLancar() {
        var data = new UsuarioCadastroData(
                "52998224725",
                "Suel",
                "Soares",
                LocalDate.of(2000, 1, 1),
                "suel@email.com",
                "81999999999"
        );

        var usuario = new Usuario();
        usuario.setCpf("52998224725");
        usuario.setEmail("suel@email.com");

        given(usuarioMapper.cadastroDataToUsuario(data)).willReturn(usuario);
        given(usuarioRepository.existsByCpfOrEmailAllIgnoreCase("52998224725", "suel@email.com")).willReturn(true);

        assertThrows(UsuarioJaCadastradoException.class, () -> usuarioService.cadastrarUsuario(data));

        then(usuarioMapper).should().cadastroDataToUsuario(data);
        then(v1).should().execute(usuario);
        then(v2).should().execute(usuario);
        then(usuarioRepository).should().existsByCpfOrEmailAllIgnoreCase("52998224725", "suel@email.com");
        then(usuarioRepository).should(never()).save(any(Usuario.class));
    }

    @Test
    void atualizar_deveAplicarAtualizacaoParcialEValidar() {
        var usuario = new Usuario();
        usuario.setId(3L);

        var data = new UsuarioUpdateData(
                "52998224725",
                "NovoNome",
                "Soares",
                LocalDate.of(2000, 1, 1),
                "81999999999"
        );

        given(usuarioRepository.findById(3L)).willReturn(Optional.of(usuario));

        usuarioService.atualizar(3L, data);

        then(usuarioRepository).should().findById(3L);
        then(usuarioMapper).should().atualizacaoParcial(usuario, data);
        then(v1).should().execute(usuario);
        then(v2).should().execute(usuario);
        then(usuarioRepository).should(never()).save(any(Usuario.class));
    }

    @Test
    void atualizar_quandoUsuarioNaoExiste_deveLancar() {
        var data = new UsuarioUpdateData(null, null, null, null, null);
        given(usuarioRepository.findById(404L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usuarioService.atualizar(404L, data));

        then(usuarioRepository).should().findById(404L);
        then(usuarioMapper).shouldHaveNoInteractions();
        then(v1).shouldHaveNoInteractions();
        then(v2).shouldHaveNoInteractions();
    }

    @Test
    void getAll_deveDelegarParaRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> slice = new PageImpl<>(List.of(new Usuario()), pageable, 2);

        given(usuarioRepository.findAll(pageable)).willReturn(slice);

        var result = usuarioService.getAll(pageable);

        assertSame(slice, result);
        then(usuarioRepository).should().findAll(pageable);
    }

    @Test
    void deletarUsuarioPorId_deveDelegarParaRepository() {
        usuarioService.deletarUsuarioPorId(9L);

        then(usuarioRepository).should().deleteById(9L);
    }
}
