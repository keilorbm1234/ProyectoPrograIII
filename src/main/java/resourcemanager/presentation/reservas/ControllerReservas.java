package resourcemanager.presentation.reservas;

import resourcemanager.data.RecursoXmlDao;
import resourcemanager.logic.*;
import resourcemanager.data.ReservaXmlDao;
import resourcemanager.presentation.TablaExportadora;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ControllerReservas {
    private final ModelReserva model;
    private final Reservas view;
    private final ReservaService reservaService;
    private final Funcionario funcionarioLogueado;
    private final ReservaIAService reservaIAService;

    public ControllerReservas(ModelReserva model, Reservas view, ReservaService reservaService, Funcionario funcionarioLogueado) {
        this.model = model;
        this.view = view;
        this.reservaService = reservaService;
        this.funcionarioLogueado = funcionarioLogueado;
        this.reservaIAService = ReservaIAService.getInstance();
        this.model.addPropertyChangeListener((PropertyChangeListener) this.view);

        cargarReservasActivas();
        cargarCategoriasDisponibles();
    }

    public void cargarReservasActivas() {
        try {
            List<Reserva> activas = reservaService.obtenerReservasActivasPorFuncionario(funcionarioLogueado.getId());
            model.setReservas(activas);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar las reservas: " + ex.getMessage());
        }
    }

    public void cargarCategoriasDisponibles() {
        try {
            List<Categoria> categorias = CategoriaService.getInstance().getAllCategorias();
            view.cargarCategorias(categorias);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar las categorías: " + ex.getMessage());
        }
    }

    public void guardarReserva(String idIgnorado, String actividad, LocalDate fecha, LocalTime inicio, LocalTime fin, List<Categoria> categorias) {
        try {
            String idReservaUnico = "RES-" + java.util.UUID.randomUUID().toString().substring(0, 8);

            List<Recurso> recursosAsignados = reservaService.asignarRecursosDisponibles(fecha, inicio, fin, categorias);

            Reserva nueva = new Reserva();
            nueva.setId(idReservaUnico);
            nueva.setActividad(actividad);
            nueva.setFecha(fecha);
            nueva.setHoraInicio(inicio);
            nueva.setHoraFin(fin);
            nueva.setFuncionario(funcionarioLogueado);
            nueva.setRecursosAsignados(recursosAsignados);

            reservaService.crearReserva(nueva);
            cargarReservasActivas();

            view.mostrarMensajeExito("Reserva creada con éxito. Código: " + idReservaUnico);

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

    public void extraerReservaDesdeFrase(String frase) {
        try {
            ReservaExtraccion datos = reservaIAService.extraerDatosReserva(frase);

            LocalDate fecha = parsearFecha(datos.getFecha());
            LocalTime horaInicio = parsearHora(datos.getHoraInicio());
            LocalTime horaFin = parsearHora(datos.getHoraFinal());

            view.aplicarDatosExtraidos(datos.getActividad(), fecha, horaInicio, horaFin, datos.getCategoriasRecurso());

        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("No se pudo completar el formulario con IA: " + ex.getMessage());
        }
    }

    private LocalDate parsearFecha(String texto) {
        try {
            return (texto == null || texto.isBlank()) ? null : LocalDate.parse(texto.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalTime parsearHora(String texto) {
        try {
            return (texto == null || texto.isBlank()) ? null : LocalTime.parse(texto.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public void imprimirReservas(String destino, javax.swing.JTable tabla){
        try{
            List<String> columnas = TablaExportadora.extraerColumnas(tabla);
            List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
            new resourcemanager.logic.PdfService().print(destino, "Lista de Reservas", null, columnas, filas);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }

}
