package resourcemanager.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testLoginRechazaCredencialesIncorrectasYAceptaLasCorrectas() {
        FuncionarioService funcionarioService = FuncionarioService.getInstance();
        AuthService authService = AuthService.getInstance();
        String id = "FUNC-TEST-AUTH-01";

        try {
            // La clave por defecto de un funcionario nuevo es igual a su id.
            funcionarioService.guardar(id, "Usuario de Prueba Login", "88888888");

            assertThrows(Exception.class, () -> authService.login(id, "clave-incorrecta"),
                    "Debe rechazar el login si la clave no coincide.");
            assertFalse(UsuarioSession.isLoggedIn(),
                    "No debe quedar una sesion activa tras un login fallido.");

            Usuario logueado = authService.login(id, id);
            assertNotNull(logueado, "El login exitoso debe retornar el usuario autenticado.");
            assertEquals(id, logueado.getId());
            assertTrue(UsuarioSession.isLoggedIn(),
                    "Tras un login exitoso, la Sesion debe quedar activa.");
            assertEquals(id, UsuarioSession.getUsuario().getId(),
                    "La Sesion debe guardar al usuario que inicio sesion.");

        } catch (Exception e) {
            fail("La prueba de login fallo al lanzar una excepcion inesperada: " + e.getMessage());
        } finally {
            UsuarioSession.logout();
            try {
                funcionarioService.borrar(id);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void testCambiarClaveValidaClaveActualYPermiteIniciarSesionConLaNueva() {
        FuncionarioService funcionarioService = FuncionarioService.getInstance();
        AuthService authService = AuthService.getInstance();
        String id = "FUNC-TEST-AUTH-02";

        try {
            funcionarioService.guardar(id, "Usuario de Prueba Cambio Clave", "88888888");

            assertThrows(ValidationException.class,
                    () -> authService.cambiarClave(id, "clave-incorrecta", "claveNueva123"),
                    "Debe rechazar el cambio si la clave actual no coincide.");

            assertThrows(ValidationException.class,
                    () -> authService.cambiarClave(id, id, ""),
                    "Debe rechazar una clave nueva vacia.");

            assertThrows(ValidationException.class,
                    () -> authService.cambiarClave(id, id, id),
                    "Debe rechazar que la nueva clave sea igual a la actual.");

            authService.cambiarClave(id, id, "claveNueva123");

            assertThrows(Exception.class, () -> authService.login(id, id),
                    "La clave anterior ya no debe permitir iniciar sesion.");
            UsuarioSession.logout();

            Usuario logueado = authService.login(id, "claveNueva123");
            assertNotNull(logueado, "Debe poder iniciar sesion con la nueva clave.");

        } catch (Exception e) {
            fail("La prueba de cambio de clave fallo al lanzar una excepcion inesperada: " + e.getMessage());
        } finally {
            UsuarioSession.logout();
            try {
                funcionarioService.borrar(id);
            } catch (Exception ignored) {
            }
        }
    }
}
