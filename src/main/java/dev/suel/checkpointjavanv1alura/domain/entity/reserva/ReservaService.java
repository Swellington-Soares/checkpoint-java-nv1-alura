package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators.IReservaValidator;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaService;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioService;
import dev.suel.checkpointjavanv1alura.exception.SalaJaReservadaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SalaService salaService;
    private final UsuarioService usuarioService;
    private final List<IReservaValidator> reservaValidators;

    @Transactional
    public Reserva reservarSala(
            Long salaId,
            Long usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        log.info("Cadastro de Reserva: SalaId={}, UsuarioId={}, DataInicio={}, DataFim={}", salaId, usuarioId, dataInicio, dataFim);

        List<Reserva> reservasAtiva = reservaRepository.buscarConflitosPorSalaOuUsuario(salaId, usuarioId, dataInicio, dataFim);

        if (!reservasAtiva.isEmpty()) {
            throw new SalaJaReservadaException();
        }

        Reserva reserva = Reserva.withId();
        reserva.setUsuario(usuarioService.findById(usuarioId));
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setSala(salaService.findById(salaId));
        reserva.setSituacao(SituacaoReserva.ATIVA);

        reservaValidators.forEach(validator -> validator.execute(reserva));

        log.info("Cadastro de Reserva com ID={}", reserva.getId());

        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva cancelarReserva(UUID reservaId, String motivoCancelamento) {
        log.info("Cancelando Reserva: ID={} MotivoCancelamento={}", reservaId, motivoCancelamento);
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow();
        reserva.cancelar(motivoCancelamento);
        return reserva;
    }


    public Page<Reserva> findAll(Pageable pageable) {
        return reservaRepository.findAll(pageable);
    }

    public void delete(UUID id) {
        reservaRepository.deleteById(id);
    }

    public Reserva getById(UUID id) {
        return reservaRepository.findById(id).orElseThrow();
    }
}
