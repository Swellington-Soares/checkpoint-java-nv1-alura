package dev.suel.checkpointjavanv1alura.web.usuario.data;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioCadastroData(
        @CPF(message = "CPF Inválido.")
        @NotBlank(message = "CPF é obrigatório.")
        String cpf,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 20, message = "O nome não pode ultrapassar 20 caracteres.")
        String nome,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Size(max = 20, message = "O sobrenome não pode ultrapassar 20 caracteres.")
        String sobrenome,

        @Past(message = "A data de nascimento é inválida.")
        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @NotBlank(message = "Número de telefone é obrigatório.")
        @Pattern(regexp = "^\\+[1-9][0-9]{7,15}$",
                message = "Telefone inválido. Deve ser no formato internacional. +{código do país}{ddd}número. Exemplo: +551191111111")
        String telefone
) {
}
