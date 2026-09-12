package resourcemanager.presentation.estadisticas;

import resourcemanager.presentation.AbstractModel;

import java.util.HashMap;
import java.util.Map;

public class ModelEstadisticas extends AbstractModel {
    public static final String DATOS_RECURSOS = "datosRecursos";
    public static final String DATOS_ACTIVIDADES = "datosActividades";

    private Map<String, Long> datosRecursos;
    private Map<String, Long> datosActividades;

    public ModelEstadisticas() {
        this.datosRecursos = new HashMap<>();
        this.datosActividades = new HashMap<>();
    }

    public Map<String, Long> getDatosRecursos() {
        return datosRecursos;
    }

    public void setDatosRecursos(Map<String, Long> datosRecursos) {
        Map<String, Long> old = this.datosRecursos;
        this.datosRecursos = datosRecursos;
        firePropertyChange(DATOS_RECURSOS, old, datosRecursos);
    }

    public Map<String, Long> getDatosActividades() {
        return datosActividades;
    }

    public void setDatosActividades(Map<String, Long> datosActividades) {
        Map<String, Long> old = this.datosActividades;
        this.datosActividades = datosActividades;
        firePropertyChange(DATOS_ACTIVIDADES, old, datosActividades);
    }
}