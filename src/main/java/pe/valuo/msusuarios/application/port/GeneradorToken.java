package pe.valuo.msusuarios.application.port;

import pe.valuo.msusuarios.domain.model.usuario.Usuario;

/** Puerto de salida: emite el token de sesión del usuario autenticado. */
public interface GeneradorToken {

    TokenEmitido generar(Usuario usuario);

    record TokenEmitido(String valor, long expiracionMs) {
    }
}
