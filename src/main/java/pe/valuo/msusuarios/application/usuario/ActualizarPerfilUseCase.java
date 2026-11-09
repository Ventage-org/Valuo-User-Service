package pe.valuo.msusuarios.application.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.application.usuario.command.ActualizarPerfilCommand;
import pe.valuo.msusuarios.application.usuario.result.PerfilResult;
import pe.valuo.msusuarios.domain.exception.DistritoNoEncontradoException;
import pe.valuo.msusuarios.domain.exception.UsuarioNoEncontradoException;
import pe.valuo.msusuarios.domain.model.usuario.Ruc;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.DistritoRepository;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;

/** La firma digital no se acepta aquí: solo cambia mediante {@link ActualizarFirmaDigitalUseCase}. */
@Service
@RequiredArgsConstructor
public class ActualizarPerfilUseCase {

    private final UsuarioRepository usuarioRepository;
    private final DistritoRepository distritoRepository;

    @Transactional
    public PerfilResult ejecutar(ActualizarPerfilCommand command) {
        Usuario usuario = usuarioRepository.buscarActivoPorId(command.usuarioId())
                .orElseThrow(UsuarioNoEncontradoException::new);

        Short distritoId = command.distritoEspecializacionId();
        if (distritoId != null && !distritoRepository.existeActivo(distritoId)) {
            throw new DistritoNoEncontradoException(distritoId);
        }

        usuario.actualizarPerfil(
                new Telefono(command.telefono()),
                Ruc.deTextoOpcional(command.ruc()),
                command.nombreAgencia(),
                distritoId);

        return PerfilResult.de(usuario);
    }
}
