package pe.valuo.msusuarios.domain.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pe.valuo.msusuarios.domain.exception.ValorInvalidoException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Test
    void emailSeNormalizaAMinusculas() {
        assertThat(new Email("  CMendoza@AgenciaValuo.PE ").valor()).isEqualTo("cmendoza@agenciavaluo.pe");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "sin-arroba", "a@b", "a b@c.pe"})
    void emailInvalidoSeRechaza(String valor) {
        assertThatThrownBy(() -> new Email(valor)).isInstanceOf(ValorInvalidoException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"812345678", "98765432", "9876543210", "98765432a"})
    void telefonoInvalidoSeRechaza(String valor) {
        assertThatThrownBy(() -> new Telefono(valor)).isInstanceOf(ValorInvalidoException.class);
    }

    @Test
    void telefonoValidoSeAcepta() {
        assertThat(new Telefono("987654321").valor()).isEqualTo("987654321");
    }

    @ParameterizedTest
    @ValueSource(strings = {"30456789012", "1045678901", "104567890123"})
    void rucInvalidoSeRechaza(String valor) {
        assertThatThrownBy(() -> new Ruc(valor)).isInstanceOf(ValorInvalidoException.class);
    }

    @Test
    void rucOpcionalVacioEquivaleASinRuc() {
        assertThat(Ruc.deTextoOpcional(null)).isNull();
        assertThat(Ruc.deTextoOpcional("  ")).isNull();
        assertThat(Ruc.deTextoOpcional("10456789012").valor()).isEqualTo("10456789012");
        assertThat(Ruc.deTextoOpcional("20456789012").valor()).isEqualTo("20456789012");
    }

    @Test
    void registrarAgenteFijaRolYEstado() {
        Usuario agente = Usuario.registrarAgente("Carlos", "Mendoza", new Email("cmendoza@agenciavaluo.pe"),
                new Telefono("987654321"), null, null, (short) 16, "hash");

        assertThat(agente.getRol()).isEqualTo(RolUsuario.AGENTE);
        assertThat(agente.isActivo()).isTrue();
        assertThat(agente.nombreCompleto()).isEqualTo("Carlos Mendoza");
    }

    @Test
    void actualizarPerfilNoTocaLaFirmaDigital() {
        Usuario agente = Usuario.registrarAgente("Carlos", "Mendoza", new Email("cmendoza@agenciavaluo.pe"),
                new Telefono("987654321"), null, null, (short) 16, "hash");
        agente.cambiarFirmaDigital("http://localhost/uploads/firmas/a.png");

        agente.actualizarPerfil(new Telefono("999888777"), new Ruc("10456789012"), "Mendoza & Asociados", (short) 19);

        assertThat(agente.getTelefono().valor()).isEqualTo("999888777");
        assertThat(agente.getRuc().valor()).isEqualTo("10456789012");
        assertThat(agente.getDistritoEspecializacionId()).isEqualTo((short) 19);
        assertThat(agente.getUrlFirmaDigital()).isEqualTo("http://localhost/uploads/firmas/a.png");
    }
}
