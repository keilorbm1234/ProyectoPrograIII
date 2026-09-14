package resourcemanager;

import resourcemanager.logic.UsuarioSession;
import resourcemanager.presentation.ControllerPrincipal;
import resourcemanager.presentation.ViewPrincipal;
import resourcemanager.presentation.login.ControllerLogin;
import resourcemanager.presentation.login.ModelLogin;
import resourcemanager.presentation.login.ViewLogin;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::doLogin);
    }

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

    public static void doRun() {
        ViewPrincipal viewPrincipal = new ViewPrincipal();
        new ControllerPrincipal(viewPrincipal);
    }
}
