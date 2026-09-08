package resourcemanager.presentation.calendaricacion;

import com.github.lgooddatepicker.components.DatePicker;
import resourcemanager.logic.PdfService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

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
                new PdfService().print(destino, "Calendarizacion de Recursos", null, calendarizacionRecTable);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(calendarizacionRecTable,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
