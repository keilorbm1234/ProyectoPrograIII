package cr.ac.una.resourcemanager.service;
import cr.ac.una.resourcemanager.model.Usuario;

public class UsuarioSession {
    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioSession.usuario = usuario;
    }

    public static void logout() {
        UsuarioSession.usuario = null;
    }

    public static boolean isLoggedIn() {
        return usuario != null;
    }
}
