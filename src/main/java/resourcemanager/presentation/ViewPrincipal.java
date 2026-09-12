package resourcemanager.presentation;
import javax.swing.*;
import java.awt.*;
public class ViewPrincipal extends JFrame{
    private JButton btnReservas;
    private JButton btnRecursos;
    private JButton btnFuncionarios;
    private JButton btnCategorias;
    private JButton btnEstadisticas;
    private JButton btnActividades;
    private JButton btnCalendarizacion;
    private JButton btnLogout;

    public ViewPrincipal() {
        setTitle("Sistema de Gestión de Recursos - UNA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnReservas = new JButton("Gestión de Reservas");
        btnRecursos = new JButton("Gestión de Recursos");
        btnFuncionarios = new JButton("Gestión de Funcionarios");
        btnCategorias = new JButton("Categorías");
        btnEstadisticas = new JButton("Estadísticas");
        btnActividades = new JButton("Actividades");
        btnCalendarizacion = new JButton("Calendarización");
        btnLogout = new JButton("Cerrar Sesión");

        panel.add(btnReservas);
        panel.add(btnRecursos);
        panel.add(btnFuncionarios);
        panel.add(btnCategorias);
        panel.add(btnEstadisticas);
        panel.add(btnActividades);
        panel.add(btnCalendarizacion);
        panel.add(btnLogout);

        add(panel);
    }


    public JButton getBtnReservas() { return btnReservas; }
    public JButton getBtnRecursos() { return btnRecursos; }
    public JButton getBtnFuncionarios() { return btnFuncionarios; }
    public JButton getBtnCategorias() { return btnCategorias; }
    public JButton getBtnEstadisticas() { return btnEstadisticas; }
    public JButton getBtnActividades() { return btnActividades; }
    public JButton getBtnCalendarizacion() { return btnCalendarizacion; }
    public JButton getBtnLogout() { return btnLogout; }


    public void aplicarPermisos(boolean esAdmin) {
        btnRecursos.setEnabled(esAdmin);
        btnFuncionarios.setEnabled(esAdmin);
        btnCategorias.setEnabled(esAdmin);

        btnReservas.setEnabled(true);
        btnActividades.setEnabled(true);
        btnCalendarizacion.setEnabled(true);
        btnEstadisticas.setEnabled(true);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
