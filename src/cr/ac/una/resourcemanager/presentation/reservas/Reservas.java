package cr.ac.una.resourcemanager.presentation.reservas;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import cr.ac.una.resourcemanager.logic.Categoria;
import cr.ac.una.resourcemanager.logic.ListaCategorias;
import cr.ac.una.resourcemanager.logic.UsuarioSession;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class Reservas extends Component {
    private JButton imprimirButton;
    private JTable misReservasTable;
    private JButton extraerButton;
    private JTextField fraseTextField;
    private JTextField actividadTextField;
    private JList categoriasList;
    private JButton reservarButton;
    private JButton cancelarReservaSeleccionadaButton;
    private JButton limpiarButton;
    private JLabel nuevaReservaLabel;
    private JLabel fraseLabel;
    private JLabel actividadLabel;
    private JLabel fechaLabel;
    private DatePicker fechaDatePicker;
    private JLabel horaInicioLabel;
    private TimePicker horaInicioTimePicker;
    private JLabel horaFinLabel;
    private TimePicker horaFinTimePicker;
    private JLabel categoriasRequeridasLabel;
    private JLabel categoriasLabel;
    private JLabel misReservasLabel;
    private ControllerReservas controllerReservas;

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
    public void setControllerReservas(ControllerReservas controllerReservas) {
        this.controllerReservas = controllerReservas;
    }
    private ListaCategorias getCategorias() {

        List<Categoria> seleccionadas = categoriasList.getSelectedValuesList();
        return new ListaCategorias(seleccionadas);
    }

    private void limpiarCampos() {
        actividadTextField.setText("");
        fechaDatePicker.setDate(null);
        horaInicioTimePicker.setTime(null);
        horaFinTimePicker.setTime(null);
        categoriasList.clearSelection();
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        if (controllerReservas != null) {
            try {
                String idReserva = UsuarioSession.getUsuario().getId();
                String actividad = actividadTextField.getText().trim();


                LocalDate fecha = fechaDatePicker.getDate();
                LocalTime inicio = horaInicioTimePicker.getTime();
                LocalTime fin = horaFinTimePicker.getTime();

                if (fecha == null || inicio == null || fin == null) {
                    mostrarError("Debe seleccionar la fecha, la hora de inicio y la hora de fin.");
                    return;
                }

                ListaCategorias categoriasSeleccionadas = getCategorias();

                controllerReservas.guardarReserva(
                        idReserva,
                        actividad,
                        fecha,
                        inicio,
                        fin,
                        categoriasSeleccionadas.getCategorias()
                );

                limpiarCampos();

            } catch (Exception ex) {
                mostrarError("Error al procesar el formulario: " + ex.getMessage());
            }
        }
    }
}
