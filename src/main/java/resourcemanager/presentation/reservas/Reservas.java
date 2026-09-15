package resourcemanager.presentation.reservas;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import resourcemanager.logic.Categoria;
import resourcemanager.logic.ListaCategorias;
import resourcemanager.logic.UsuarioSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.DefaultTableModel;
import resourcemanager.logic.Reserva;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class Reservas extends JPanel implements PropertyChangeListener {
    private JPanel mainPanel;
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

    public Reservas(){
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        imprimirButton.addActionListener(e -> imprimir());
        reservarButton.addActionListener(this::btnGuardarActionPerformed);
        limpiarButton.addActionListener(e -> limpiarCampos());
        extraerButton.addActionListener(e -> onExtraer());
        cancelarReservaSeleccionadaButton.addActionListener(e -> cancelarSeleccionada());
    }

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

                if(actividad.isEmpty()){
                    mostrarError("Debe ingresar una actividad.");
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

    private void imprimir() {
        if (controllerReservas == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("mis_reservas.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if (!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            controllerReservas.imprimirReservas(destino, misReservasTable);
        }
    }

    public void cargarCategorias(List<Categoria> categorias) {
        categoriasList.setListData(categorias.toArray());
    }

    private void onExtraer() {
        if (controllerReservas == null) return;

        String frase = fraseTextField.getText().trim();
        if (frase.isEmpty()) {
            mostrarError("Escriba una frase describiendo la reserva antes de extraer.");
            return;
        }

        extraerButton.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                controllerReservas.extraerReservaDesdeFrase(frase);
                return null;
            }

            @Override
            protected void done() {
                extraerButton.setEnabled(true);
            }
        };
        worker.execute();
    }

    public void aplicarDatosExtraidos(String actividad, LocalDate fecha, LocalTime horaInicio,
                                      LocalTime horaFin, List<String> nombresCategorias) {
        SwingUtilities.invokeLater(() -> {
            if (actividad != null && !actividad.isBlank()) {
                actividadTextField.setText(actividad);
            }
            if (fecha != null) {
                fechaDatePicker.setDate(fecha);
            }
            if (horaInicio != null) {
                horaInicioTimePicker.setTime(horaInicio);
            }
            if (horaFin != null) {
                horaFinTimePicker.setTime(horaFin);
            }

            if (nombresCategorias != null && !nombresCategorias.isEmpty()) {
                List<Integer> indicesSeleccionados = new ArrayList<>();
                javax.swing.ListModel<?> modelo = categoriasList.getModel();
                for (int i = 0; i < modelo.getSize(); i++) {
                    Categoria c = (Categoria) modelo.getElementAt(i);
                    boolean coincide = nombresCategorias.stream()
                            .anyMatch(nombre -> nombre.equalsIgnoreCase(c.getDescripcion()));
                    if (coincide) {
                        indicesSeleccionados.add(i);
                    }
                }
                int[] indices = indicesSeleccionados.stream().mapToInt(Integer::intValue).toArray();
                categoriasList.setSelectedIndices(indices);
            }

            mostrarMensajeExito("Se completó el formulario con IA. Puede editarlo antes de reservar.");
        });
    }

    private void cancelarSeleccionada() {
        int fila = misReservasTable.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione una reserva de la tabla para cancelarla.");
            return;
        }
        String idReserva = misReservasTable.getValueAt(fila, 0).toString();
        controllerReservas.cancelarReserva(idReserva);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(ModelReserva.LISTA_RESERVAS.equals(evt.getPropertyName())){
            cargarTabla((List<Reserva>) evt.getNewValue());
        }
    }

    private void cargarTabla(List<Reserva> reservas) {
        String[] columnas = {"ID", "Actividad", "Fecha", "Hora Inicio", "Hora Fin", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Reserva r : reservas) {
            modelo.addRow(new Object[]{
                    r.getId(), r.getActividad(), r.getFecha(),
                    r.getHoraInicio(), r.getHoraFin(), r.getEstado()
            });
        }
        misReservasTable.setModel(modelo);
    }
}