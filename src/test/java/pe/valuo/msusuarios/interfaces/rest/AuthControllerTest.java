package pe.valuo.msusuarios.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.valuo.msusuarios.application.usuario.IniciarSesionUseCase;
import pe.valuo.msusuarios.application.usuario.RegistrarAgenteUseCase;
import pe.valuo.msusuarios.domain.exception.CredencialesInvalidasException;
import pe.valuo.msusuarios.domain.exception.EmailYaRegistradoException;
import pe.valuo.msusuarios.infrastructure.config.WebConfig;
import pe.valuo.msusuarios.infrastructure.security.JwtConfig;
import pe.valuo.msusuarios.infrastructure.security.SecurityConfig;
import pe.valuo.msusuarios.interfaces.rest.error.GlobalExceptionHandler;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtConfig.class, WebConfig.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean RegistrarAgenteUseCase registrarAgente;
    @MockitoBean IniciarSesionUseCase iniciarSesion;

    private static final String REGISTRO_VALIDO = """
            {"nombre":"Carlos","apellido":"Mendoza","email":"cmendoza@agenciavaluo.pe",
             "telefono":"987654321","password":"Password123!","ruc":"10456789012",
             "nombreAgencia":"Mendoza Bienes Raíces","distritoEspecializacionId":16}""";

    @Test
    void registroValidoDevuelve201ConElIdDelUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        when(registrarAgente.ejecutar(any())).thenReturn(id);

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(REGISTRO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario registrado exitosamente"))
                .andExpect(jsonPath("$.usuarioId").value(id.toString()));
    }

    @Test
    void telefonoInvalidoDevuelve400ConDetalleDelCampo() throws Exception {
        String body = REGISTRO_VALIDO.replace("987654321", "812345678");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.telefono").exists());
    }

    @Test
    void contrasenaDebilDevuelve400() throws Exception {
        String body = REGISTRO_VALIDO.replace("Password123!", "password");

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.password").exists());
    }

    @Test
    void emailDuplicadoDevuelve409() throws Exception {
        when(registrarAgente.ejecutar(any())).thenThrow(new EmailYaRegistradoException());

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(REGISTRO_VALIDO))
                .andExpect(status().isConflict());
    }

    @Test
    void loginConCredencialesInvalidasDevuelve401() throws Exception {
        when(iniciarSesion.ejecutar(any())).thenThrow(new CredencialesInvalidasException());

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"a@b.pe\",\"password\":\"x\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rutaProtegidaSinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/perfil")).andExpect(status().isUnauthorized());
    }
}
