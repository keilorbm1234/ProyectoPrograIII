package resourcemanager.integration;

import resourcemanager.logic.AuthService;
import resourcemanager.logic.FuncionarioService;
import resourcemanager.logic.Usuario;
import resourcemanager.logic.UsuarioSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración de la funcionalidad 1 del proyecto (Ingreso/Login
 * y cambio de clave): encadena FuncionarioService, AuthService y la
 * Sesion (UsuarioSession) tal como ocurriría en un flujo real de uso.
 */
public class LoginCambioClaveIntegrationIT {

    @Test
    public void testFlujoCompletoCrearFuncionarioLoginYCambioDeClave() {
        FuncionarioService funcionarioService = FuncionarioService.getInstance();
        AuthService authService = AuthService.getInstance();
        String id = "IT-LOGIN-01";

        try {
            // 1) Un administrador crea un funcionario nuevo (clave por defecto = id).
            funcionarioService.guardar(id, "Funcionario Prueba Integracion Login", "88888888");

            // 2) El funcionario inicia sesión por primera vez con la clave por defecto.
            Usuario logueado = authService.login(id, id);
            assertNotNull(logueado, "El login con la clave por defecto debe ser exitoso.");
            assertTrue(UsuarioSession.isLoggedIn(), "La Sesion debe quedar activa tras el login.");
            assertEquals(id, UsuarioSession.getUsuario().getId());

            // 3) El funcionario cambia su clave.
            authService.cambiarClave(id, id, "ClaveSegura#2026");

            // 4) La clave anterior ya no debe funcionar.
            UsuarioSession.logout();
            assertThrows(Exception.class, () -> authService.login(id, id),
                    "La clave anterior debe quedar invalidada tras el cambio.");

            // 5) La nueva clave sí debe permitir iniciar sesión.
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
