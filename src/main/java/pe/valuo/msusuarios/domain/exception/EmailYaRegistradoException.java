package pe.valuo.msusuarios.domain.exception;

public class EmailYaRegistradoException extends DomainException {

    public EmailYaRegistradoException() {
        super("El correo electrónico ya está registrado");
    }
}
