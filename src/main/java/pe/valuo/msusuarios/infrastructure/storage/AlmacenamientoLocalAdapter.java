package pe.valuo.msusuarios.infrastructure.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.valuo.msusuarios.application.port.AlmacenamientoArchivos;
import pe.valuo.msusuarios.infrastructure.config.ValuoProperties;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Almacenamiento en disco local para desarrollo. Reemplazable por Cloudinary/Azure con otro adaptador. */
@Slf4j
@Component
class AlmacenamientoLocalAdapter implements AlmacenamientoArchivos {

    private final Path raiz;
    private final String urlBase;

    AlmacenamientoLocalAdapter(ValuoProperties properties) {
        this.raiz = Path.of(properties.storage().directorio()).toAbsolutePath().normalize();
        String base = properties.storage().urlBase();
        this.urlBase = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    @Override
    public String guardar(String rutaRelativa, byte[] contenido) {
        Path destino = resolverSeguro(rutaRelativa);
        try {
            Files.createDirectories(destino.getParent());
            Files.write(destino, contenido);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo", e);
        }
        return urlBase + "/" + rutaRelativa;
    }

    @Override
    public void eliminar(String url) {
        String prefijo = urlBase + "/";
        if (url == null || !url.startsWith(prefijo)) {
            return;
        }
        try {
            Files.deleteIfExists(resolverSeguro(url.substring(prefijo.length())));
        } catch (IOException | IllegalArgumentException e) {
            log.warn("No se pudo eliminar el archivo anterior: {}", e.getMessage());
        }
    }

    /** Evita path traversal: el destino siempre debe quedar dentro del directorio raíz. */
    private Path resolverSeguro(String rutaRelativa) {
        Path destino = raiz.resolve(rutaRelativa).normalize();
        if (!destino.startsWith(raiz)) {
            throw new IllegalArgumentException("Ruta de archivo no permitida");
        }
        return destino;
    }
}
