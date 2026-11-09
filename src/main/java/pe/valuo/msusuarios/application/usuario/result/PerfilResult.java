package pe.valuo.msusuarios.application.usuario.result;

import pe.valuo.msusuarios.domain.model.usuario.Usuario;

import java.util.UUID;

public record PerfilResult(
        UUID id,
        String email,
        String nombre,
        String apellido,
        String telefono,
        String ruc,
        String nombreAgencia,
        Short distritoEspecializacionId,
        String urlFirmaDigital,
        String urlFotoPerfil) {

    public static PerfilResult de(Usuario usuario) {
        return new PerfilResult(
                usuario.getId(),
                usuario.getEmail().valor(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getTelefono().valor(),
                usuario.getRuc() == null ? null : usuario.getRuc().valor(),
                usuario.getNombreAgencia(),
                usuario.getDistritoEspecializacionId(),
                usuario.getUrlFirmaDigital(),
                usuario.getUrlFotoPerfil());
    }
}
