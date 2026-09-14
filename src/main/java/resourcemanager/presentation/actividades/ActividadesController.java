package resourcemanager.presentation.actividades;

import resourcemanager.logic.Reserva;
import resourcemanager.logic.ReservaService;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class ActividadesController {
    private final ModelActividades model;
    private final Actividades view;
    private final ReservaService reservaService;

    public ActividadesController(ModelActividades model, Actividades view) {
        this.model = model;
        this.view = view;
        this.reservaService = ReservaService.getInstance();

        // Escuchar cambios en el modelo para actualizar la tabla
        this.model.addPropertyChangeListener(evt -> {
            view.getTableModel().setDatos(model.getInicioSemana(), model.getReservas());
        });

        initController();
    }

    private void initController() {
        view.addCargarListener(e -> cargarMatriz());
    }

    private void cargarMatriz() {
        try {
            LocalDate fecha = view.getFechaSeleccionada();
            if (fecha == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fecha de referencia.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate inicioSemana = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate finSemana = inicioSemana.plusDays(6);

            List<Reserva> reservas = reservaService.obtenerReservasPorRangoFechas(inicioSemana, finSemana);

            // Actualizar modelo
            model.setDatos(inicioSemana, reservas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar las actividades: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
