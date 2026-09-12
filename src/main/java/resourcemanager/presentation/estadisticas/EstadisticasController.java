package resourcemanager.presentation.estadisticas;

import resourcemanager.logic.ReservaService;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Map;

public class EstadisticasController {
    private final ModelEstadisticas model;
    private final Estadisticas view;
    private final ReservaService reservaService;

    public EstadisticasController(ModelEstadisticas model, Estadisticas view) {
        this.model = model;
        this.view = view;
        this.reservaService = ReservaService.getInstance();

        // Escuchar cambios de datos en el modelo
        this.model.addPropertyChangeListener(evt -> {
            if (ModelEstadisticas.DATOS_RECURSOS.equals(evt.getPropertyName())) {
                view.mostrarEstadisticasRecursos(model.getDatosRecursos());
            } else if (ModelEstadisticas.DATOS_ACTIVIDADES.equals(evt.getPropertyName())) {
                view.mostrarEstadisticasActividades(model.getDatosActividades());
            }
        });

        initController();
    }

    private void initController() {
        view.addCargarRecursosListener(e -> cargarEstadisticasRecursos());
        view.addCargarActividadesListener(e -> cargarEstadisticasActividades());
    }

    private void cargarEstadisticasRecursos() {
        try {
            LocalDate desde = view.getDesdeRecursos();
            LocalDate hasta = view.getHastaRecursos();

            if (desde == null || hasta == null || desde.isAfter(hasta)) {
                JOptionPane.showMessageDialog(null, "Rango de fechas inválido.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, Long> datos = reservaService.obtenerEstadisticasRecursos(desde, hasta);
            model.setDatosRecursos(datos);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar recursos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEstadisticasActividades() {
        try {
            LocalDate desde = view.getDesdeActividades();
            LocalDate hasta = view.getHastaActividades();

            if (desde == null || hasta == null || desde.isAfter(hasta)) {
                JOptionPane.showMessageDialog(null, "Rango de fechas inválido.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, Long> datos = reservaService.obtenerEstadisticasActividades(desde, hasta);
            model.setDatosActividades(datos);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar actividades: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}