package cr.ac.una.resourcemanager;
import cr.ac.una.resourcemanager.presentation.login.ControllerLogin;
import cr.ac.una.resourcemanager.presentation.login.ModelLogin;
import cr.ac.una.resourcemanager.presentation.login.ViewLogin;

public class Application {
    public static void main(String[] args) {
        //para probar el view de login
        ViewLogin view = new ViewLogin();
        ModelLogin model = new ModelLogin();
        ControllerLogin controller = new ControllerLogin(view, model);

        view.setController(controller);
        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }
}