package pe.valuo.msusuarios.infrastructure.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import pe.valuo.msusuarios.infrastructure.config.ValuoProperties;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    JwtEncoder jwtEncoder(ValuoProperties properties) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(clave(properties)));
    }

    @Bean
    JwtDecoder jwtDecoder(ValuoProperties properties) {
        return NimbusJwtDecoder.withSecretKey(clave(properties)).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private SecretKey clave(ValuoProperties properties) {
        byte[] bytes = Base64.getDecoder().decode(properties.jwt().secret());
        if (bytes.length < 32) {
            throw new IllegalStateException("valuo.jwt.secret debe tener al menos 32 bytes (Base64)");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}
