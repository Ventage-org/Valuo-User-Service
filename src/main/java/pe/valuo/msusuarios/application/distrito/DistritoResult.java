package pe.valuo.msusuarios.application.distrito;

import pe.valuo.msusuarios.domain.model.distrito.Distrito;

public record DistritoResult(Short id, String nombre, String zonaUrbana) {

    public static DistritoResult de(Distrito distrito) {
        return new DistritoResult(distrito.getId(), distrito.getNombre(), distrito.getZonaUrbana());
    }
}
