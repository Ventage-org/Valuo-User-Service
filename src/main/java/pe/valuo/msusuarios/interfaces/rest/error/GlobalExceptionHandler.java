package pe.valuo.msusuarios.interfaces.rest.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pe.valuo.msusuarios.application.exception.ArchivoInvalidoException;
import pe.valuo.msusuarios.domain.exception.CredencialesInvalidasException;
import pe.valuo.msusuarios.domain.exception.DistritoNoEncontradoException;
import pe.valuo.msusuarios.domain.exception.EmailYaRegistradoException;
import pe.valuo.msusuarios.domain.exception.UsuarioNoEncontradoException;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;

import java.util.LinkedHashMap;
import java.util.Map;

/** Traduce excepciones de dominio y de aplicación a respuestas HTTP (RFC 9457 ProblemDetail). */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ValorInvalidoException.class, DistritoNoEncontradoException.class, ArchivoInvalidoException.class})
    ProblemDetail solicitudInvalida(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    ProblemDetail credencialesInvalidas(CredencialesInvalidasException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    ProblemDetail usuarioNoEncontrado(UsuarioNoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    ProblemDetail emailYaRegistrado(EmailYaRegistradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    /** Red de seguridad ante la carrera de dos registros simultáneos con el mismo correo. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail violacionIntegridad(DataIntegrityViolationException e) {
        log.warn("Violación de integridad: {}", e.getMostSpecificCause().getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "El recurso ya existe o viola una restricción");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos");
        problema.setProperty("errores", errores);
        return ResponseEntity.badRequest().body(problema);
    }
}
