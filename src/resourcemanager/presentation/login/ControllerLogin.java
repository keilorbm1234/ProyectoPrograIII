package resourcemanager.presentation.login;

import resourcemanager.data.FuncionarioXmlDAO;
import resourcemanager.logic.Usuario;
import resourcemanager.logic.AuthService;
import resourcemanager.logic.UsuarioSession;
import resourcemanager.presentation.ControllerPrincipal;
import resourcemanager.presentation.ViewPrincipal;

import javax.swing.*;


public class ControllerLogin {
    private final ViewLogin view;
    private final ModelLogin model;
    private final AuthService authService;

    public ControllerLogin(ViewLogin view, ModelLogin model) {
        this.view = view;
        this.model = model;
        this.authService = new AuthService(new FuncionarioXmlDAO());
    }

    public void ingresar(String id, String clave) {
        try {
            authService.login(id, clave);
            Usuario logueado = UsuarioSession.getUsuario();

            if (logueado != null) {
                String rol = logueado.getRol();

                if ("ADMIN".equalsIgnoreCase(rol) || "FUNCIONARIO".equalsIgnoreCase(rol)) {


                    view.dispose();

                    ViewPrincipal viewPrincipal = new ViewPrincipal();
                    ControllerPrincipal controllerPrincipal = new ControllerPrincipal(viewPrincipal);


                    controllerPrincipal.configurarVista();

                } else {
                    throw new Exception("El usuario no tiene un rol válido asignado.");
                }
            }
        } catch (Exception ex) {
            view.mostrarError(ex.getMessage());
        }
    }

}
