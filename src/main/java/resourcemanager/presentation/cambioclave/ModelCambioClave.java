package resourcemanager.presentation.cambioclave;

import resourcemanager.logic.Funcionario;
import resourcemanager.presentation.AbstractModel;

/**
 * Model del MVC de Cambio de Clave: mantiene el usuario al que se le
 * cambiará la clave (el que tiene la sesión activa).
 */
public class ModelCambioClave extends AbstractModel {
    private final Funcionario usuario;

    public ModelCambioClave(Funcionario usuario) {
        this.usuario = usuario;
    }

    public Funcionario getUsuario() {
        return usuario;
    }
}
