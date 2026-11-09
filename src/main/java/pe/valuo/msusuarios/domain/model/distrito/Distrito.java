package pe.valuo.msusuarios.domain.model.distrito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Catálogo de solo lectura: los 22 distritos del BCRP, sembrados por Flyway. */
@Entity
@Table(name = "distrito")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Distrito {

    @Id
    private Short id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(name = "zona_urbana", nullable = false, length = 30)
    private String zonaUrbana;

    @Column(nullable = false)
    private boolean activo;
}
