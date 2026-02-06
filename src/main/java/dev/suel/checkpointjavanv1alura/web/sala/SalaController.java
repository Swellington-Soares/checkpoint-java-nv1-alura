package dev.suel.checkpointjavanv1alura.web.sala;


import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaService;
import dev.suel.checkpointjavanv1alura.web.sala.data.SalaCadastroData;
import dev.suel.checkpointjavanv1alura.web.sala.data.SalaInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
@Slf4j
public class SalaController {

    private final SalaService salaService;
    private final SalaMapper salaMapper;

    @GetMapping
    ResponseEntity<Page<SalaInfoResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                salaService.getAll(pageable)
                        .map(salaMapper::toInfoResponse)
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<SalaInfoResponse> show(@PathVariable Long id) {
        return ResponseEntity.ok(salaMapper.toInfoResponse(salaService.findById(id)));
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        salaService.deletarPeloId(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    ResponseEntity<SalaInfoResponse> cadastrarNova(@RequestBody @Valid SalaCadastroData data) {
        Sala sala = salaService.registrarNovaSala(data.nome(), data.capacidade());
        return ResponseEntity.created(
                URI.create("/api/v1/salas/" + sala.getId())
        ).body(salaMapper.toInfoResponse(sala));
    }

}
