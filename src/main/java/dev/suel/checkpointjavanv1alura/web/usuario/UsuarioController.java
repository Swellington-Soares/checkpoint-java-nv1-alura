package dev.suel.checkpointjavanv1alura.web.usuario;


import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioService;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioCadastroData;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioInfoResponse;
import dev.suel.checkpointjavanv1alura.web.usuario.data.UsuarioUpdateData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    public ResponseEntity<Page<UsuarioInfoResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                usuarioService.getAll(pageable).map(usuarioMapper::toInfoResponse)
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioInfoResponse> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroData data) {
        var usuario = usuarioService.cadastrarUsuario(data);
        return ResponseEntity.created(
                URI.create("/api/v1/usuarios/" + usuario.getId())
        ).body(usuarioMapper.toInfoResponse(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioInfoResponse> show(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioMapper.toInfoResponse(usuarioService.findById(id)));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateData data) {
        usuarioService.atualizar(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


}
