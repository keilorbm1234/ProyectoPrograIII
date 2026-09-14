package resourcemanager.presentation.login;

import resourcemanager.logic.Usuario;
import resourcemanager.logic.AuthService;

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
