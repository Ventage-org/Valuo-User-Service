package pe.valuo.msusuarios.application.usuario.command;

import java.util.UUID;

public record ActualizarPerfilCommand(
        UUID usuarioId,
        String telefono,
        String ruc,
        String nombreAgencia,
        Short distritoEspecializacionId) {
}
