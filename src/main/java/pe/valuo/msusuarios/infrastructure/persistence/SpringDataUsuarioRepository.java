package pe.valuo.msusuarios.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;

import java.util.Optional;
import java.util.UUID;

interface SpringDataUsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmailValor(String email);

    Optional<Usuario> findByEmailValorAndActivoTrue(String email);

    Optional<Usuario> findByIdAndActivoTrue(UUID id);
}
