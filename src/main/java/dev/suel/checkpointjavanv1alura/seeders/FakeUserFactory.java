package dev.suel.checkpointjavanv1alura.seeders;

import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import net.datafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeUserFactory {

    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    public static List<Usuario> generate(int i) {
        return Stream.generate(FakeUserFactory::createUser).limit(i).collect(Collectors.toList());
    }

    private static Usuario createUser() {
        var usuario = new Usuario();
        usuario.setEmail(faker.internet().emailAddress());
        usuario.setTelefone(faker.phoneNumber().phoneNumberInternational());
        usuario.setSobrenome(faker.name().lastName());
        usuario.setNome(faker.name().firstName());
        usuario.setCpf(faker.cpf().valid());
        usuario.setDataNascimento(faker.timeAndDate().birthday(18, 35));
        return usuario;
    }
}
