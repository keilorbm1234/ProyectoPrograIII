package cr.ac.una.resourcemanager.presentation.recursos;
import cr.ac.una.resourcemanager.logic.Recurso;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
public class ModelRecurso {
    private List<Recurso> recursos = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        List<Recurso> old = this.recursos;
        this.recursos = recursos;
        support.firePropertyChange("recursos", old, recursos);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
