package pe.valuo.msusuarios.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.valuo.msusuarios.domain.model.distrito.Distrito;
import pe.valuo.msusuarios.domain.repository.DistritoRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class DistritoRepositoryAdapter implements DistritoRepository {

    private final SpringDataDistritoRepository jpa;

    @Override
    public List<Distrito> listarActivos() {
        return jpa.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public boolean existeActivo(Short id) {
        return jpa.existsByIdAndActivoTrue(id);
    }
}
