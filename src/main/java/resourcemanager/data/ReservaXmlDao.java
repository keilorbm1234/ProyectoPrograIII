package resourcemanager.data;

import resourcemanager.logic.ListaReservas;
import resourcemanager.logic.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void guardarEnXml() throws PersistenceException {
        try {
            ListaReservas coleccion = new ListaReservas();
            coleccion.setReservas(this.reservas);
            XmlManager.guardar(coleccion, rutaArchivo, ListaReservas.class);
        } catch (Exception e) {
            throw new PersistenceException("Error al guardar en el archivo XML de Reservas: " + e.getMessage());
        }
    }

    @Override
    public void create(Reserva entity) throws Exception {
        if (read(entity.getId()).isPresent()) {
            throw new DuplicateEntityException("Ya existe una reserva con el ID: " + entity.getId());
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
        throw new PersistenceException("No existe una reserva con el ID: " + entity.getId());
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
        throw new PersistenceException("Reserva no encontrada para eliminar con el ID: " + id);
    }

    @Override
    public List<Reserva> readAll() throws Exception {
        return new ArrayList<>(this.reservas);
    }

    //Obtener reservas por funcionario
    public List<Reserva> obtenerReservasPorFuncionario(String funcionarioId){
        if(funcionarioId == null || funcionarioId.trim().isEmpty()){
            return new ArrayList<>();
        }
        return this.reservas.stream().filter(r-> r.getFuncionario() != null && r.getFuncionario().getId().equals(funcionarioId))
                .collect(Collectors.toList());
    }

    //Obtener reservas por fecha exacta
    public List<Reserva> obtenerReservasporFecha(LocalDate fecha){
        if(fecha == null){
            return new ArrayList<>();
        }
        return this.reservas.stream()
                .filter(r-> r.getFecha() != null && r.getFecha().isEqual(fecha))
                .collect(Collectors.toList());
    }

    //Obtener reservas en un rango de fechas
    public List<Reserva> obtenerPorRangoFechas(LocalDate desde, LocalDate hasta){
        if(desde == null || hasta == null){
            return new ArrayList<>();
        }
        return this.reservas.stream()
                .filter(r -> r.getFecha() != null &&
                        !r.getFecha().isBefore(desde) &&
                        !r.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }
}
