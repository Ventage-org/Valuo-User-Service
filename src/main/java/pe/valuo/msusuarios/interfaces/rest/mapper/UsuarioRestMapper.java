package pe.valuo.msusuarios.interfaces.rest.mapper;

import pe.valuo.msusuarios.application.usuario.command.ActualizarPerfilCommand;
import pe.valuo.msusuarios.application.usuario.command.IniciarSesionCommand;
import pe.valuo.msusuarios.application.usuario.command.RegistrarAgenteCommand;
import pe.valuo.msusuarios.application.usuario.result.SesionResult;
import pe.valuo.msusuarios.interfaces.rest.dto.ActualizarPerfilRequest;
import pe.valuo.msusuarios.interfaces.rest.dto.LoginRequest;
import pe.valuo.msusuarios.interfaces.rest.dto.LoginResponse;
import pe.valuo.msusuarios.interfaces.rest.dto.RegistroRequest;

import java.util.UUID;

public final class UsuarioRestMapper {

    private UsuarioRestMapper() {
    }

    public static RegistrarAgenteCommand aCommand(RegistroRequest request) {
        return new RegistrarAgenteCommand(
                request.nombre(),
                request.apellido(),
                request.email(),
                request.telefono(),
                request.password(),
                request.ruc(),
                request.nombreAgencia(),
                request.distritoEspecializacionId());
    }

    public static IniciarSesionCommand aCommand(LoginRequest request) {
        return new IniciarSesionCommand(request.email(), request.password());
    }

    public static ActualizarPerfilCommand aCommand(UUID usuarioId, ActualizarPerfilRequest request) {
        return new ActualizarPerfilCommand(
                usuarioId,
                request.telefono(),
                request.ruc(),
                request.nombreAgencia(),
                request.distritoEspecializacionId());
    }

    public static LoginResponse aResponse(SesionResult sesion) {
        SesionResult.UsuarioSesion u = sesion.usuario();
        return new LoginResponse(
                sesion.token(),
                "Bearer",
                sesion.expiracionMs(),
                new LoginResponse.UsuarioResumen(u.id(), u.nombre(), u.apellido(), u.email(), u.rol().name()));
    }
}
