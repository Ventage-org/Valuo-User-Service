package pe.valuo.msusuarios.domain.service;

/** Puerto de dominio para el hashing de contraseñas; la implementación vive en infraestructura. */
public interface CodificadorContrasena {

    String codificar(String contrasenaPlana);

    boolean coincide(String contrasenaPlana, String hash);
}
