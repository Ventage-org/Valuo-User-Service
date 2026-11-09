package pe.valuo.msusuarios.application.usuario.result;

import pe.valuo.msusuarios.domain.model.usuario.RolUsuario;

import java.util.UUID;

public record SesionResult(String token, long expiracionMs, UsuarioSesion usuario) {

    public record UsuarioSesion(UUID id, String nombre, String apellido, String email, RolUsuario rol) {
    }
}
