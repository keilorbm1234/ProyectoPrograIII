package cr.ac.una.resourcemanager.view.login;

import cr.ac.una.resourcemanager.model.Usuario;
import cr.ac.una.resourcemanager.util.AbstractModel;

public class ModelLogin extends AbstractModel {
    private Usuario current;

    public ModelLogin() {
        this.current = new Usuario();
    }
    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        Usuario old = this.current;
        this.current = current;
        firePropertyChange("current", old, current);

    }
}
