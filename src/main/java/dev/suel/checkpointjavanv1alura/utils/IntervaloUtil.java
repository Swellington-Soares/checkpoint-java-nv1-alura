package dev.suel.checkpointjavanv1alura.utils;

import java.time.LocalDateTime;

public class IntervaloUtil {

    private IntervaloUtil() {}

    public static boolean estaDentro(LocalDateTime data,
                                     LocalDateTime inicio,
                                     LocalDateTime fim) {

        return !data.isBefore(inicio) && !data.isAfter(fim);
    }
}
