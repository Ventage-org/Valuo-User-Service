package pe.valuo.msusuarios.application.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.application.port.GeneradorToken;
import pe.valuo.msusuarios.application.usuario.command.IniciarSesionCommand;
import pe.valuo.msusuarios.application.usuario.result.SesionResult;
import pe.valuo.msusuarios.domain.exception.CredencialesInvalidasException;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;
import pe.valuo.msusuarios.domain.service.CodificadorContrasena;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IniciarSesionUseCase {

    private final UsuarioRepository usuarioRepository;
    private final CodificadorContrasena codificadorContrasena;
    private final GeneradorToken generadorToken;

    @Transactional(readOnly = true)
    public SesionResult ejecutar(IniciarSesionCommand command) {
        Usuario usuario = buscarUsuario(command.email())
                .filter(u -> codificadorContrasena.coincide(command.contrasena(), u.getPasswordHash()))
                .orElseThrow(CredencialesInvalidasException::new);

        GeneradorToken.TokenEmitido token = generadorToken.generar(usuario);
        return new SesionResult(
                token.valor(),
                token.expiracionMs(),
                new SesionResult.UsuarioSesion(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getEmail().valor(),
                        usuario.getRol()));
    }

    /** Un correo mal formado se trata igual que uno inexistente: 401 genérico, no 400. */
    private Optional<Usuario> buscarUsuario(String correo) {
        try {
            return usuarioRepository.buscarActivoPorEmail(new Email(correo));
        } catch (ValorInvalidoException e) {
            return Optional.empty();
        }
    }
}
