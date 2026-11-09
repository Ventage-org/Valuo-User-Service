package pe.valuo.msusuarios.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.valuo.msusuarios.domain.model.distrito.Distrito;

import java.util.List;

interface SpringDataDistritoRepository extends JpaRepository<Distrito, Short> {

    List<Distrito> findByActivoTrueOrderByNombreAsc();

    boolean existsByIdAndActivoTrue(Short id);
}
