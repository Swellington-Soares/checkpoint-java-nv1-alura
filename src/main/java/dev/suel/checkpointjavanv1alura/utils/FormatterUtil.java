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

        if (telefone.length() == 13) {
            return "+" +
                    telefone.substring(0, 2) +
                    " (" +
                    telefone.substring(2, 4) +
                    ") " +
                    telefone.charAt(4) +
                    " " +
                    telefone.substring(5, 9) +
                    " " +
                    telefone.substring(9, 13);
        }

        if (telefone.length() == 12) {
            return "+" +
                    telefone.substring(0, 2) +
                    " (" +
                    telefone.substring(2, 4) +
                    ") " +
                    telefone.substring(4, 8) +
                    " " +
                    telefone.substring(8, 12);
        }

        throw new IllegalArgumentException("Telefone deve ter 12 e 14 dígitos.");
    }

}
