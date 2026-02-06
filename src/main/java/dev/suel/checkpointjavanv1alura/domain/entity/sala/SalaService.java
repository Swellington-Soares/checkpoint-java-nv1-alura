package dev.suel.checkpointjavanv1alura.domain.entity.sala;


import dev.suel.checkpointjavanv1alura.domain.entity.sala.validators.ISalaValidator;
import dev.suel.checkpointjavanv1alura.exception.IllegalActionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaService {

    private final SalaRepository salaRepository;
    private final List<ISalaValidator> salaValidatorList;

    public Sala findById(Long salaId) {
        return salaRepository.findById(salaId).orElseThrow();
    }

    @Transactional
    public Sala registrarNovaSala(
            String nome,
            int capacidade
    ) {

        Sala sala = new Sala();
        sala.setNome(nome);
        sala.setCapacidade(capacidade);

        salaValidatorList.forEach(validator -> validator.execute(sala));

        if (salaRepository.existsByNomeIgnoreCase(sala.getNome().trim()))
            throw new IllegalArgumentException("Já existe uma sala registrada com o mesmo nome.");

        return salaRepository.save(sala);
    }

    @Transactional(readOnly = true)
    public Page<Sala> getAll(Pageable pageable) {
        return salaRepository.findAll(pageable);
    }

    @Transactional
    public void deletarPeloId(Long id) {
        Sala sala = salaRepository.findById(id).orElseThrow();
        if (sala.isReservada())
            throw new IllegalActionException("Você não pode realizar esta ação. A sala contém reservas ativas.");

        salaRepository.delete(sala);
    }
}
