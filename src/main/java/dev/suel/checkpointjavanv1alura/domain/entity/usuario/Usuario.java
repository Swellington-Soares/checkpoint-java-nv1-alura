package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas = new ArrayList<>();

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;


    public boolean isMaiorDeIdade() {
        return dataNascimento.isBefore(LocalDate.now().minusYears(18));
    }


    public String getNomeCompleto() {
        return this.nome + " " + this.sobrenome;
    }
}
