package pe.valuo.msusuarios.domain.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/** Raíz del agregado Usuario. Toda modificación pasa por métodos que expresan intención de negocio. */
@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Embedded
    private Email email;

    @Embedded
    private Telefono telefono;

    @Embedded
    private Ruc ruc;

    @Column(name = "nombre_agencia", length = 120)
    private String nombreAgencia;

    @Column(name = "distrito_especializacion_id")
    private Short distritoEspecializacionId;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "url_firma_digital", length = 500)
    private String urlFirmaDigital;

    @Column(name = "url_foto_perfil", length = 500)
    private String urlFotoPerfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;

    @Column(nullable = false)
    private boolean activo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Usuario registrarAgente(String nombre, String apellido, Email email, Telefono telefono,
                                          Ruc ruc, String nombreAgencia, Short distritoEspecializacionId,
                                          String passwordHash) {
        Usuario usuario = new Usuario();
        usuario.nombre = nombre;
        usuario.apellido = apellido;
        usuario.email = email;
        usuario.telefono = telefono;
        usuario.ruc = ruc;
        usuario.nombreAgencia = nombreAgencia;
        usuario.distritoEspecializacionId = distritoEspecializacionId;
        usuario.passwordHash = passwordHash;
        usuario.rol = RolUsuario.AGENTE;
        usuario.activo = true;
        return usuario;
    }

    public void actualizarPerfil(Telefono telefono, Ruc ruc, String nombreAgencia, Short distritoEspecializacionId) {
        this.telefono = telefono;
        this.ruc = ruc;
        this.nombreAgencia = nombreAgencia;
        this.distritoEspecializacionId = distritoEspecializacionId;
    }

    public void cambiarFirmaDigital(String urlFirmaDigital) {
        this.urlFirmaDigital = urlFirmaDigital;
    }

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }
}
