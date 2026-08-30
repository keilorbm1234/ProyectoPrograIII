package cr.ac.una.resourcemanager;
import cr.ac.una.resourcemanager.dao.FuncionarioXmlDAO;
import cr.ac.una.resourcemanager.view.login.ControllerLogin;
import cr.ac.una.resourcemanager.view.login.ModelLogin;
import cr.ac.una.resourcemanager.view.login.ViewLogin;
public class Main {
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