package pe.valuo.msusuarios.domain.exception;

public class UsuarioNoEncontradoException extends DomainException {

    public UsuarioNoEncontradoException() {
        super("Usuario no encontrado");
    }
}
