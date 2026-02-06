package dev.suel.checkpointjavanv1alura.utils;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class FormatterUtil {

    private FormatterUtil() {}

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatarDataHora(TemporalAccessor accessor) {
        return formatter.format(accessor);
    }

    public static String formatarDataNascimento(TemporalAccessor accessor) {
        return formatter2.format(accessor);
    }

    public static String formatarCPF(String cpf) {
        if (cpf == null)
            throw new IllegalArgumentException("CPF não pode ser nulo.");

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11)
            throw new IllegalArgumentException("CPF deve conter 11 dígitos.");

        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);

    }

    public static String formatarNumeroTelefone(String valor) {
        if (valor == null)
            throw new IllegalArgumentException("Telefone não pode ser null.");

        var telefone = valor.replaceAll("\\D", "");

        /**
         * 5 0
         * 5 1
         * 8 2
         * 1 3
         * 9 4
         * 9 5
         * 4 6
         * 7 7
         * 7 8
         * 6 9
         * 3 10
         * 9 11
         * 5 12
         */

        if (telefone.length() == 13) {
            return new StringBuilder("+")
                    .append(telefone, 0, 2)
                    .append(" (")
                    .append(telefone, 2, 4)
                    .append(") ")
                    .append(telefone, 4, 5)
                    .append(" ")
                    .append(telefone, 5, 9)
                    .append(" ")
                    .append(telefone, 9, 13)
                    .toString();
        }

        if (telefone.length() == 12) {
            return new StringBuilder("+")
                    .append(telefone, 0, 2)
                    .append(" (")
                    .append(telefone, 2, 4)
                    .append(") ")
                    .append(telefone, 4, 8)
                    .append(" ")
                    .append(telefone, 8, 12)
                    .toString();
        }

        throw new IllegalArgumentException("Telefone deve ter 12 e 14 dígitos.");
    }

}
