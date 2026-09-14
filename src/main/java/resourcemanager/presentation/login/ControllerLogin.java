package resourcemanager.presentation.login;

import resourcemanager.logic.Usuario;
import resourcemanager.logic.AuthService;

/**
 * Controller del MVC de Login (según guía del profesor):
 * expone un único método "login" (ingresar) que valida las credenciales
 * y, si son correctas, deja al usuario guardado en la Sesion (UsuarioSession)
 * y en el Model, y cierra el JDialog. La creación de la ventana principal
 * (ViewPrincipal) NO es responsabilidad de este controller: eso lo maneja
 * Application.doRun(), que se ejecuta solo si el login fue exitoso.
 */
public class ControllerLogin {
    private final ViewLogin view;
    private final ModelLogin model;
    private final AuthService authService;

    public ControllerLogin(ViewLogin view, ModelLogin model) {
        this.view = view;
        this.model = model;
        this.authService = AuthService.getInstance();
    }

    public void ingresar(String id, String clave) {
        try {
            Usuario logueado = authService.login(id, clave);
            model.setCurrent(logueado);
            view.dispose();
        } catch (Exception ex) {
            String mensaje = (ex.getMessage() != null) ? ex.getMessage() : "Error desconocido al intentar iniciar sesión.";
            view.mostrarError(mensaje);
        }
    }
}
