package pe.valuo.msusuarios.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.valuo.msusuarios.domain.model.usuario.Ruc;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;

/** La firma digital no se actualiza aquí: se sube con POST /perfil/firma. */
public record ActualizarPerfilRequest(
        @NotBlank @Pattern(regexp = Telefono.REGEX, message = "debe ser un celular de 9 dígitos que inicia con 9") String telefono,
        @Pattern(regexp = "^$|" + Ruc.REGEX, message = "debe tener 11 dígitos y comenzar con 10 o 20") String ruc,
        @Size(max = 120) String nombreAgencia,
        Short distritoEspecializacionId) {
}
