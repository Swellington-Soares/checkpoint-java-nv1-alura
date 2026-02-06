package dev.suel.checkpointjavanv1alura.domain.entity.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCpfOrEmailAllIgnoreCase(String cpf, String email);
}