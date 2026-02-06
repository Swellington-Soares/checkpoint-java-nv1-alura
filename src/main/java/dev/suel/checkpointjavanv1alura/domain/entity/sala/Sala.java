package dev.suel.checkpointjavanv1alura.domain.entity.sala;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;
import dev.suel.checkpointjavanv1alura.utils.IntervaloUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private int capacidade;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private LocalDateTime dataRegistro;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "sala")
    private List<Reserva> reservas = new ArrayList<>();

    @Generated
    public boolean isReservada() {
        return !reservas.isEmpty() &&
                reservas.stream().anyMatch(
                        reserva -> reserva.getSituacao() == SituacaoReserva.ATIVA &&
                                IntervaloUtil.estaDentro(LocalDateTime.now(),
                                        reserva.getDataInicio(),
                                        reserva.getDataFim())
                );
    }

    private void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
        reserva.setSala(this);
    }

    public void removerReserva(Reserva reserva) {
        reservas.remove(reserva);
        reserva.setSala(null);
    }

    @Generated
    @PrePersist
    void prePersist() {
        dataRegistro = LocalDateTime.now();
        nome = nome.trim().toUpperCase();
    }

}
