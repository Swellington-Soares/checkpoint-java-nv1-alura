package dev.suel.checkpointjavanv1alura.domain.entity.reserva.validators;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservaSalaValidatorImplTest {
    @Mock
    private Reserva reserva;

    @Mock
    private Sala sala;

    private final ReservaSalaValidatorImpl validator = new ReservaSalaValidatorImpl();

    @Test
    void execute_deveLancarExcecao_quandoSalaForNull() {
        given(reserva.getSala()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Sala inválida ou não foi cadastrada corretamente.", ex.getMessage());
    }

    @Test
    void execute_deveLancarExcecao_quandoSalaIdForNull() {
        given(reserva.getSala()).willReturn(sala);
        given(sala.getId()).willReturn(null);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> validator.execute(reserva));

        assertEquals("Sala inválida ou não foi cadastrada corretamente.", ex.getMessage());
    }

    @Test
    void execute_naoDeveLancarExcecao_quandoSalaForValida() {
        given(reserva.getSala()).willReturn(sala);
        given(sala.getId()).willReturn(1L);

        assertDoesNotThrow(() -> validator.execute(reserva));
    }
}