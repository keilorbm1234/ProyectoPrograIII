package cr.ac.una.resourcemanager.presentation.login;

import cr.ac.una.resourcemanager.logic.Usuario;
import cr.ac.una.resourcemanager.logic.UsuarioSession;
import cr.ac.una.resourcemanager.logic.ValidationException;

public class SessionManager {
    public static boolean esAdmin() {
        Usuario user = UsuarioSession.getUsuario();
        return user != null && "ADMIN".equalsIgnoreCase(user.getRol());
    }

    public static boolean esFuncionario() {
        Usuario user = UsuarioSession.getUsuario();
        return user != null && "FUNCIONARIO".equalsIgnoreCase(user.getRol());
    }

    public static void validarPermisoAdmin() throws ValidationException {
        if (!esAdmin()) {
            throw new ValidationException("Acceso restringido: Se requieren permisos de Administrador.");
        }
    }
}
