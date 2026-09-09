package resourcemanager.presentation.funcionarios;

import resourcemanager.logic.Funcionario;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class Funcionarios extends JPanel {
    private JTextField idBusqTextField;
    private JTextField nombreBusqTextField;
    private JButton buscarButton;
    private JButton imprimirButton;
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JTextField telefonoTextField;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JTable listadoFuncTable;
    private JLabel busquedaLabel;
    private JLabel idLabel;
    private JLabel nombreLabel;
    private JLabel funcionarioLabel;
    private JLabel idLabel1;
    private JLabel nombreLabel1;
    private JLabel telefonoLabel;
    private JLabel listadoLabel;
    private ControllerFuncionarios controllerFuncionarios;

    public Funcionarios() {
        guardarButton.addActionListener(e -> guardar());
        borrarButton.addActionListener(e -> borrar());
        limpiarButton.addActionListener(e -> limpiarCampos());
        buscarButton.addActionListener(e -> buscar());
        imprimirButton.addActionListener(e -> imprimir());

        listadoFuncTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFilaTabla();
            }
        });
    }

    public void setControllerFuncionarios(ControllerFuncionarios controllerFuncionarios) {
        this.controllerFuncionarios = controllerFuncionarios;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarTabla(List<Funcionario> funcionarios) {
        String[] columnas = {"ID", "Nombre", "Telefono"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Funcionario f : funcionarios) {
            modelo.addRow(new Object[]{f.getId(), f.getNombre(), f.getTelefono()});
        }
        listadoFuncTable.setModel(modelo);
    }

    public void limpiarCampos() {
        idTextField.setText("");
        nombreTextField.setText("");
        telefonoTextField.setText("");
        listadoFuncTable.clearSelection();
    }

    private void seleccionarFilaTabla() {
        int fila = listadoFuncTable.getSelectedRow();
        if (fila >= 0) {
            idTextField.setText(listadoFuncTable.getValueAt(fila, 0).toString());
            nombreTextField.setText(listadoFuncTable.getValueAt(fila, 1).toString());
            Object telefono = listadoFuncTable.getValueAt(fila, 2);
            telefonoTextField.setText(telefono != null ? telefono.toString() : "");
        }
    }

    private void guardar() {
        if (controllerFuncionarios == null) return;
        controllerFuncionarios.guardarFuncionario(
                idTextField.getText().trim(),
                nombreTextField.getText(),
                telefonoTextField.getText()
        );
    }

    private void borrar() {
        if (controllerFuncionarios == null) return;
        controllerFuncionarios.borrarFuncionario(idTextField.getText().trim());
    }

    private void buscar() {
        if (controllerFuncionarios == null) return;
        // El DAO busca por ID o por nombre con un solo termino; priorizamos el campo ID si viene lleno.
        String texto = !idBusqTextField.getText().trim().isEmpty()
                ? idBusqTextField.getText().trim()
                : nombreBusqTextField.getText().trim();
        controllerFuncionarios.buscarFuncionarios(texto);
    }

    private void imprimir() {
        if (controllerFuncionarios == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("funcionarios.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String destino = fileChooser.getSelectedFile().getAbsolutePath();
            if (!destino.toLowerCase().endsWith(".pdf")) {
                destino += ".pdf";
            }
            controllerFuncionarios.imprimirFuncionarios(destino, listadoFuncTable);
        }
    }
}
