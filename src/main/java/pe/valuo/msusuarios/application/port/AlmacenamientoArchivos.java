package pe.valuo.msusuarios.application.port;

/** Puerto de salida: guarda archivos públicos (firmas) y devuelve su URL. */
public interface AlmacenamientoArchivos {

    /** @param rutaRelativa ruta dentro del almacenamiento, p. ej. {@code firmas/abc.png} */
    String guardar(String rutaRelativa, byte[] contenido);

    /** Elimina el archivo si la URL pertenece a este almacenamiento; ignora cualquier otra. */
    void eliminar(String url);
}
