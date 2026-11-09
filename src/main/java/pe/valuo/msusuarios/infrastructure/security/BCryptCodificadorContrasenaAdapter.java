package pe.valuo.msusuarios.infrastructure.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.valuo.msusuarios.domain.service.CodificadorContrasena;

@Component
class BCryptCodificadorContrasenaAdapter implements CodificadorContrasena {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String codificar(String contrasenaPlana) {
        return encoder.encode(contrasenaPlana);
    }

    @Override
    public boolean coincide(String contrasenaPlana, String hash) {
        return encoder.matches(contrasenaPlana, hash);
    }
}
