package resourcemanager.presentation.calendarizacion;

import com.github.lgooddatepicker.components.DatePicker;
import resourcemanager.logic.Categoria;
import resourcemanager.logic.PdfService;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class Calendarizacion {
    private JPanel mainPanel;
    private JTable calendarizacionRecTable;
    private JComboBox<Categoria> categoriaComboBox;
    private JButton cargarButton;
    private JButton imprimirButton;
    private JLabel filtrosLabel;
    private JLabel fechaLabel;
    private JLabel categoriaLabel;
    private DatePicker fechaDatePicker;
    private JLabel calRecLabel;

    private CalendarizacionTableModel tableModel;

    public Calendarizacion() {
        tableModel = new CalendarizacionTableModel();
        calendarizacionRecTable.setModel(tableModel);

        imprimirButton.addActionListener(e -> imprimir());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public LocalDate getFechaSeleccionada() {
        return fechaDatePicker.getDate();
    }

    public Categoria getCategoriaSeleccionada() {
        return (Categoria) categoriaComboBox.getSelectedItem();
    }

    public CalendarizacionTableModel getTableModel() {
        return tableModel;
    }

    public JTable getCalendarizacionRecTable() {
        return calendarizacionRecTable;
    }

    public void cargarCategorias(List<Categoria> categorias) {
        categoriaComboBox.removeAllItems();
        for (Categoria c : categorias) {
            categoriaComboBox.addItem(c);
        }
    }

    public void addCargarListener(ActionListener listener) {
        cargarButton.addActionListener(listener);
    }

    private void imprimir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("calendarizacion.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(calendarizacionRecTable);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if (!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            try {
                List<String> columnas = TablaExportadora.extraerColumnas(calendarizacionRecTable);
                List<List<String>> filas = TablaExportadora.extraerFilas(calendarizacionRecTable);
                new PdfService().print(destino, "Calendarizacion de Recursos", null, columnas, filas);
                JOptionPane.showMessageDialog(calendarizacionRecTable, "PDF generado con éxito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(calendarizacionRecTable,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}