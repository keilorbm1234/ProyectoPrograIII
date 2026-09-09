package resourcemanager.presentation.recursos;

import resourcemanager.logic.Categoria;
import resourcemanager.logic.Recurso;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.List;

public class Recursos extends JPanel {
	private JTable recursosListadoTable;
	private JComboBox categoriaFiltroComboBox;
	private JTextField descripcionFiltrotextField;
	private JButton buscarButton;
	private JButton imprimirButton;
	private JTextField idTextField;
	private JComboBox categoriaComboBox;
	private JTextField descripcionTextField;
	private JButton guardarButton;
	private JButton borrarButton;
	private JButton limpiarButton;
	private JLabel filtroLabel;
	private JLabel categoriaFiltroLabel;
	private JLabel descripcionFIltroLabel;
	private JLabel recursoLabel;
	private JLabel idLabel;
	private JLabel categoriaLabel;
	private JLabel descripcionLabel;
	private JLabel listadoLabel;
	private ControllerRecursos controllerRecursos;

	public Recursos(){
		imprimirButton.addActionListener(e -> imprimir());
		guardarButton.addActionListener(e -> guardar());
		borrarButton.addActionListener(e -> borrar());
		limpiarButton.addActionListener(e -> limpiarCampos());
		buscarButton.addActionListener(e -> buscar());

		recursosListadoTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				seleccionarFilaTabla();
			}
		});
	}

	public void setControllerRecursos(ControllerRecursos controllerRecursos) {
		this.controllerRecursos = controllerRecursos;
	}

	public void mostrarError(String mensaje){
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarMensajeExito(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
	}

	public void cargarCategorias(List<Categoria> categorias) {
		categoriaComboBox.removeAllItems();
		categoriaFiltroComboBox.removeAllItems();

		categoriaFiltroComboBox.addItem(null); // representa "Todas las categorias"
		for (Categoria c : categorias) {
			categoriaComboBox.addItem(c);
			categoriaFiltroComboBox.addItem(c);
		}
	}

	public void cargarTabla(List<Recurso> recursos) {
		String[] columnas = {"ID", "Categoria", "Descripción"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (Recurso r : recursos) {
			modelo.addRow(new Object[]{
					r.getId(),
					r.getCategoria() != null ? r.getCategoria().getDescripcion() : "",
					r.getDescripcion()
			});
		}
		recursosListadoTable.setModel(modelo);
	}

	public void limpiarCampos() {
		idTextField.setText("");
		descripcionTextField.setText("");
		categoriaComboBox.setSelectedItem(null);
		recursosListadoTable.clearSelection();
	}

	private void seleccionarFilaTabla() {
		int fila = recursosListadoTable.getSelectedRow();
		if (fila >= 0) {
			idTextField.setText(recursosListadoTable.getValueAt(fila, 0).toString());
			descripcionTextField.setText(recursosListadoTable.getValueAt(fila, 2).toString());

			String descripcionCategoria = recursosListadoTable.getValueAt(fila, 1).toString();
			for (int i = 0; i < categoriaComboBox.getItemCount(); i++) {
				Categoria c = (Categoria) categoriaComboBox.getItemAt(i);
				if (c != null && c.getDescripcion().equals(descripcionCategoria)) {
					categoriaComboBox.setSelectedItem(c);
					break;
				}
			}
		}
	}

	private void guardar() {
		if (controllerRecursos == null) return;
		Categoria categoriaSeleccionada = (Categoria) categoriaComboBox.getSelectedItem();
		controllerRecursos.guardarRecurso(idTextField.getText().trim(), categoriaSeleccionada, descripcionTextField.getText());
	}

	private void borrar() {
		if (controllerRecursos == null) return;
		controllerRecursos.borrarRecurso(idTextField.getText().trim());
	}

	private void buscar() {
		if (controllerRecursos == null) return;
		Categoria categoriaFiltro = (Categoria) categoriaFiltroComboBox.getSelectedItem();
		String categoriaId = categoriaFiltro != null ? categoriaFiltro.getId() : null;
		controllerRecursos.buscarRecursos(categoriaId, descripcionFiltrotextField.getText());
	}

	private void imprimir(){
		if(controllerRecursos == null) return;

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File("recursos.pdf"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

		int seleccion = fileChooser.showSaveDialog(this);
		if (seleccion == JFileChooser.APPROVE_OPTION) {
			String destino = fileChooser.getSelectedFile().getAbsolutePath();
			if (!destino.toLowerCase().endsWith(".pdf")) {
				destino += ".pdf";
			}
			controllerRecursos.imprimirRecursos(destino, recursosListadoTable);
		}
	}
}

