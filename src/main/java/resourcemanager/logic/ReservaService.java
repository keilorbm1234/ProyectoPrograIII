package resourcemanager.logic;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import resourcemanager.data.RecursoXmlDao;
import resourcemanager.data.ReservaXmlDao;
import resourcemanager.data.DuplicateEntityException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReservaService {
    private static ReservaService instance;
    private final ReservaXmlDao reservaXmlDao;
    private final RecursoXmlDao recursoXmlDao;

    public ReservaService(ReservaXmlDao reservaXmlDao, RecursoXmlDao recursoXmlDao) {
        this.reservaXmlDao = reservaXmlDao;
        this.recursoXmlDao = recursoXmlDao;
    }

    public static synchronized ReservaService getInstance() {
        if (instance == null) {
            instance = new ReservaService(new ReservaXmlDao(), new RecursoXmlDao());
        }
        return instance;
    }

    public void crearReserva(Reserva nueva) throws Exception {
        try {
            if (reservaXmlDao.read(nueva.getId()).isPresent()) {
                throw new DuplicateEntityException("Ya existe una reserva registrada con el ID: " + nueva.getId());
            }
        } catch (Exception e) {
            if (e instanceof DuplicateEntityException) throw (DuplicateEntityException) e;
        }


        validarDisponibilidadYHorario(nueva);

        nueva.setEstado("ACTIVA");

        try {
            reservaXmlDao.create(nueva);
        } catch (Exception e) {
            throw new ValidationException("No se pudo guardar la reserva en el sistema.", e);
        }
    }

    public void modificarReserva(Reserva modificada) throws Exception {
        Optional<Reserva> opt;
        try {
            opt = reservaXmlDao.read(modificada.getId());
        } catch (Exception e) {
            throw new ValidationException("Error al buscar la reserva.");
        }

        Reserva existente = opt.orElseThrow(() ->
                new ValidationException("La reserva que intenta modificar no existe."));

        if ("CANCELADA".equalsIgnoreCase(existente.getEstado())) {
            throw new ValidationException("No se puede modificar una reserva en estado CANCELADA.");
        }


        validarDisponibilidadYHorario(modificada);

        try {
            reservaXmlDao.update(modificada);
        } catch (Exception e) {
            throw new ValidationException("Error al actualizar la reserva en el XML.", e);
        }
    }


    public void cancelarReserva(String idReserva) throws Exception {
        Reserva reserva = buscarPorId(idReserva)
                .orElseThrow(() -> new ValidationException("La reserva indicada no existe."));

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new ValidationException("La reserva ya se encuentra cancelada.");
        }

        reserva.setEstado("CANCELADA");
        reservaXmlDao.update(reserva);
    }


    public Optional<Reserva> buscarPorId(String id) throws Exception {
        return reservaXmlDao.read(id);
    }

    private void validarReserva(Reserva r) throws Exception {
        if (r.getFecha() == null || r.getFecha().isBefore(LocalDate.now())) {
            throw new ValidationException("La fecha de la reserva no puede ser en el pasado.");
        }
        if (r.getHoraInicio() == null || r.getHoraFin() == null || !r.getHoraFin().isAfter(r.getHoraInicio())) {
            throw new ValidationException("La hora final debe ser posterior a la hora de inicio.");
        }
        if (r.getRecursosAsignados() == null || r.getRecursosAsignados().isEmpty()) {
            throw new ValidationException("Debe asignar al menos un recurso a la reserva.");
        }


        List<Reserva> todas = reservaXmlDao.readAll();

        for (Reserva existente : todas) {
            if (existente.getId().equals(r.getId()) || "CANCELADA".equalsIgnoreCase(existente.getEstado())) {
                continue;
            }

            if (existente.getFecha().equals(r.getFecha())) {
                boolean hayCruceHorario = r.getHoraInicio().isBefore(existente.getHoraFin())
                        && r.getHoraFin().isAfter(existente.getHoraInicio());

                if (hayCruceHorario) {
                    boolean comparteRecurso = r.getRecursosAsignados().stream()
                            .anyMatch(rec -> existente.getRecursosAsignados().stream()
                                    .anyMatch(e -> e.getId().equals(rec.getId())));

                    if (comparteRecurso) {
                        throw new ValidationException("El horario seleccionado entra en conflicto con otra reserva activa.");
                    }
                }
            }
        }
    }
    private void validarDisponibilidadYHorario(Reserva nueva) throws ValidationException {

        if (nueva.getFecha() == null || nueva.getFecha().isBefore(LocalDate.now())) {
            throw new ValidationException("La fecha de la reserva no puede ser en el pasado.");
        }
        if (nueva.getHoraInicio() == null || nueva.getHoraFin() == null) {
            throw new ValidationException("Debe ingresar la hora de inicio y fin.");
        }
        if (!nueva.getHoraFin().isAfter(nueva.getHoraInicio())) {
            throw new ValidationException("La hora final debe ser posterior a la hora de inicio.");
        }
        if (nueva.getRecursosAsignados() == null || nueva.getRecursosAsignados().isEmpty()) {
            throw new ValidationException("Debe asignar al menos un recurso a la reserva.");
        }


        List<Reserva> reservasExistentes;
        try {
            reservasExistentes = reservaXmlDao.readAll();
        } catch (Exception e) {
            throw new ValidationException("Error al consultar la disponibilidad de reservas.", e);
        }


        for (Reserva existente : reservasExistentes) {

            if (existente.getId().equals(nueva.getId()) || "CANCELADA".equalsIgnoreCase(existente.getEstado())) {
                continue;
            }


            if (existente.getFecha().equals(nueva.getFecha())) {


                boolean haySolapamientoHorario = nueva.getHoraInicio().isBefore(existente.getHoraFin())
                        && nueva.getHoraFin().isAfter(existente.getHoraInicio());

                if (haySolapamientoHorario) {

                    for (Recurso recursoNuevo : nueva.getRecursosAsignados()) {
                        for (Recurso recursoExistente : existente.getRecursosAsignados()) {

                            if (recursoNuevo.getId().equals(recursoExistente.getId())) {
                                throw new ValidationException(
                                        "El recurso '" + recursoNuevo.getDescripcion() +
                                                "' (" + recursoNuevo.getId() + ") no está disponible en el horario de " +
                                                existente.getHoraInicio() + " a " + existente.getHoraFin() +
                                                " debido a otra reserva activa."
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Recurso> asignarRecursosDisponibles(LocalDate fecha, LocalTime inicio, LocalTime fin, List<Categoria> categoriasSolicitadas) throws ValidationException {

        List<Recurso> disponibles = obtenerRecursosDisponibles(fecha, inicio, fin);
        List<Recurso> seleccionados = new ArrayList<>();

        for (Categoria cat : categoriasSolicitadas) {
            Optional<Recurso> asignado = disponibles.stream()
                    .filter(r -> r.getCategoria() != null && r.getCategoria().getId().equals(cat.getId()))
                    .findFirst();

            if (asignado.isPresent()) {
                seleccionados.add(asignado.get());
            } else {
                throw new ValidationException("No hay recursos disponibles para la categoría: " + cat.getDescripcion());
            }
        }

        return seleccionados;
    }

    public List<Recurso> obtenerRecursosDisponibles(LocalDate fecha, LocalTime inicio, LocalTime fin) throws ValidationException {
        try {
            List<Recurso> todosRecursos = recursoXmlDao.readAll();
            List<Reserva> todasReservas = reservaXmlDao.readAll();


            Set<String> idsOcupadas = todasReservas.stream()
                    .filter(r -> !"CANCELADA".equalsIgnoreCase(r.getEstado()))
                    .filter(r -> r.getFecha().equals(fecha))
                    .filter(r -> inicio.isBefore(r.getHoraFin()) && fin.isAfter(r.getHoraInicio()))
                    .flatMap(r -> r.getRecursosAsignados().stream())
                    .map(Recurso::getId)
                    .collect(Collectors.toSet());


            return todosRecursos.stream()
                    .filter(r -> !idsOcupadas.contains(r.getId()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ValidationException("Error al consultar la disponibilidad de recursos.", e);
        }
    }
    public List<Reserva> obtenerTodas() throws Exception {
        return reservaXmlDao.readAll();
    }


    public List<Reserva> obtenerReservasActivasPorFuncionario(String idFuncionario) throws Exception {
        return reservaXmlDao.readAll().stream()
                .filter(r -> r.getFuncionario() != null && idFuncionario.equalsIgnoreCase(r.getFuncionario().getId()))
                .filter(r -> "ACTIVA".equalsIgnoreCase(r.getEstado()))
                .collect(Collectors.toList());
    }


    public void cancelarReservaFutura(String idReserva, String idFuncionario) throws Exception {
        Reserva reserva = buscarPorId(idReserva)
                .orElseThrow(() -> new ValidationException("La reserva no existe."));

        if (!"ACTIVA".equalsIgnoreCase(reserva.getEstado())) {
            throw new ValidationException("Solo se pueden cancelar reservas que estén en estado ACTIVA.");
        }

        if (reserva.getFuncionario() == null || !idFuncionario.equalsIgnoreCase(reserva.getFuncionario().getId())) {
            throw new ValidationException("No tiene permisos para cancelar esta reserva.");
        }


        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioReserva = LocalDateTime.of(reserva.getFecha(), reserva.getHoraInicio());

        if (!inicioReserva.isAfter(ahora)) {
            throw new ValidationException("No se puede cancelar una reserva que ya ha iniciado o pasado.");
        }


        reserva.setEstado("CANCELADA");
        reservaXmlDao.update(reserva);
    }

    public ReservaExtraccion extraerReserva(String frase) {
        OpenAiChatModel aiModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        ReservaExtractorService aiService = AiServices.create(ReservaExtractorService.class, aiModel);

        String listaCategorias = String.join(",", CategoriaService.getInstance().getNombreCategorias());

        return aiService.extraer(frase, listaCategorias, LocalDate.now().toString());
    }
}
