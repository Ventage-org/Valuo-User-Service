package pe.valuo.msusuarios.application.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.application.usuario.command.RegistrarAgenteCommand;
import pe.valuo.msusuarios.domain.exception.DistritoNoEncontradoException;
import pe.valuo.msusuarios.domain.exception.EmailYaRegistradoException;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Ruc;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.DistritoRepository;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;
import pe.valuo.msusuarios.domain.service.CodificadorContrasena;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrarAgenteUseCase {

    private final UsuarioRepository usuarioRepository;
    private final DistritoRepository distritoRepository;
    private final CodificadorContrasena codificadorContrasena;

    @Transactional
    public UUID ejecutar(RegistrarAgenteCommand command) {
        Email email = new Email(command.email());
        Telefono telefono = new Telefono(command.telefono());
        Ruc ruc = Ruc.deTextoOpcional(command.ruc());

        if (usuarioRepository.existePorEmail(email)) {
            throw new EmailYaRegistradoException();
        }
        Short distritoId = command.distritoEspecializacionId();
        if (distritoId != null && !distritoRepository.existeActivo(distritoId)) {
            throw new DistritoNoEncontradoException(distritoId);
        }

        Usuario agente = Usuario.registrarAgente(
                command.nombre().trim(),
                command.apellido().trim(),
                email,
                telefono,
                ruc,
                command.nombreAgencia(),
                distritoId,
                codificadorContrasena.codificar(command.contrasena()));

        return usuarioRepository.guardar(agente).getId();
    }
}
