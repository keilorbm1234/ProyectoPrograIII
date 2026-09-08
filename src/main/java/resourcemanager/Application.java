package resourcemanager;
import resourcemanager.presentation.login.ControllerLogin;
import resourcemanager.presentation.login.ModelLogin;
import resourcemanager.presentation.login.ViewLogin;

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