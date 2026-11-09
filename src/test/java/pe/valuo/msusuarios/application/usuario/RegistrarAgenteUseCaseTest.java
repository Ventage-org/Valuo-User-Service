package pe.valuo.msusuarios.application.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.valuo.msusuarios.application.usuario.command.RegistrarAgenteCommand;
import pe.valuo.msusuarios.domain.exception.DistritoNoEncontradoException;
import pe.valuo.msusuarios.domain.exception.EmailYaRegistradoException;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;
import pe.valuo.msusuarios.domain.model.usuario.Email;
import pe.valuo.msusuarios.domain.model.usuario.RolUsuario;
import pe.valuo.msusuarios.domain.model.usuario.Usuario;
import pe.valuo.msusuarios.domain.repository.DistritoRepository;
import pe.valuo.msusuarios.domain.repository.UsuarioRepository;
import pe.valuo.msusuarios.domain.service.CodificadorContrasena;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarAgenteUseCaseTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock DistritoRepository distritoRepository;
    @Mock CodificadorContrasena codificadorContrasena;

    RegistrarAgenteUseCase useCase;

    @BeforeEach
    void preparar() {
        useCase = new RegistrarAgenteUseCase(usuarioRepository, distritoRepository, codificadorContrasena);
    }

    private RegistrarAgenteCommand comando(String ruc, Short distritoId) {
        return new RegistrarAgenteCommand("Carlos", "Mendoza", "CMendoza@agenciavaluo.pe", "987654321",
                "Password123!", ruc, "Mendoza Bienes Raíces", distritoId);
    }

    @Test
    void registraAgenteConContrasenaCodificadaYRucVacioComoNulo() {
        when(usuarioRepository.existePorEmail(any(Email.class))).thenReturn(false);
        when(distritoRepository.existeActivo((short) 16)).thenReturn(true);
        when(codificadorContrasena.codificar("Password123!")).thenReturn("hash");
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        useCase.ejecutar(comando("", (short) 16));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).guardar(captor.capture());
        Usuario guardado = captor.getValue();
        assertThat(guardado.getPasswordHash()).isEqualTo("hash");
        assertThat(guardado.getRuc()).isNull();
        assertThat(guardado.getRol()).isEqualTo(RolUsuario.AGENTE);
        assertThat(guardado.getEmail().valor()).isEqualTo("cmendoza@agenciavaluo.pe");
    }

    @Test
    void emailDuplicadoLanzaConflicto() {
        when(usuarioRepository.existePorEmail(any(Email.class))).thenReturn(true);

        assertThatThrownBy(() -> useCase.ejecutar(comando(null, (short) 16)))
                .isInstanceOf(EmailYaRegistradoException.class);
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void distritoInexistenteSeRechaza() {
        when(usuarioRepository.existePorEmail(any(Email.class))).thenReturn(false);
        when(distritoRepository.existeActivo((short) 99)).thenReturn(false);

        assertThatThrownBy(() -> useCase.ejecutar(comando(null, (short) 99)))
                .isInstanceOf(DistritoNoEncontradoException.class);
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void rucConFormatoInvalidoSeRechaza() {
        assertThatThrownBy(() -> useCase.ejecutar(comando("123", (short) 16)))
                .isInstanceOf(ValorInvalidoException.class);
    }
}
