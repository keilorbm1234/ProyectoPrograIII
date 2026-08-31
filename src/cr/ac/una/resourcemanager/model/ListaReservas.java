package cr.ac.una.resourcemanager.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ListaReservas")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListaReservas {
    @XmlElement(name = "reserva")
    private List<Reserva> reservas;

    public ListaReservas() {
        this.reservas = new ArrayList<>();
    }

    public ListaReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
