package dev.suel.checkpointjavanv1alura.seeders;

import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import net.datafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeSalaFactory {

    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    private FakeSalaFactory() {}

    public static List<Sala> generate(int i) {
        return Stream.generate(FakeSalaFactory::createSala)
                .limit(i)
                .collect(Collectors.toList());
    }

    private static Sala createSala() {
        var sala = new Sala();
        sala.setNome( faker.movie().name() );
        sala.setCapacidade( faker.number().numberBetween(1, 20) );
        return sala;
    }

}
