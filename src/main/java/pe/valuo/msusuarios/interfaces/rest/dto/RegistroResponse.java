package pe.valuo.msusuarios.interfaces.rest.dto;

import java.util.UUID;

public record RegistroResponse(String mensaje, UUID usuarioId) {
}
