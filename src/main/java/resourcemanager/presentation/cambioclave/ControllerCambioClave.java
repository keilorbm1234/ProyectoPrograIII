package resourcemanager.presentation.cambioclave;

import resourcemanager.logic.AuthService;

/**
 * Controller del MVC de Cambio de Clave: recibe la clave actual y la nueva
 * desde la View, y delega la validación/actualización en AuthService.
 */
public class ControllerCambioClave {
    private final ModelCambioClave model;
    private final ViewCambioClave view;
    private final AuthService authService;

    public ControllerCambioClave(ModelCambioClave model, ViewCambioClave view) {
        this.model = model;
        this.view = view;
        this.authService = AuthService.getInstance();
    }

    public void cambiarClave(String claveActual, String claveNueva) {
        try {
            authService.cambiarClave(model.getUsuario().getId(), claveActual, claveNueva);
            view.mostrarExito("Clave actualizada correctamente.");
        } catch (Exception ex) {
            String mensaje = (ex.getMessage() != null) ? ex.getMessage() : "Error al cambiar la clave.";
            view.mostrarError(mensaje);
        }
    }
}
