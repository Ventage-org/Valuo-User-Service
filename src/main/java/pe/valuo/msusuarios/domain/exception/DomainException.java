package pe.valuo.msusuarios.domain.exception;

/** Base de todas las violaciones de reglas de negocio del bounded context Identidad y Acceso. */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String mensaje) {
        super(mensaje);
    }
}
