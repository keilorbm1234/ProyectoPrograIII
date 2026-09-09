package resourcemanager.presentation.reservas;

import resourcemanager.data.RecursoXmlDao;
import resourcemanager.logic.*;
import resourcemanager.data.ReservaXmlDao;


import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ControllerReservas {
    private final ModelReserva model;
    private final Reservas view;
    private final ReservaService reservaService;
    private final Funcionario funcionarioLogueado;

    public ControllerReservas(ModelReserva model, Reservas view, ReservaService reservaService, Funcionario funcionarioLogueado) {
        this.model = model;
        this.view = view;
        this.reservaService = reservaService;
        this.funcionarioLogueado = funcionarioLogueado;


        this.model.addPropertyChangeListener((PropertyChangeListener) this.view);

        cargarReservasActivas();
    }

    public void cargarReservasActivas() {
        try {
            List<Reserva> activas = reservaService.obtenerReservasActivasPorFuncionario(funcionarioLogueado.getId());
            model.setReservas(activas);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar las reservas: " + ex.getMessage());
        }
    }

    public void guardarReserva(String id, String actividad, LocalDate fecha, LocalTime inicio, LocalTime fin, List<Categoria> categorias) {
        try {

            List<Recurso> recursosAsignados = reservaService.asignarRecursosDisponibles(fecha, inicio, fin, categorias);

            Reserva nueva = new Reserva();
            nueva.setId(id);
            nueva.setActividad(actividad);
            nueva.setFecha(fecha);
            nueva.setHoraInicio(inicio);
            nueva.setHoraFin(fin);
            nueva.setFuncionario(funcionarioLogueado);
            nueva.setRecursosAsignados(recursosAsignados);

            reservaService.crearReserva(nueva);
            cargarReservasActivas();


            view.mostrarMensajeExito("Reserva creada con éxito.");

        } catch (ValidationException ex) {

            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Ocurrió un error inesperado: " + ex.getMessage());
        }
    }

    public void cancelarReserva(String idReserva) {
        try {
            reservaService.cancelarReservaFutura(idReserva,funcionarioLogueado.getId());
            cargarReservasActivas();
            view.mostrarMensajeExito("La reserva ha sido cancelada correctamente y sus recursos están libres.");
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al cancelar la reserva: " + ex.getMessage());
        }
    }

    public void imprimirReservas(String destino, javax.swing.JTable tabla){
        try{
            new resourcemanager.logic.PdfService().print(destino, "Mis Reservas", null, tabla);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }
}
