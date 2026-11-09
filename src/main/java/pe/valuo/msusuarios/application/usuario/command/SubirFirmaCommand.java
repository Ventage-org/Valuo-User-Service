package pe.valuo.msusuarios.application.usuario.command;

import java.util.UUID;

public record SubirFirmaCommand(UUID usuarioId, String contentType, byte[] contenido) {
}
