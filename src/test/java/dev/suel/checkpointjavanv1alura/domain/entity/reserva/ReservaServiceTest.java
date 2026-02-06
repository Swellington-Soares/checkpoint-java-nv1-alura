package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators.IReservaValidator;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaService;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioService;
import dev.suel.checkpointjavanv1alura.exception.SalaJaReservadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    private ReservaRepository reservaRepository;
    private SalaService salaService;
    private UsuarioService usuarioService;
    private IReservaValidator v1;
    private IReservaValidator v2;

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaRepository = mock(ReservaRepository.class);
        salaService = mock(SalaService.class);
        usuarioService = mock(UsuarioService.class);
        v1 = mock(IReservaValidator.class);
        v2 = mock(IReservaValidator.class);

        reservaService = new ReservaService(
                reservaRepository,
                salaService,
                usuarioService,
                List.of(v1, v2)
        );
    }

    @Test
    void reservarSala_quandoHaConflito_deveLancar() {
        Long salaId = 1L;
        Long usuarioId = 2L;
        LocalDateTime inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2030, 1, 1, 12, 0);

        given(reservaRepository.buscarConflitosPorSalaOuUsuario(salaId, usuarioId, inicio, fim))
                .willReturn(List.of(new Reserva()));

        assertThrows(SalaJaReservadaException.class, () -> reservaService.reservarSala(salaId, usuarioId, inicio, fim));

        then(reservaRepository).should().buscarConflitosPorSalaOuUsuario(salaId, usuarioId, inicio, fim);
        then(usuarioService).shouldHaveNoInteractions();
        then(salaService).shouldHaveNoInteractions();
        then(reservaRepository).should(never()).save(any(Reserva.class));
        then(v1).shouldHaveNoInteractions();
        then(v2).shouldHaveNoInteractions();
    }

    @Test
    void reservarSala_quandoSemConflito_deveMontarValidarESalvar() {
        Long salaId = 1L;
        Long usuarioId = 2L;
        LocalDateTime inicio = LocalDateTime.of(2030, 1, 1, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2030, 1, 1, 12, 0);

        given(reservaRepository.buscarConflitosPorSalaOuUsuario(salaId, usuarioId, inicio, fim))
                .willReturn(List.of());

        var usuario = new Usuario();
        usuario.setId(usuarioId);

        var sala = new Sala();
        sala.setId(salaId);

        given(usuarioService.findById(usuarioId)).willReturn(usuario);
        given(salaService.findById(salaId)).willReturn(sala);

        ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
        given(reservaRepository.save(any(Reserva.class))).willAnswer(inv -> inv.getArgument(0));

        var result = reservaService.reservarSala(salaId, usuarioId, inicio, fim);

        then(reservaRepository).should().buscarConflitosPorSalaOuUsuario(salaId, usuarioId, inicio, fim);
        then(usuarioService).should().findById(usuarioId);
        then(salaService).should().findById(salaId);
        then(reservaRepository).should().save(captor.capture());

        var criada = captor.getValue();
        assertNotNull(criada.getId());
        assertSame(usuario, criada.getUsuario());
        assertSame(sala, criada.getSala());
        assertEquals(inicio, criada.getDataInicio());
        assertEquals(fim, criada.getDataFim());
        assertEquals(SituacaoReserva.ATIVA, criada.getSituacao());

        then(v1).should().execute(criada);
        then(v2).should().execute(criada);

        assertSame(criada, result);
    }

    @Test
    void cancelarReserva_quandoExiste_deveChamarCancelarEDevolverReserva() {
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        var reserva = mock(Reserva.class);

        given(reservaRepository.findById(id)).willReturn(Optional.of(reserva));

        var result = reservaService.cancelarReserva(id, "sem necessidade");

        assertSame(reserva, result);
        then(reservaRepository).should().findById(id);
        then(reserva).should().cancelar("sem necessidade");
    }

    @Test
    void cancelarReserva_quandoNaoExiste_deveLancar() {
        var id = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
        given(reservaRepository.findById(id)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reservaService.cancelarReserva(id, "x"));

        then(reservaRepository).should().findById(id);
    }

    @Test
    void findAll_deveDelegarParaRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Reserva> slice = new PageImpl<>(List.of(new Reserva()), pageable, 2);

        given(reservaRepository.findAll(pageable)).willReturn(slice);

        var result = reservaService.findAll(pageable);

        assertSame(slice, result);
        then(reservaRepository).should().findAll(pageable);
    }

    @Test
    void delete_deveDelegarParaRepository() {
        var id = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

        reservaService.delete(id);

        then(reservaRepository).should().deleteById(id);
    }

    @Test
    void getById_quandoExiste_deveRetornar() {
        var id = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");
        var reserva = new Reserva();
        reserva.setId(id);

        given(reservaRepository.findById(id)).willReturn(Optional.of(reserva));

        var result = reservaService.getById(id);

        assertSame(reserva, result);
        then(reservaRepository).should().findById(id);
    }

    @Test
    void getById_quandoNaoExiste_deveLancar() {
        var id = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");
        given(reservaRepository.findById(id)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reservaService.getById(id));

        then(reservaRepository).should().findById(id);
    }
}
