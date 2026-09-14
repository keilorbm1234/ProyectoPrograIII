package resourcemanager;

import resourcemanager.logic.UsuarioSession;
import resourcemanager.presentation.ControllerPrincipal;
import resourcemanager.presentation.ViewPrincipal;
import resourcemanager.presentation.login.ControllerLogin;
import resourcemanager.presentation.login.ModelLogin;
import resourcemanager.presentation.login.ViewLogin;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 *
 * Sigue el flujo indicado por el profesor:
 * primero se hace login (doLogin) y solo si el login fue exitoso
 * (UsuarioSession.isLoggedIn()) se continúa con la ejecución normal (doRun),
 * mostrando únicamente las opciones que correspondan al rol del usuario.
 */
public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::doLogin);
    }

    /**
     * Muestra el JDialog modal de Login (MVC: ViewLogin - ModelLogin - ControllerLogin).
     * Al ser modal, la ejecución se detiene aquí hasta que la ventana se cierre
     * (ya sea porque el login fue exitoso o porque el usuario canceló).
     * Si el login fue exitoso, continúa con doRun().
     */
    public static void doLogin() {
        ViewLogin view = new ViewLogin();
        ModelLogin model = new ModelLogin();
        ControllerLogin controller = new ControllerLogin(view, model);

        view.setController(controller);
        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);

        if (UsuarioSession.isLoggedIn()) {
            doRun();
        }
    }

    /**
     * Ejecución normal de la aplicación: abre la ventana principal, la cual
     * habilita solo las opciones (tabs/botones) que correspondan al rol
     * del usuario que inició sesión (ver ViewPrincipal.aplicarPermisos).
     */
    public static void doRun() {
        ViewPrincipal viewPrincipal = new ViewPrincipal();
        new ControllerPrincipal(viewPrincipal);
    }
}
