package resourcemanager.presentation.actividades;

import resourcemanager.logic.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ActividadesTableModel extends AbstractTableModel {
    private final String[] HORAS = {
            "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
    };

    private LocalDate inicioSemana;
    private List<Reserva> reservas = new ArrayList<>();

    public void setDatos(LocalDate fechaReferencia, List<Reserva> reservas) {
        if (fechaReferencia != null) {
            this.inicioSemana = fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else {
            this.inicioSemana = null;
        }
        this.reservas = (reservas != null) ? reservas : new ArrayList<>();
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return HORAS.length;
    }

    @Override
    public int getColumnCount() {
        return 8; // Hora + 7 días
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) return "Hora";
        if (inicioSemana == null) return "Día " + columnIndex;

        LocalDate dia = inicioSemana.plusDays(columnIndex - 1);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd");
        return dia.format(fmt);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String horaTexto = HORAS[rowIndex];
        if (columnIndex == 0) return horaTexto;
        if (inicioSemana == null) return "";

        LocalTime horaFila = LocalTime.parse(horaTexto);
        LocalDate diaColumna = inicioSemana.plusDays(columnIndex - 1);

        for (Reserva r : reservas) {
            if (r.getFecha().equals(diaColumna)) {
                if (!horaFila.isBefore(r.getHoraInicio()) && horaFila.isBefore(r.getHoraFin())) {
                    String funcNom = (r.getFuncionario() != null) ? r.getFuncionario().getNombre() : "N/A";
                    return r.getActividad() + " (" + funcNom + ")";
                }
            }
        }
        return "";
    }
}