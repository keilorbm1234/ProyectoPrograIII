package resourcemanager.presentation.recursos;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

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
	}

	public void setControllerRecursos(ControllerRecursos controllerRecursos) {
		this.controllerRecursos = controllerRecursos;
	}

	public void mostrarError(String mensaje){
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
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
