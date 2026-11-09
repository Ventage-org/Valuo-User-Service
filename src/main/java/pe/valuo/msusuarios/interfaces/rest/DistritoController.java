package pe.valuo.msusuarios.interfaces.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.valuo.msusuarios.application.distrito.DistritoResult;
import pe.valuo.msusuarios.application.distrito.ListarDistritosUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/distritos")
@RequiredArgsConstructor
public class DistritoController {

    private final ListarDistritosUseCase listarDistritos;

    @GetMapping
    public List<DistritoResult> listar() {
        return listarDistritos.ejecutar();
    }
}
