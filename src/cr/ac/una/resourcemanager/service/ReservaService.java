package cr.ac.una.resourcemanager.service;

import cr.ac.una.resourcemanager.dao.ReservaXmlDao;
import cr.ac.una.resourcemanager.exception.DuplicateEntityException;
import cr.ac.una.resourcemanager.exception.ValidationException;
import cr.ac.una.resourcemanager.model.Recurso;
import cr.ac.una.resourcemanager.model.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ReservaService {
    private final ReservaXmlDao reservaXmlDao;

    public ReservaService(ReservaXmlDao reservaXmlDao) {
        this.reservaXmlDao = reservaXmlDao;
    }

    public void crearReserva(Reserva nueva) throws Exception {
        try {
            if (reservaXmlDao.read(nueva.getId()).isPresent()) {
                throw new DuplicateEntityException("Ya existe una reserva registrada con el ID: " + nueva.getId());
            }
        } catch (Exception e) {
            if (e instanceof DuplicateEntityException) throw (DuplicateEntityException) e;
        }

        // Validar conflicto de horarios y recursos antes de guardar
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

        // Validar disponibilidad con los nuevos datos
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

        // Obtener la lista directa desde ListaReservas
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
        // 1. Validaciones básicas de horario
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

        // 2. Obtener todas las reservas registradas desde el DAO
        List<Reserva> reservasExistentes;
        try {
            reservasExistentes = reservaXmlDao.readAll();
        } catch (Exception e) {
            throw new ValidationException("Error al consultar la disponibilidad de reservas.", e);
        }

        // 3. Evaluar conflictos con otras reservas
        for (Reserva existente : reservasExistentes) {
            // Ignorar si es la misma reserva (al modificar) o si está cancelada
            if (existente.getId().equals(nueva.getId()) || "CANCELADA".equalsIgnoreCase(existente.getEstado())) {
                continue;
            }

            // Si coinciden en la misma fecha
            if (existente.getFecha().equals(nueva.getFecha())) {

                // Condición matemática de solapamiento: (InicioA < FinB) Y (FinA > InicioB)
                boolean haySolapamientoHorario = nueva.getHoraInicio().isBefore(existente.getHoraFin())
                        && nueva.getHoraFin().isAfter(existente.getHoraInicio());

                if (haySolapamientoHorario) {
                    // Verificar si alguno de los recursos solicitados ya está ocupado en la reserva existente
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
}
