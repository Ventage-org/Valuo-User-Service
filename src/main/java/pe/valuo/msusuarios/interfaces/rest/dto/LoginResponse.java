package pe.valuo.msusuarios.interfaces.rest.dto;

import java.util.UUID;

public record LoginResponse(String token, String tipo, long expiracionMs, UsuarioResumen usuario) {

    public record UsuarioResumen(UUID id, String nombre, String apellido, String email, String rol) {
    }
}
