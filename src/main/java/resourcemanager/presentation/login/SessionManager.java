package resourcemanager.presentation.login;

import resourcemanager.logic.Usuario;
import resourcemanager.logic.UsuarioSession;
import resourcemanager.logic.ValidationException;

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
