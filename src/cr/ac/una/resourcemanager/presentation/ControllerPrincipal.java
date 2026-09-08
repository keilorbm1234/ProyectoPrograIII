package cr.ac.una.resourcemanager.presentation;
import cr.ac.una.resourcemanager.data.RecursoXmlDao;
import cr.ac.una.resourcemanager.data.ReservaXmlDao;
import cr.ac.una.resourcemanager.logic.Funcionario;
import cr.ac.una.resourcemanager.logic.ReservaService;
import cr.ac.una.resourcemanager.presentation.login.SessionManager;
import cr.ac.una.resourcemanager.logic.Usuario;
import cr.ac.una.resourcemanager.logic.UsuarioSession;
import cr.ac.una.resourcemanager.presentation.login.ViewLogin;
import cr.ac.una.resourcemanager.presentation.recursos.ControllerRecursos;
import cr.ac.una.resourcemanager.presentation.recursos.ModelRecurso;
import cr.ac.una.resourcemanager.presentation.recursos.Recursos;
import cr.ac.una.resourcemanager.presentation.reservas.ControllerReservas;
import cr.ac.una.resourcemanager.presentation.reservas.ModelReserva;
import cr.ac.una.resourcemanager.presentation.reservas.Reservas;

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
            Recursos vistaRecursos = new Recursos(); // Asumiendo que la vista existe

            ControllerRecursos controllerRecursos = new ControllerRecursos(
                    modelRecurso,
                    vistaRecursos,
                    recursoDao
            );

            // vistaRecursos.setController(controllerRecursos);

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
            return;
        }

        // Aquí abrirás el JDialog de Funcionarios
    }

        private void abrirReservas() {
            try {
                ReservaXmlDao reservaDao = new ReservaXmlDao();
                RecursoXmlDao recursoDao = new RecursoXmlDao();
                ReservaService reservaService = new ReservaService(reservaDao, recursoDao);

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
                ventanaContainer.getContentPane().add(vistaReservas); // Agregamos tu Component
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
