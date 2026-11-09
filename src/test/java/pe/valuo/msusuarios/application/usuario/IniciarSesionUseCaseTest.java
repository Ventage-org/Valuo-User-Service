package pe.valuo.msusuarios.application.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.valuo.msusuarios.application.port.GeneradorToken;
import pe.valuo.msusuarios.application.usuario.command.IniciarSesionCommand;
import pe.valuo.msusuarios.application.usuario.result.SesionResult;
import pe.valuo.msusuarios.domain.exception.CredencialesInvalidasException;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.Telefono;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;
import pe.valuo.msusuarios.domain.service.CodificadorContrasena;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IniciarSesionUseCaseTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock CodificadorContrasena codificadorContrasena;
    @Mock GeneradorToken generadorToken;

    IniciarSesionUseCase useCase;
    Usuario agente;

    @BeforeEach
    void preparar() {
        useCase = new IniciarSesionUseCase(usuarioRepository, codificadorContrasena, generadorToken);
        agente = Usuario.registrarAgente("Carlos", "Mendoza", new Email("cmendoza@agenciavaluo.pe"),
                new Telefono("987654321"), null, null, (short) 16, "hash");
    }

    @Test
    void credencialesCorrectasDevuelvenSesionConToken() {
        when(usuarioRepository.buscarActivoPorEmail(any(Email.class))).thenReturn(Optional.of(agente));
        when(codificadorContrasena.coincide("Password123!", "hash")).thenReturn(true);
        when(generadorToken.generar(agente)).thenReturn(new GeneradorToken.TokenEmitido("jwt", 28_800_000L));

        SesionResult sesion = useCase.ejecutar(new IniciarSesionCommand("cmendoza@agenciavaluo.pe", "Password123!"));

        assertThat(sesion.token()).isEqualTo("jwt");
        assertThat(sesion.expiracionMs()).isEqualTo(28_800_000L);
        assertThat(sesion.usuario().email()).isEqualTo("cmendoza@agenciavaluo.pe");
    }

    @Test
    void contrasenaIncorrectaLanzaCredencialesInvalidas() {
        when(usuarioRepository.buscarActivoPorEmail(any(Email.class))).thenReturn(Optional.of(agente));
        when(codificadorContrasena.coincide("mala", "hash")).thenReturn(false);

        assertThatThrownBy(() -> useCase.ejecutar(new IniciarSesionCommand("cmendoza@agenciavaluo.pe", "mala")))
                .isInstanceOf(CredencialesInvalidasException.class);
        verifyNoInteractions(generadorToken);
    }

    @Test
    void usuarioInexistenteLanzaElMismoError() {
        when(usuarioRepository.buscarActivoPorEmail(any(Email.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.ejecutar(new IniciarSesionCommand("nadie@valuo.pe", "x")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void correoMalFormadoSeTrataComoCredencialesInvalidas() {
        assertThatThrownBy(() -> useCase.ejecutar(new IniciarSesionCommand("no-es-correo", "x")))
                .isInstanceOf(CredencialesInvalidasException.class);
        verifyNoInteractions(usuarioRepository);
    }
}
