package pe.valuo.msusuarios.interfaces.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.valuo.msusuarios.application.usuario.ActualizarFirmaDigitalUseCase;
import pe.valuo.msusuarios.application.usuario.ActualizarPerfilUseCase;
import pe.valuo.msusuarios.application.usuario.ConsultarPerfilUseCase;
import pe.valuo.msusuarios.application.usuario.command.SubirFirmaCommand;
import pe.valuo.msusuarios.application.usuario.result.PerfilResult;
import pe.valuo.msusuarios.interfaces.rest.dto.ActualizarPerfilRequest;
import pe.valuo.msusuarios.interfaces.rest.dto.FirmaResponse;
import pe.valuo.msusuarios.interfaces.rest.mapper.UsuarioRestMapper;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final ConsultarPerfilUseCase consultarPerfil;
    private final ActualizarPerfilUseCase actualizarPerfil;
    private final ActualizarFirmaDigitalUseCase actualizarFirmaDigital;

    @GetMapping
    public PerfilResult consultar(@AuthenticationPrincipal Jwt jwt) {
        return consultarPerfil.ejecutar(usuarioId(jwt));
    }

    @PutMapping
    public PerfilResult actualizar(@AuthenticationPrincipal Jwt jwt,
                                   @Valid @RequestBody ActualizarPerfilRequest request) {
        return actualizarPerfil.ejecutar(UsuarioRestMapper.aCommand(usuarioId(jwt), request));
    }

    @PostMapping(path = "/firma", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FirmaResponse subirFirma(@AuthenticationPrincipal Jwt jwt,
                                    @RequestParam("archivo") MultipartFile archivo) throws IOException {
        String url = actualizarFirmaDigital.ejecutar(
                new SubirFirmaCommand(usuarioId(jwt), archivo.getContentType(), archivo.getBytes()));
        return new FirmaResponse(url);
    }

    private UUID usuarioId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
