package pe.valuo.msusuarios.domain.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;

import java.util.Locale;
import java.util.regex.Pattern;

/** Value object: correo normalizado a minúsculas. */
@Embeddable
public record Email(@Column(name = "email", nullable = false, length = 150) String valor) {

    private static final Pattern FORMATO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if (valor == null || valor.isBlank()) {
            throw new ValorInvalidoException("El correo electrónico es obligatorio");
        }
        valor = valor.trim().toLowerCase(Locale.ROOT);
        if (valor.length() > 150 || !FORMATO.matcher(valor).matches()) {
            throw new ValorInvalidoException("El correo electrónico no tiene un formato válido");
        }
    }
}
