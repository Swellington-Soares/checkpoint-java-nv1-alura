package dev.suel.checkpointjavanv1alura.seeders;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.SituacaoReserva;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeReservaFactory {

    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    public static List<Reserva> generate(int i) {
        return Stream.generate(FakeReservaFactory::createReserva)
                .limit(i)
                .collect(Collectors.toList());
    }

    private static Reserva createReserva() {
        var reserva = Reserva.withId();
        reserva.setDataInicio(
                LocalDateTime.ofEpochSecond( faker.timeAndDate().future().getEpochSecond(), 0,
                        ZoneOffset.ofHours(-3)
                ));
        reserva.setDataFim( reserva.getDataInicio().plusMonths(
                faker.number().numberBetween( 1, 6 ) ) );
        reserva.setSituacao( faker.options().option(SituacaoReserva.class) );

        return reserva;
    }


}
