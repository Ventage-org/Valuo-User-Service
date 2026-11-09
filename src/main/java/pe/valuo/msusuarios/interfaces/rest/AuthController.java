package pe.valuo.msusuarios.interfaces.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.valuo.msusuarios.application.usuario.IniciarSesionUseCase;
import pe.valuo.msusuarios.application.usuario.RegistrarAgenteUseCase;
import pe.valuo.msusuarios.interfaces.rest.dto.LoginRequest;
import pe.valuo.msusuarios.interfaces.rest.dto.LoginResponse;
import pe.valuo.msusuarios.interfaces.rest.dto.RegistroRequest;
import pe.valuo.msusuarios.interfaces.rest.dto.RegistroResponse;
import pe.valuo.msusuarios.interfaces.rest.mapper.UsuarioRestMapper;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrarAgenteUseCase registrarAgente;
    private final IniciarSesionUseCase iniciarSesion;

    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        UUID usuarioId = registrarAgente.ejecutar(UsuarioRestMapper.aCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegistroResponse("Usuario registrado exitosamente", usuarioId));
    }

    @PostMapping("/login")
    public LoginResponse iniciarSesion(@Valid @RequestBody LoginRequest request) {
        return UsuarioRestMapper.aResponse(iniciarSesion.ejecutar(UsuarioRestMapper.aCommand(request)));
    }
}
