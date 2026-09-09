package resourcemanager.presentation.actividades;

import com.github.lgooddatepicker.components.DatePicker;
import resourcemanager.logic.PdfService;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class Actividades {
    private JButton cargarButton;
    private JButton imprimirButton;
    private JTable ActividadesSemanalesTable;
    private JLabel semanaLabel;
    private JLabel fechaRefLabel;
    private DatePicker fechaRefDatePicker;
    private JLabel actividSemLabel;

    public Actividades(){
        imprimirButton.addActionListener(e -> imprimir());
    }

    private void imprimir(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("actividades.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(ActividadesSemanalesTable);
        if(seleccion == JFileChooser.APPROVE_OPTION){
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if(!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            try {
                List<String> columnas = TablaExportadora.extraerColumnas(ActividadesSemanalesTable);
                List<List<String>> filas = TablaExportadora.extraerFilas(ActividadesSemanalesTable);
                new PdfService().print(destino, "Actividades Semanales", null, columnas, filas);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ActividadesSemanalesTable,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
