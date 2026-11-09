package pe.valuo.msusuarios.application.usuario.command;

public record RegistrarAgenteCommand(
        String nombre,
        String apellido,
        String email,
        String telefono,
        String contrasena,
        String ruc,
        String nombreAgencia,
        Short distritoEspecializacionId) {
}
