package resourcemanager.presentation.reservas;

import resourcemanager.logic.Reserva;
import resourcemanager.presentation.AbstractModel;
import java.util.ArrayList;
import java.util.List;

public class ModelReserva extends AbstractModel {
    public static final String LISTA_RESERVAS = "listaReservas";
    public static final String RESERVA_SELECCIONADA = "reservaSeleccionada";

    private List<Reserva> reservas = new ArrayList<>();
    private Reserva seleccionada = new Reserva();

    public List<Reserva> getReservas() { return reservas; }

    public void setReservas(List<Reserva> reservas) {
        List<Reserva> old = this.reservas;
        this.reservas = reservas;

        firePropertyChange(LISTA_RESERVAS, old, this.reservas);
    }

    public Reserva getSeleccionada() { return seleccionada; }

    public void setSeleccionada(Reserva seleccionada) {
        Reserva old = this.seleccionada;
        this.seleccionada = seleccionada;

        firePropertyChange(RESERVA_SELECCIONADA, old, this.seleccionada);
    }
}
