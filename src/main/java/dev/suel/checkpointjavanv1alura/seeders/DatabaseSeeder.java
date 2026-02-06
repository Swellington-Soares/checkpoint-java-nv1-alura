package dev.suel.checkpointjavanv1alura.seeders;

import dev.suel.checkpointjavanv1alura.domain.entity.reserva.Reserva;
import dev.suel.checkpointjavanv1alura.domain.entity.reserva.ReservaRepository;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.Sala;
import dev.suel.checkpointjavanv1alura.domain.entity.sala.SalaRepository;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.Usuario;
import dev.suel.checkpointjavanv1alura.domain.entity.usuario.UsuarioRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Profile("dev")
@RequiredArgsConstructor
@Service
public class DatabaseSeeder implements ApplicationRunner {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if (!args.containsOption("seed:db")) return;
        Random random = new Random(Instant.now().getEpochSecond());
        reservaRepository.deleteAll();
        salaRepository.deleteAll();
        usuarioRepository.deleteAll();

        List<Usuario> fakeUsers = FakeUserFactory.generate(50);
        List<Sala> fakeSalas = FakeSalaFactory.generate(20);

        fakeSalas.forEach(sala -> {
            try {
                salaRepository.save(sala);
            } catch (Exception ignored) {}
        });

        fakeUsers.forEach(user -> {
            try {
                usuarioRepository.save(user);
            } catch (Exception ignored) {
            }
        });


        List<Reserva> reservas = FakeReservaFactory.generate(200);
        for (Reserva reserva : reservas) {
            try {
                reserva.setUsuario(fakeUsers.get(random.nextInt(fakeUsers.size())));
                reserva.setSala(fakeSalas.get(random.nextInt(fakeSalas.size())));
                reservaRepository.save(reserva);
            } catch (Exception ignored) {}
        }

    }
}
