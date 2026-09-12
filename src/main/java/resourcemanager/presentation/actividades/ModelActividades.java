package resourcemanager.presentation.actividades;

import resourcemanager.logic.Reserva;
import resourcemanager.presentation.AbstractModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModelActividades extends AbstractModel {
    public static final String ACTIVIDADES_PROPERTY = "actividades";

    private LocalDate inicioSemana;
    private List<Reserva> reservas;

    public ModelActividades() {
        this.reservas = new ArrayList<>();
    }

    public LocalDate getInicioSemana() {
        return inicioSemana;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setDatos(LocalDate inicioSemana, List<Reserva> reservas) {
        List<Reserva> oldReservas = this.reservas;
        this.inicioSemana = inicioSemana;
        this.reservas = reservas;
        firePropertyChange(ACTIVIDADES_PROPERTY, oldReservas, reservas);
    }
}