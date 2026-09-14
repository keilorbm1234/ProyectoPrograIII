package resourcemanager.presentation.estadisticas;

import com.github.lgooddatepicker.components.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import resourcemanager.logic.PdfService;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Estadisticas {
    private JPanel mainPanel;
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
        // Inicializar tablas con sus estructuras
        estadisticasRecTable.setModel(new DefaultTableModel(new Object[]{"Categoría", "Cantidad"}, 0));
        estadisticasActTable.setModel(new DefaultTableModel(new Object[]{"Semana", "Cantidad"}, 0));

        imprimirRecButton.addActionListener(e -> imprimir("estadisticas_recursos.pdf",
                "Estadisticas de Recursos", estadisticasRecTable));
        imprimirActButton.addActionListener(e -> imprimir("estadisticas_actividades.pdf",
                "Estadisticas de Actividades", estadisticasActTable));
    }

    public JPanel getMainPanel() { return mainPanel; }

    public LocalDate getDesdeRecursos() { return desdeRecDatePicker.getDate(); }
    public LocalDate getHastaRecursos() { return hastaRecDatePicker.getDate(); }
    public LocalDate getDesdeActividades() { return desdeActDatePicker.getDate(); }
    public LocalDate getHastaActividades() { return hastaActDatePicker.getDate(); }

    public void addCargarRecursosListener(ActionListener listener) {
        cargarRecButton.addActionListener(listener);
    }

    public void addCargarActividadesListener(ActionListener listener) {
        cargarActButton.addActionListener(listener);
    }

    // Renderizar tabla y gráfico de barra para Recursos
    public void mostrarEstadisticasRecursos(Map<String, Long> datos) {
        DefaultTableModel model = (DefaultTableModel) estadisticasRecTable.getModel();
        model.setRowCount(0);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            dataset.addValue(entry.getValue(), "Cantidad", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Recursos Usados", "Categoría", "Cantidad",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        grafRecSpace.removeAll();
        grafRecSpace.setLayout(new BorderLayout());
        grafRecSpace.add(new ChartPanel(chart), BorderLayout.CENTER);
        grafRecSpace.revalidate();
        grafRecSpace.repaint();
    }

    // Renderizar tabla y gráfico de barra para Actividades
    public void mostrarEstadisticasActividades(Map<String, Long> datos) {
        DefaultTableModel model = (DefaultTableModel) estadisticasActTable.getModel();
        model.setRowCount(0);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            dataset.addValue(entry.getValue(), "Cantidad", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Actividades Realizadas", "Semana", "Cantidad",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        grafActSpace.removeAll();
        grafActSpace.setLayout(new BorderLayout());
        grafActSpace.add(new ChartPanel(chart), BorderLayout.CENTER);
        grafActSpace.revalidate();
        grafActSpace.repaint();
    }

    private void imprimir(String nombreArchivo, String titulo, JTable tabla) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(nombreArchivo));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(tabla);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if (!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            try {
                List<String> columnas = TablaExportadora.extraerColumnas(tabla);
                List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
                new PdfService().print(destino, titulo, null, columnas, filas);
                JOptionPane.showMessageDialog(tabla, "PDF generado con éxito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(tabla,
                        "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}