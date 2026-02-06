package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ReservaDataValidatorImplTest {
    @Mock
    private Reserva reserva;

    private final ReservaDataValidatorImpl validator = new ReservaDataValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoDataInicioForNull() {
        given(reserva.getDataInicio()).willReturn(null);
        given(reserva.getDataFim()).willReturn(LocalDateTime.now().plusDays(1));

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Data de início inválida.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoDataInicioForNoPassado() {
        var now = LocalDateTime.of(2026, 2, 5, 10, 0);

        given(reserva.getDataInicio()).willReturn(now.minusDays(1));
        given(reserva.getDataFim()).willReturn(now.plusDays(1));

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(now);

            var ex = assertThrows(IllegalArgumentException.class,
                    () -> validator.execute(reserva));

            assertEquals("Data de início inválida.", ex.getMessage());
        }
    }

    @Test
    void execute_deveLancarExcecao_quandoDataFimForNull() {
        given(reserva.getDataInicio()).willReturn(LocalDateTime.now().plusDays(1));
        given(reserva.getDataFim()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Data de fim inválida.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoDataFimForNoPassado() {
        var now = LocalDateTime.of(2026, 2, 5, 10, 0);

        given(reserva.getDataInicio()).willReturn(now.plusDays(2));
        given(reserva.getDataFim()).willReturn(now.minusDays(1));

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(now);

            var ex = assertThrows(IllegalArgumentException.class,
                    () -> validator.execute(reserva));

            assertEquals("Data de fim inválida.", ex.getMessage());
        }
    }

    @Test
    void execute_deveLancarExcecao_quandoDataInicioForDepoisDaDataFim() {
        var inicio = LocalDateTime.now().plusDays(5);
        var fim = LocalDateTime.now().plusDays(2);

        given(reserva.getDataInicio()).willReturn(inicio);
        given(reserva.getDataFim()).willReturn(fim);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Data início não pode ser depois do fim.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoDataFimForAntesDaDataInicio() {
        var inicio = LocalDateTime.now().plusDays(10);
        var fim = LocalDateTime.now().plusDays(5);

        given(reserva.getDataInicio()).willReturn(inicio);
        given(reserva.getDataFim()).willReturn(fim);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Data início não pode ser depois do fim.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoDatasForemValidas() {
        var inicio = LocalDateTime.now().plusDays(2);
        var fim = LocalDateTime.now().plusDays(5);

        given(reserva.getDataInicio()).willReturn(inicio);
        given(reserva.getDataFim()).willReturn(fim);

        assertDoesNotThrow(() -> validator.execute(reserva));
    }
}