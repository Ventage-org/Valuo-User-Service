package pe.valuo.msusuarios.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "valuo")
public record ValuoProperties(Jwt jwt, Storage storage, Cors cors) {

    /** @param secret clave HMAC en Base64 (mínimo 32 bytes) */
    public record Jwt(String secret, long expiracionMs) {
    }

    public record Storage(String directorio, String urlBase) {
    }

    public record Cors(List<String> origins) {
    }
}
