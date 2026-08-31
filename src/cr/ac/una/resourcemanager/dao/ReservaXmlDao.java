package cr.ac.una.resourcemanager.dao;

import cr.ac.una.resourcemanager.model.ListaReservas;
import cr.ac.una.resourcemanager.model.Reserva;
import cr.ac.una.resourcemanager.util.XmlManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservaXmlDao implements DAO<Reserva, String> {
    private static final String rutaArchivo = "data/Reservas.xml";
    private List<Reserva> reservas;

    public ReservaXmlDao() {
        ListaReservas coleccion = XmlManager.cargar(rutaArchivo, ListaReservas.class);
        if (coleccion != null && coleccion.getReservas() != null) {
            this.reservas = new ArrayList<>(coleccion.getReservas());
        } else {
            this.reservas = new ArrayList<>();
        }
    }

    public void guardarEnXml() {
        ListaReservas coleccion = new ListaReservas();
        coleccion.setReservas(this.reservas);
        XmlManager.guardar(coleccion, rutaArchivo, ListaReservas.class);
    }

    @Override
    public void create(Reserva entity) throws Exception {
        if (read(entity.getId()).isPresent()) {
            throw new Exception("Ya existe una reserva con el ID: " + entity.getId());
        }
        this.reservas.add(entity);
        guardarEnXml();
    }

    @Override
    public Optional<Reserva> read(String id) throws Exception {
        for (Reserva reserva : this.reservas) {
            if (reserva.getId().equals(id)) {
                return Optional.of(reserva);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Reserva entity) throws Exception {
        for (int i = 0; i < this.reservas.size(); i++) {
            if (this.reservas.get(i).getId().equals(entity.getId())) {
                this.reservas.set(i, entity);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("No existe una reserva con el ID: " + entity.getId());
    }

    @Override
    public void delete(String id) throws Exception {
        for (int i = 0; i < this.reservas.size(); i++) {
            if (this.reservas.get(i).getId().equals(id)) {
                this.reservas.remove(i);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("Reserva no encontrada para eliminar con ID: " + id);
    }

    @Override
    public List<Reserva> readAll() throws Exception {
        return new ArrayList<>(this.reservas);
    }
}
