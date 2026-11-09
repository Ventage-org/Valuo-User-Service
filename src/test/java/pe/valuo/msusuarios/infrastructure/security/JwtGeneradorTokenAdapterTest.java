package pe.valuo.msusuarios.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import pe.valuo.msusuarios.application.port.GeneradorToken;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.infrastructure.config.ValuoProperties;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtGeneradorTokenAdapterTest {

    // Base64 de 32+ bytes: solo para pruebas.
    private static final String SECRETO = "dGVzdC1zZWNyZXQta2V5LXBhcmEtcHJ1ZWJhcy0zMi1ieXRlcy1taW5pbW8h";

    private final ValuoProperties properties = new ValuoProperties(
            new ValuoProperties.Jwt(SECRETO, 28_800_000L),
            new ValuoProperties.Storage("./uploads", "http://localhost:8081/uploads"),
            new ValuoProperties.Cors(List.of("http://localhost:4200")));

    @Test
    void tokenSeDecodificaConLosClaimsEsperados() {
        JwtConfig config = new JwtConfig();
        JwtGeneradorTokenAdapter adapter = new JwtGeneradorTokenAdapter(config.jwtEncoder(properties), properties);
        Usuario agente = Usuario.registrarAgente("Carlos", "Mendoza", new Email("cmendoza@agenciavaluo.pe"),
                new Telefono("987654321"), null, null, (short) 16, "hash");
        UUID id = UUID.randomUUID();
        ReflectionTestUtils.setField(agente, "id", id); // el id real lo asigna JPA al persistir

        GeneradorToken.TokenEmitido emitido = adapter.generar(agente);
        Jwt jwt = config.jwtDecoder(properties).decode(emitido.valor());

        assertThat(jwt.getSubject()).isEqualTo(id.toString());
        assertThat(emitido.expiracionMs()).isEqualTo(28_800_000L);
        assertThat(jwt.getHeaders().get("alg")).isEqualTo("HS256");
        assertThat(jwt.<String>getClaim("email")).isEqualTo("cmendoza@agenciavaluo.pe");
        assertThat(jwt.<String>getClaim("role")).isEqualTo("AGENTE");
        assertThat(jwt.<String>getClaim("nombreCompleto")).isEqualTo("Carlos Mendoza");
        assertThat(Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt())).isEqualTo(Duration.ofHours(8));
        assertThat(jwt.getExpiresAt()).isAfter(Instant.now());
    }
}
