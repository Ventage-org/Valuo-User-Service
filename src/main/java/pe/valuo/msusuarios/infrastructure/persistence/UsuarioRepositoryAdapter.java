package pe.valuo.msusuarios.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final SpringDataUsuarioRepository jpa;

    @Override
    public Usuario guardar(Usuario usuario) {
        return jpa.save(usuario);
    }

    @Override
    public boolean existePorEmail(Email email) {
        return jpa.existsByEmailValor(email.valor());
    }

    @Override
    public Optional<Usuario> buscarActivoPorEmail(Email email) {
        return jpa.findByEmailValorAndActivoTrue(email.valor());
    }

    @Override
    public Optional<Usuario> buscarActivoPorId(UUID id) {
        return jpa.findByIdAndActivoTrue(id);
    }
}
