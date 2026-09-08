package resourcemanager.presentation.estadisticas;

import com.github.lgooddatepicker.components.DatePicker;
import resourcemanager.logic.PdfService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Estadisticas {
    private JTable estadisticasRecTable;
    private JTable estadisticasActTable;
    private JButton cargarRecButton;
    private JButton cargarActButton;
    private JLabel recursosLabel;
    private JLabel actividadesLabel;
    private JLabel fechasRecLabel;
    private JLabel fechasActLabel;
    private DatePicker desdeRecDatePicker;
    private DatePicker hastaRecDatePicker;
    private DatePicker desdeActDatePicker;
    private DatePicker hastaActDatePicker;
    private JLabel estadisticasRecLabel;
    private JLabel estadisticasActLabel;
    private JLabel grafRecLabel;
    private JLabel grafActLabel;
    private JPanel grafRecSpace;
    private JPanel grafActSpace;
    private JButton imprimirRecButton;
    private JButton imprimirActButton;

    public Estadisticas() {
        imprimirRecButton.addActionListener(e -> imprimir("estadisticas_recursos.pdf",
                "Estadisticas de Recursos", estadisticasRecTable));
        imprimirActButton.addActionListener(e -> imprimir("estadisticas_actividades.pdf",
                "Estadisticas de Actividades", estadisticasActTable));
    }

    private void imprimir(String nombreArchivo, String titulo, JTable tabla){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(nombreArchivo));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(tabla);
        if(seleccion == JFileChooser.APPROVE_OPTION){
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if(!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            try {
                new PdfService().print(destino, titulo, null, tabla);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tabla,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
