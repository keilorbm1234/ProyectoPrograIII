package resourcemanager.presentation.calendarizacion;

import resourcemanager.logic.Recurso;
import resourcemanager.logic.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarizacionTableModel extends AbstractTableModel {
    private final String[] HORAS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
    };

    private List<Recurso> recursos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();

    // Este método es llamado cuando el Model notifyChanges
    public void setDatos(List<Recurso> recursos, List<Reserva> reservas) {
        this.recursos = (recursos != null) ? recursos : new ArrayList<>();
        this.reservas = (reservas != null) ? reservas : new ArrayList<>();
        // Notifica a la JTable que la estructura de columnas (recursos) y filas cambió
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return HORAS.length;
    }

    @Override
    public int getColumnCount() {
        return recursos.size() + 1; // Columna 0 (Hora) + N Recursos
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) return "Hora";
        Recurso r = recursos.get(columnIndex - 1);
        return r.getDescripcion() + " (" + r.getId() + ")";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String horaTexto = HORAS[rowIndex];
        if (columnIndex == 0) return horaTexto;

        Recurso recursoColumna = recursos.get(columnIndex - 1);
        LocalTime horaFila = LocalTime.parse(horaTexto);

        for (Reserva res : reservas) {
            boolean asignado = res.getRecursosAsignados() != null &&
                    res.getRecursosAsignados().stream().anyMatch(r -> r.getId().equals(recursoColumna.getId()));

            if (asignado) {
                if (!horaFila.isBefore(res.getHoraInicio()) && horaFila.isBefore(res.getHoraFin())) {
                    String nom = (res.getFuncionario() != null) ? res.getFuncionario().getNombre() : "N/A";
                    return res.getActividad() + " (" + nom + ")";
                }
            }
        }
        return "";
    }
}