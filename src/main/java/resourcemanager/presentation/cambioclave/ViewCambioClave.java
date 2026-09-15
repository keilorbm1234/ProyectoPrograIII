package resourcemanager.presentation.cambioclave;

import resourcemanager.logic.Funcionario;

import javax.swing.*;
import java.awt.*;

public class ViewCambioClave extends JDialog {
    private final JPasswordField claveActualField;
    private final JPasswordField claveNuevaField;
    private final JPasswordField confirmarClaveField;
    private final JButton btnGuardar;
    private final JButton btnCancelar;

    private ControllerCambioClave controller;

    public ViewCambioClave(Window owner, Funcionario usuario) {
        super(owner, "Cambiar Clave", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        claveActualField = new JPasswordField(15);
        claveNuevaField = new JPasswordField(15);
        confirmarClaveField = new JPasswordField(15);
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        String nombreUsuario = usuario != null ? usuario.getNombre() : "";
        String idUsuario = usuario != null ? usuario.getId() : "";
        panelFormulario.add(new JLabel("Usuario: " + nombreUsuario + " (" + idUsuario + ")"), gbc);
        gbc.gridwidth = 1;

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(new JLabel("Clave actual:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(claveActualField, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(new JLabel("Clave nueva:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(claveNuevaField, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelFormulario.add(new JLabel("Confirmar clave nueva:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(confirmarClaveField, gbc);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        getRootPane().setDefaultButton(btnGuardar);
        setLayout(new BorderLayout());
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> onGuardar());
        btnCancelar.addActionListener(e -> dispose());
    }

    public void setController(ControllerCambioClave controller) {
        this.controller = controller;
    }

    private void onGuardar() {
        if (controller == null) {
            return;
        }

        String claveActual = new String(claveActualField.getPassword());
        String claveNueva = new String(claveNuevaField.getPassword());
        String confirmacion = new String(confirmarClaveField.getPassword());

        if (claveNueva.isEmpty() || claveActual.isEmpty()) {
            mostrarError("Debe completar la clave actual y la nueva clave.");
            return;
        }
        if (!claveNueva.equals(confirmacion)) {
            mostrarError("La nueva clave y su confirmación no coinciden.");
            return;
        }

        controller.cambiarClave(claveActual, claveNueva);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
