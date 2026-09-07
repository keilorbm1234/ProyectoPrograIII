package cr.ac.una.resourcemanager.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
//creación de esta clase para que todos la puedan usar en sus model de cada paquete que necesiten

public class AbstractModel {
    protected PropertyChangeSupport changeSupport;

    public AbstractModel() {
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
