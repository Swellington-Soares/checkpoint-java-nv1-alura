package dev.suel.checkpointjavanv1alura.domain.entity.sala;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.validators.ISalaValidator;
import dev.suel.checkpointjavanv1alura.exception.IllegalActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    private SalaRepository salaRepository;
    private ISalaValidator v1;
    private ISalaValidator v2;

    private SalaService salaService;

    @BeforeEach
    void setUp() {
        salaRepository = mock(SalaRepository.class);
        v1 = mock(ISalaValidator.class);
        v2 = mock(ISalaValidator.class);

        salaService = new SalaService(salaRepository, List.of(v1, v2));
    }

    @Test
    void findById_quandoExiste_deveRetornarSala() {
        var sala = new Sala();
        sala.setId(1L);

        given(salaRepository.findById(1L)).willReturn(Optional.of(sala));

        var result = salaService.findById(1L);

        assertSame(sala, result);
        then(salaRepository).should().findById(1L);
    }

    @Test
    void findById_quandoNaoExiste_deveLancar() {
        given(salaRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> salaService.findById(99L));

        then(salaRepository).should().findById(99L);
    }

    @Test
    void registrarNovaSala_quandoNomeNaoExiste_deveValidarESalvar() {
        given(salaRepository.existsByNomeIgnoreCase("Sala A")).willReturn(false);

        var salvo = new Sala();
        salvo.setId(10L);

        ArgumentCaptor<Sala> captor = ArgumentCaptor.forClass(Sala.class);
        given(salaRepository.save(any(Sala.class))).willReturn(salvo);

        var result = salaService.registrarNovaSala("Sala A", 20);

        assertSame(salvo, result);
        then(salaRepository).should().existsByNomeIgnoreCase("Sala A");
        then(salaRepository).should().save(captor.capture());

        var criada = captor.getValue();
        assertEquals("Sala A", criada.getNome());
        assertEquals(20, criada.getCapacidade());

        then(v1).should().execute(criada);
        then(v2).should().execute(criada);
    }

    @Test
    void registrarNovaSala_quandoNomeComEspacos_deveVerificarComTrim() {
        given(salaRepository.existsByNomeIgnoreCase("Sala A")).willReturn(false);
        given(salaRepository.save(any(Sala.class))).willAnswer(inv -> inv.getArgument(0));

        salaService.registrarNovaSala("  Sala A  ", 10);

        then(salaRepository).should().existsByNomeIgnoreCase("Sala A");
        then(salaRepository).should().save(any(Sala.class));
    }

    @Test
    void registrarNovaSala_quandoNomeJaExiste_deveLancar() {
        given(salaRepository.existsByNomeIgnoreCase("Sala A")).willReturn(true);

        var ex = assertThrows(IllegalArgumentException.class, () -> salaService.registrarNovaSala("Sala A", 10));
        assertEquals("Já existe uma sala registrada com o mesmo nome.", ex.getMessage());

        then(salaRepository).should().existsByNomeIgnoreCase("Sala A");
        then(salaRepository).should(never()).save(any(Sala.class));
    }

    @Test
    void getAll_deveDelegarParaRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sala> slice = new PageImpl<>(List.of(new Sala()), pageable, 2);

        given(salaRepository.findAll(pageable)).willReturn(slice);

        var result = salaService.getAll(pageable);

        assertSame(slice, result);
        then(salaRepository).should().findAll(pageable);
    }

    @Test
    void deletarPeloId_quandoNaoReservada_deveDeletar() {
        var sala = mock(Sala.class);
        given(sala.isReservada()).willReturn(false);
        given(salaRepository.findById(1L)).willReturn(Optional.of(sala));

        salaService.deletarPeloId(1L);

        then(salaRepository).should().findById(1L);
        then(salaRepository).should().delete(sala);
    }

    @Test
    void deletarPeloId_quandoReservada_deveLancarENaoDeletar() {
        var sala = mock(Sala.class);
        given(sala.isReservada()).willReturn(true);
        given(salaRepository.findById(1L)).willReturn(Optional.of(sala));

        var ex = assertThrows(IllegalActionException.class, () -> salaService.deletarPeloId(1L));
        assertEquals("Você não pode realizar esta ação. A sala contém reservas ativas.", ex.getMessage());

        then(salaRepository).should().findById(1L);
        then(salaRepository).should(never()).delete(any(Sala.class));
    }

    @Test
    void deletarPeloId_quandoNaoExiste_deveLancar() {
        given(salaRepository.findById(404L)).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> salaService.deletarPeloId(404L));

        then(salaRepository).should().findById(404L);
        then(salaRepository).should(never()).delete(any(Sala.class));
    }
}
