package resourcemanager.presentation;

import resourcemanager.presentation.actividades.Actividades;
import resourcemanager.presentation.calendarizacion.Calendarizacion;
import resourcemanager.presentation.categorias.Categorias;
import resourcemanager.presentation.estadisticas.Estadisticas;
import resourcemanager.presentation.funcionarios.Funcionarios;
import resourcemanager.presentation.recursos.Recursos;
import resourcemanager.presentation.reservas.ControllerReservas;
import resourcemanager.presentation.reservas.Reservas;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Objects;

public class ViewPrincipal extends JFrame {
    private JTabbedPane tabbedPane;
    private JButton btnCambiarClave;
    private JButton btnLogout;
    private ControllerReservas controllerReservas;

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

        tabbedPane.addChangeListener(e -> {
            Component pestanaActual = tabbedPane.getSelectedComponent();
            if (pestanaActual instanceof Reservas && controllerReservas != null) {
                controllerReservas.cargarCategoriasDisponibles();
            }
        });

        try {
            setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon.png"))).getImage());
            btnCambiarClave.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/clave.png"))));
            btnLogout.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/login.png"))));
        } catch (Exception e) {
            System.out.println("Error al cargar iconos en la ventana principal: " + e.getMessage());
        }
    }
    public void setControllerReservas(ControllerReservas controllerReservas) {
        this.controllerReservas = controllerReservas;
    }

    public void setTituloUsuario(String id, String rol) {
        setTitle("SISTEMA DE RESERVAS - " + id + " (" + rol.toUpperCase() + ")");
    }

    public void agregarPestana(String titulo, Icon icono, Component contenido) {
        tabbedPane.addTab(titulo, icono, contenido);
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