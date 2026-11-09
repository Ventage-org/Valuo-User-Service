package pe.valuo.msusuarios.domain.exception;

/** Mensaje deliberadamente genérico: no revela si falló el correo o la contraseña. */
public class CredencialesInvalidasException extends DomainException {

    public CredencialesInvalidasException() {
        super("Credenciales inválidas");
    }
}
