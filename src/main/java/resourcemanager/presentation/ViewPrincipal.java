package resourcemanager.presentation;

import javax.swing.*;
import java.awt.*;

public class ViewPrincipal extends JFrame {
    private JTabbedPane tabbedPane;
    private JButton btnCambiarClave;
    private JButton btnLogout;

    public ViewPrincipal() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        btnCambiarClave = new JButton("Cambiar Clave");
        btnLogout = new JButton("Cerrar Sesión");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.add(btnCambiarClave);
        topPanel.add(btnLogout);

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /** Título estilo "SISTEMA DE RESERVAS - 111 (FUNCIONARIO)" como en el PDF. */
    public void setTituloUsuario(String id, String rol) {
        setTitle("SISTEMA DE RESERVAS - " + id + " (" + rol.toUpperCase() + ")");
    }

    public void agregarPestana(String titulo, Component contenido) {
        tabbedPane.addTab(titulo, contenido);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JButton getBtnCambiarClave() {
        return btnCambiarClave;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}