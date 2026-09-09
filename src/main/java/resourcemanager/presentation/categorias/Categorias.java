package resourcemanager.presentation.categorias;

import resourcemanager.logic.Categoria;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class Categorias extends JPanel {
    private JTextField descripcionBusqTextField;
    private JButton buscarButton;
    private JButton imprimirButton;
    private JTextField textField2;
    private JTextField descripcionTextField;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTable categoriasListadoTable;
    private JLabel busquedaLabel;
    private JLabel descripcionBusqLabel;
    private JLabel categoriaLabel;
    private JLabel idLabel;
    private JLabel descripcionLabel;
    private JLabel listadoLabel;
    private ControllerCategorias controllerCategorias;

    public Categorias() {
        textField2.setEditable(false); // el ID se autogenera, no se escribe a mano

        guardarButton.addActionListener(e -> guardar());
        borrarButton.addActionListener(e -> borrar());
        limpiarButton.addActionListener(e -> limpiarCampos());
        buscarButton.addActionListener(e -> buscar());
        imprimirButton.addActionListener(e -> imprimir());

        categoriasListadoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFilaTabla();
            }
        });
    }

    public void setControllerCategorias(ControllerCategorias controllerCategorias) {
        this.controllerCategorias = controllerCategorias;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarTabla(List<Categoria> categorias) {
        String[] columnas = {"ID", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Categoria c : categorias) {
            modelo.addRow(new Object[]{c.getId(), c.getDescripcion()});
        }
        categoriasListadoTable.setModel(modelo);
    }

    public void limpiarCampos() {
        textField2.setText("");
        descripcionTextField.setText("");
        categoriasListadoTable.clearSelection();
    }

    private void seleccionarFilaTabla() {
        int fila = categoriasListadoTable.getSelectedRow();
        if (fila >= 0) {
            textField2.setText(categoriasListadoTable.getValueAt(fila, 0).toString());
            descripcionTextField.setText(categoriasListadoTable.getValueAt(fila, 1).toString());
        }
    }

    private void guardar() {
        if (controllerCategorias == null) return;
        controllerCategorias.guardarCategorias(textField2.getText().trim(), descripcionTextField.getText());
    }

    private void borrar() {
        if (controllerCategorias == null) return;
        controllerCategorias.borrarCategoria(textField2.getText().trim());
    }

    private void buscar() {
        if (controllerCategorias == null) return;
        controllerCategorias.buscarCategorias(descripcionBusqTextField.getText());
    }

    private void imprimir() {
        if (controllerCategorias == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("categorias.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if (!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            controllerCategorias.imprimirCategorias(destino, categoriasListadoTable);
        }
    }
}

