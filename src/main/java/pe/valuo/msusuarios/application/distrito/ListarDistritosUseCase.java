package pe.valuo.msusuarios.application.distrito;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.valuo.msusuarios.domain.repository.DistritoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarDistritosUseCase {

    private final DistritoRepository distritoRepository;

    @Transactional(readOnly = true)
    public List<DistritoResult> ejecutar() {
        return distritoRepository.listarActivos().stream().map(DistritoResult::de).toList();
    }
}
