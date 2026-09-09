package resourcemanager.presentation.calendaricacion;

import com.github.lgooddatepicker.components.DatePicker;
import resourcemanager.logic.PdfService;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class Calendarizacion {
    private JTable calendarizacionRecTable;
    private JComboBox categoriaComboBox;
    private JButton cargarButton;
    private JButton imprimirButton;
    private JLabel filtrosLabel;
    private JLabel fechaLabel;
    private JLabel categoriaLabel;
    private DatePicker fechaDatePicker;
    private JLabel calRecLabel;

    public Calendarizacion(){
        imprimirButton.addActionListener(e -> imprimir());
    }

    private void imprimir(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("calendarizacion.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(calendarizacionRecTable);
        if(seleccion == JFileChooser.APPROVE_OPTION){
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if(!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            try {
                List<String> columnas = TablaExportadora.extraerColumnas(calendarizacionRecTable);
                List<List<String>> filas = TablaExportadora.extraerFilas(calendarizacionRecTable);
                new PdfService().print(destino, "Calendarizacion de Recursos", null, columnas, filas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(calendarizacionRecTable,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
