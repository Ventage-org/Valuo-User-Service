package pe.valuo.msusuarios.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pe.valuo.msusuarios.application.port.GeneradorToken;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.infrastructure.config.ValuoProperties;

import java.time.Instant;

@Component
@RequiredArgsConstructor
class JwtGeneradorTokenAdapter implements GeneradorToken {

    private final JwtEncoder jwtEncoder;
    private final ValuoProperties properties;

    @Override
    public TokenEmitido generar(Usuario usuario) {
        long expiracionMs = properties.jwt().expiracionMs();
        Instant ahora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(usuario.getId().toString())
                .issuedAt(ahora)
                .expiresAt(ahora.plusMillis(expiracionMs))
                .claim("email", usuario.getEmail().valor())
                .claim("role", usuario.getRol().name())
                .claim("nombreCompleto", usuario.nombreCompleto())
                .build();

        // Sin cabecera explícita Nimbus intentaría RS256 y fallaría con una clave HMAC.
        JwsHeader cabecera = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(cabecera, claims)).getTokenValue();
        return new TokenEmitido(token, expiracionMs);
    }
}
