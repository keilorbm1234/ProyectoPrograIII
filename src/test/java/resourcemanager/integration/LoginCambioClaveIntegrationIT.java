package resourcemanager.integration;

import resourcemanager.logic.AuthService;
import resourcemanager.logic.FuncionarioService;
import resourcemanager.logic.Usuario;
import resourcemanager.logic.UsuarioSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginCambioClaveIntegrationIT {

    @Test
    public void testFlujoCompletoCrearFuncionarioLoginYCambioDeClave() {
        FuncionarioService funcionarioService = FuncionarioService.getInstance();
        AuthService authService = AuthService.getInstance();
        String id = "IT-LOGIN-01";

        try {
            funcionarioService.guardar(id, "Funcionario Prueba Integracion Login", "88888888");

            Usuario logueado = authService.login(id, id);
            assertNotNull(logueado, "El login con la clave por defecto debe ser exitoso.");
            assertTrue(UsuarioSession.isLoggedIn(), "La Sesion debe quedar activa tras el login.");
            assertEquals(id, UsuarioSession.getUsuario().getId());

            authService.cambiarClave(id, id, "ClaveSegura#2026");

            UsuarioSession.logout();
            assertThrows(Exception.class, () -> authService.login(id, id),
                    "La clave anterior debe quedar invalidada tras el cambio.");

            Usuario logueadoConNuevaClave = authService.login(id, "ClaveSegura#2026");
            assertNotNull(logueadoConNuevaClave);
            assertTrue(UsuarioSession.isLoggedIn());

        } catch (Exception e) {
            fail("La prueba de integración de login/cambio de clave falló: " + e.getMessage());
        } finally {
            UsuarioSession.logout();
            try {
                funcionarioService.borrar(id);
            } catch (Exception ignored) {
            }
        }
    }
}
