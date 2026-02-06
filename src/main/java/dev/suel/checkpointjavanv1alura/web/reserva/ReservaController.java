package dev.suel.checkpointjavanv1alura.web.reserva;


import dev.suel.checkpointjavanv1alura.domain.entity.reserva.ReservaMapper;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.ReservaService;
import dev.suel.checkpointjavanv1alura.web.reserva.data.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Slf4j
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapper reservaMapper;

    @GetMapping
    ResponseEntity<Page<ReservaInfoResponse>> getAll(
            @ModelAttribute ReservaFiltro filtro,
            Pageable pageable) {

        var reservas = reservaService.findAll(filtro, pageable)
                .map(reservaMapper::toInfoResponse);
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    ResponseEntity<ReservaInfoResponse> fazerReserva(@RequestBody @Valid ReservaCadastroData data) {
        return ResponseEntity.ok(
                reservaMapper.toInfoResponse(reservaService.reservarSala(
                        data.sala(),
                        data.usuario(),
                        data.dataInicio(),
                        data.dataFim()
                ))
        );
    }

    @PatchMapping("/{id}/cancelar")
    ResponseEntity<ReservaCanceladaResponse> cancelar(
            @PathVariable UUID id,
            @RequestBody ReservaCancelamentoData data) {
        return ResponseEntity.ok(
                reservaMapper.toInfoCancelamento(reservaService.cancelarReserva(id, data.motivoCancelamento()))
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ReservaInfoResponse> showDetail(@PathVariable UUID id) {
        return ResponseEntity.ok( reservaMapper.toInfoResponse( reservaService.findById(id) ) );
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
