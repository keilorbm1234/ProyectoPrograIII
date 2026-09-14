package resourcemanager.presentation.calendarizacion;

import resourcemanager.logic.*;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class CalendarizacionController {
    private final ModelCalendarizacion model;
    private final Calendarizacion view;
    private final ReservaService reservaService;
    private final RecursoService recursoService;
    private final CategoriaService categoriaService;

    public CalendarizacionController(ModelCalendarizacion model, Calendarizacion view) {
        this.model = model;
        this.view = view;
        this.reservaService = ReservaService.getInstance();
        this.recursoService = RecursoService.getInstance();
        this.categoriaService = CategoriaService.getInstance();

        // Escuchar cambios en el modelo para actualizar la tabla
        this.model.addPropertyChangeListener(evt -> {
            view.getTableModel().setDatos(model.getRecursos(), model.getReservas());
        });

        initController();
    }

    private void initController() {
        try {
            List<Categoria> categorias = categoriaService.getAllCategorias();
            view.cargarCategorias(categorias);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorías: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        view.addCargarListener(e -> cargarMatriz());
    }

    private void cargarMatriz() {
        try {
            LocalDate fecha = view.getFechaSeleccionada();
            if (fecha == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fecha válida.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Categoria categoria = view.getCategoriaSeleccionada();
            if (categoria == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una categoría.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Recurso> recursos = recursoService.filtrarPorCategoria(categoria.getId());
            List<Reserva> reservas = reservaService.obtenerReservasPorFechaYCategoria(fecha, categoria.getId());

            // Actualizar el modelo (desencadena el PropertyChangeListener)
            model.setRecursos(recursos);
            model.setReservas(reservas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar la calendarización: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}