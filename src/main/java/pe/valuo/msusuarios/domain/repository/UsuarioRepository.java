package pe.valuo.msusuarios.domain.repository;

import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    boolean existePorEmail(Email email);

    Optional<Usuario> buscarActivoPorEmail(Email email);

    Optional<Usuario> buscarActivoPorId(UUID id);
}
