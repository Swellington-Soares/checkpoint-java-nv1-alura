package dev.suel.checkpointjavanv1alura.domain.validators;

import java.util.regex.Pattern;

public final class PhoneNumberValidator {
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+[1-9][0-9]{11,12}$");

    private PhoneNumberValidator() { }

    public static boolean isValid(String phone) {
        if (phone == null || phone.isBlank())
            return false;

        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static void validateOrThrow(String phone) {
        if (!isValid(phone))
            throw new IllegalArgumentException("Número de telefone inválido. Use o formato internacional, exemplo: +5511999999999");
    }
}
