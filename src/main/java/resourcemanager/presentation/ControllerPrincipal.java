package resourcemanager.presentation;
import resourcemanager.data.RecursoXmlDao;
import resourcemanager.logic.Funcionario;
import resourcemanager.logic.ReservaService;
import resourcemanager.presentation.login.SessionManager;
import resourcemanager.logic.UsuarioSession;
import resourcemanager.presentation.login.ViewLogin;
import resourcemanager.presentation.recursos.ControllerRecursos;
import resourcemanager.presentation.recursos.ModelRecurso;
import resourcemanager.presentation.recursos.Recursos;
import resourcemanager.presentation.reservas.ControllerReservas;
import resourcemanager.presentation.reservas.ModelReserva;
import resourcemanager.presentation.reservas.Reservas;

import javax.swing.*;

public class ControllerPrincipal {
    private final ViewPrincipal view;

    public ControllerPrincipal(ViewPrincipal view) {
            this.view = view;
            initController();
            configurarVista();
        }

        public void configurarVista() {

            boolean esAdmin = SessionManager.esAdmin();
            view.aplicarPermisos(esAdmin);
            view.setVisible(true);
        }

        private void initController() {
            view.getBtnReservas().addActionListener(e -> abrirReservas());

            // Conectar los botones de los módulos administrativos
            if (view.getBtnRecursos() != null) {
                view.getBtnRecursos().addActionListener(e -> abrirRecursos());
            }
            if (view.getBtnFuncionarios() != null) {
                view.getBtnFuncionarios().addActionListener(e -> abrirFuncionarios());
            }

            view.getBtnLogout().addActionListener(e -> cerrarSesion());

        }
    private void abrirRecursos() {
        if (!SessionManager.esAdmin()) {
            view.mostrarMensaje("Acceso denegado: No tiene permisos de administrador.");
            return;
        }

        try {
            RecursoXmlDao recursoDao = new RecursoXmlDao();
            ModelRecurso modelRecurso = new ModelRecurso();
            Recursos vistaRecursos = new Recursos();

            ControllerRecursos controllerRecursos = new ControllerRecursos(
                    modelRecurso,
                    vistaRecursos,
                    recursoDao
            );

            vistaRecursos.setControllerRecursos(controllerRecursos);


            JDialog ventanaContainer = new JDialog(view, "Gestión de Recursos", true);
            ventanaContainer.getContentPane().add(vistaRecursos);
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir el módulo de Recursos: " + ex.getMessage());
        }
    }

    private void abrirFuncionarios() {
        if (!SessionManager.esAdmin()) {
            view.mostrarMensaje("Acceso denegado: No tiene permisos de administrador.");
        }
    }

        private void abrirReservas() {
            try {
                ReservaService reservaService = ReservaService.getInstance();
                Funcionario funcionarioActual = (Funcionario) UsuarioSession.getUsuario();
                ModelReserva modelReserva = new ModelReserva();
                Reservas vistaReservas = new Reservas();

                ControllerReservas controllerReservas = new ControllerReservas(
                        modelReserva,
                        vistaReservas,
                        reservaService,
                        funcionarioActual
                );

                vistaReservas.setControllerReservas(controllerReservas);

                JDialog ventanaContainer = new JDialog(view, "Gestión de Reservas", true);
                ventanaContainer.getContentPane().add(vistaReservas);
                ventanaContainer.pack();
                ventanaContainer.setLocationRelativeTo(view);
                ventanaContainer.setVisible(true);

            } catch (Exception ex) {
                view.mostrarMensaje("Error al abrir el módulo de Reservas: " + ex.getMessage());
            }
        }

        private void cerrarSesion() {
            UsuarioSession.logout();
            view.dispose();


            ViewLogin login = new ViewLogin();
            login.pack();
            login.setVisible(true);
        }
}
