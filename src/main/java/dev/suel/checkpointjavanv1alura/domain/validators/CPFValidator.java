package dev.suel.checkpointjavanv1alura.domain.validators;

public final class CPFValidator {

    private CPFValidator() { }

    public static boolean isValid(String cpf) {
        if (cpf == null)
            return false;

        String digits = onlyDigits(cpf);

        if (digits.length() != 11)
            return false;

        if (allSameDigits(digits))
            return false;

        int d1 = calcDigit(digits, 9);
        int d2 = calcDigit(digits, 10);

        return digits.charAt(9) == (char) ('0' + d1)
                && digits.charAt(10) == (char) ('0' + d2);
    }

    public static void validateOrThrow(String cpf) {
        if (!isValid(cpf))
            throw new IllegalArgumentException("CPF inválido.");
    }

    private static String onlyDigits(String value) {
        StringBuilder sb = new StringBuilder(11);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c >= '0' && c <= '9')
                sb.append(c);
        }
        return sb.toString();
    }

    private static boolean allSameDigits(String digits) {
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first)
                return false;
        }
        return true;
    }

    private static int calcDigit(String digits, int baseLength) {
        int sum = 0;
        int weight = baseLength + 1;

        for (int i = 0; i < baseLength; i++) {
            int num = digits.charAt(i) - '0';
            sum += num * (weight - i);
        }

        int mod = sum % 11;
        return (mod < 2) ? 0 : (11 - mod);
    }


}
