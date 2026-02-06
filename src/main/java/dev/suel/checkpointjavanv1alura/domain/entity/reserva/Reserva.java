package dev.suel.checkpointjavanv1alura.domain.entity.reserva;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.exception.CancelamentoDeReservaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reserva {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoReserva situacao;

    @Column
    private LocalDateTime dataCancelamento;


    private String motivoCancelamento;


    public static Reserva withId() {
        var reserva = new Reserva();
        reserva.setId(UUID.randomUUID());
        return reserva;
    }

    public void cancelar() {
       if (!situacao.equals(SituacaoReserva.ATIVA))
           throw new CancelamentoDeReservaException("A reserva não se encontra ativa.");

       if (LocalDateTime.now().isAfter(dataInicio.plusDays(3)))
           throw new CancelamentoDeReservaException("O cancelamento da reserva é permitido somente até 3 dias após a data de início.");

        this.dataCancelamento = LocalDateTime.now();
        this.situacao = SituacaoReserva.CANCELADA;
    }

    public void cancelar(String motivoCancelamento) {
        this.cancelar();
        this.motivoCancelamento = motivoCancelamento;
    }
}
