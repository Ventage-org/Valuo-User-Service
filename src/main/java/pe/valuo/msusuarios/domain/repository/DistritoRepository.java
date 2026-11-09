package pe.valuo.msusuarios.domain.repository;

import pe.valuo.msusuarios.domain.model.distrito.Distrito;

import java.util.List;

public interface DistritoRepository {

    List<Distrito> listarActivos();

    boolean existeActivo(Short id);
}
