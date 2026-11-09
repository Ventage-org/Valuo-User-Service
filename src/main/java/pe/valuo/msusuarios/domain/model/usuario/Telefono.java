package pe.valuo.msusuarios.domain.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;

import java.util.regex.Pattern;

/** Value object: celular peruano de 9 dígitos que inicia con 9. */
@Embeddable
public record Telefono(@Column(name = "telefono", nullable = false, length = 9) String valor) {

    public static final String REGEX = "^9[0-9]{8}$";
    private static final Pattern FORMATO = Pattern.compile(REGEX);

    public Telefono {
        if (valor == null || !FORMATO.matcher(valor.trim()).matches()) {
            throw new ValorInvalidoException("El teléfono debe ser un celular peruano de 9 dígitos que inicia con 9");
        }
        valor = valor.trim();
    }
}
