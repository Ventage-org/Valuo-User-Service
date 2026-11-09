package pe.valuo.msusuarios.domain.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;

import java.util.regex.Pattern;

/** Value object: RUC de 11 dígitos que inicia con 10 o 20. */
@Embeddable
public record Ruc(@Column(name = "ruc", length = 11) String valor) {

    public static final String REGEX = "^(10|20)[0-9]{9}$";
    private static final Pattern FORMATO = Pattern.compile(REGEX);

    public Ruc {
        if (valor == null || !FORMATO.matcher(valor.trim()).matches()) {
            throw new ValorInvalidoException("El RUC debe tener 11 dígitos y comenzar con 10 o 20");
        }
        valor = valor.trim();
    }

    /** El RUC es opcional: vacío o nulo equivale a "sin RUC". */
    public static Ruc deTextoOpcional(String texto) {
        return (texto == null || texto.isBlank()) ? null : new Ruc(texto);
    }
}
