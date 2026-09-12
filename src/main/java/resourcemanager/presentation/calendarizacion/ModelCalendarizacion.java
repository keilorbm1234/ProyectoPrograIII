package resourcemanager.presentation.calendarizacion;

import resourcemanager.logic.Recurso;
import resourcemanager.logic.Reserva;
import resourcemanager.presentation.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class ModelCalendarizacion extends AbstractModel {
    public static final String RECURSOS_PROPERTY = "recursos";
    public static final String RESERVAS_PROPERTY = "reservas";

    private List<Recurso> recursos;
    private List<Reserva> reservas;

    public ModelCalendarizacion() {
        this.recursos = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        List<Recurso> oldRecursos = this.recursos;
        this.recursos = recursos;
        firePropertyChange(RECURSOS_PROPERTY, oldRecursos, recursos);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        List<Reserva> oldReservas = this.reservas;
        this.reservas = reservas;
        firePropertyChange(RESERVAS_PROPERTY, oldReservas, reservas);
    }
}