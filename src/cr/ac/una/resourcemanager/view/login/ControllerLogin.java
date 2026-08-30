package cr.ac.una.resourcemanager.view.login;

import cr.ac.una.resourcemanager.dao.DAO;
import cr.ac.una.resourcemanager.dao.FuncionarioXmlDAO;
import cr.ac.una.resourcemanager.model.Usuario;
import cr.ac.una.resourcemanager.service.AuthService;
import cr.ac.una.resourcemanager.service.UsuarioSession;

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
            // Recuperamos el usuario que acaba de iniciar sesión
            Usuario logueado = UsuarioSession.getUsuario();

            if (logueado != null) {
                // Validamos el ROL (ADMIN vs FUNCIONARIO)
                if ("ADMIN".equalsIgnoreCase(logueado.getRol())) {
                    JOptionPane.showMessageDialog(
                            view,
                            "Acceso concedido. Bienvenido Administrador (" + logueado.getId() + ")",
                            "Inicio de Sesión Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // abrir la vista de Admin cuando exista
                } else if ("FUNCIONARIO".equalsIgnoreCase(logueado.getRol())) {
                    JOptionPane.showMessageDialog(
                            view,
                            "Acceso concedido. Bienvenido Funcionario (" + logueado.getId() + ")",
                            "Inicio de Sesión Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // abrir la vista de Funcionario cuando exista
                } else {
                    throw new Exception("El usuario no tiene un rol válido asignado.");
                }
                view.dispose();
            }
        } catch (Exception ex) {
            view.mostrarError(ex.getMessage());
        }
    }


}
