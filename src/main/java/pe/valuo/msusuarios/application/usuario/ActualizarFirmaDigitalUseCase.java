package pe.valuo.msusuarios.application.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.application.exception.ArchivoInvalidoException;
import pe.valuo.msusuarios.application.port.AlmacenamientoArchivos;
import pe.valuo.msusuarios.application.usuario.command.SubirFirmaCommand;
import pe.valuo.msusuarios.domain.exception.UsuarioNoEncontradoException;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActualizarFirmaDigitalUseCase {

    private static final int TAMANO_MAXIMO_BYTES = 2 * 1024 * 1024;

    private final UsuarioRepository usuarioRepository;
    private final AlmacenamientoArchivos almacenamientoArchivos;

    @Transactional
    public String ejecutar(SubirFirmaCommand command) {
        Usuario usuario = usuarioRepository.buscarActivoPorId(command.usuarioId())
                .orElseThrow(UsuarioNoEncontradoException::new);

        String extension = validarImagen(command);
        String ruta = "firmas/" + usuario.getId() + "-" + UUID.randomUUID() + "." + extension;

        String urlAnterior = usuario.getUrlFirmaDigital();
        String urlNueva = almacenamientoArchivos.guardar(ruta, command.contenido());
        usuario.cambiarFirmaDigital(urlNueva);

        if (urlAnterior != null) {
            almacenamientoArchivos.eliminar(urlAnterior);
        }
        return urlNueva;
    }

    /** Valida tamaño, content-type declarado y firma binaria real. Devuelve la extensión a usar. */
    private String validarImagen(SubirFirmaCommand command) {
        byte[] bytes = command.contenido();
        if (bytes == null || bytes.length == 0) {
            throw new ArchivoInvalidoException("El archivo está vacío");
        }
        if (bytes.length > TAMANO_MAXIMO_BYTES) {
            throw new ArchivoInvalidoException("El archivo supera el tamaño máximo de 2 MB");
        }
        String tipo = command.contentType();
        if ("image/png".equals(tipo) && esPng(bytes)) {
            return "png";
        }
        if ("image/jpeg".equals(tipo) && esJpeg(bytes)) {
            return "jpg";
        }
        throw new ArchivoInvalidoException("Solo se permiten imágenes PNG o JPEG válidas");
    }

    private boolean esPng(byte[] b) {
        return b.length > 8 && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G';
    }

    private boolean esJpeg(byte[] b) {
        return b.length > 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
    }
}
