package pe.valuo.msusuarios.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.valuo.msusuarios.domain.model.usuario.Ruc;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;

public record RegistroRequest(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 80) String apellido,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Pattern(regexp = Telefono.REGEX, message = "debe ser un celular de 9 dígitos que inicia con 9") String telefono,
        @NotBlank @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "debe tener mínimo 8 caracteres, 1 mayúscula y 1 número") String password,
        @Pattern(regexp = "^$|" + Ruc.REGEX, message = "debe tener 11 dígitos y comenzar con 10 o 20") String ruc,
        @Size(max = 120) String nombreAgencia,
        Short distritoEspecializacionId) {
}
