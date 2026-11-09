package pe.valuo.msusuarios.application.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.application.usuario.result.PerfilResult;
import pe.valuo.msusuarios.domain.exception.UsuarioNoEncontradoException;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultarPerfilUseCase {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public PerfilResult ejecutar(UUID usuarioId) {
        return usuarioRepository.buscarActivoPorId(usuarioId)
                .map(PerfilResult::de)
                .orElseThrow(UsuarioNoEncontradoException::new);
    }
}
