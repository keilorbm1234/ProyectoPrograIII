package resourcemanager.presentation.cambioclave;

import resourcemanager.logic.Funcionario;
import resourcemanager.presentation.AbstractModel;

public class ModelCambioClave extends AbstractModel {
    private final Funcionario usuario;

    public ModelCambioClave(Funcionario usuario) {
        this.usuario = usuario;
    }

    public Funcionario getUsuario() {
        return usuario;
    }
}
