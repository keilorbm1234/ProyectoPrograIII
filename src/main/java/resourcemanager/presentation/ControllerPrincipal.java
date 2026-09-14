package resourcemanager.presentation;

import resourcemanager.logic.CategoriaService;
import resourcemanager.logic.FuncionarioService;
import resourcemanager.logic.RecursoService;
import resourcemanager.logic.Funcionario;
import resourcemanager.logic.ReservaService;
import resourcemanager.presentation.actividades.Actividades;
import resourcemanager.presentation.actividades.ActividadesController;
import resourcemanager.presentation.calendarizacion.Calendarizacion;
import resourcemanager.presentation.calendarizacion.CalendarizacionController;
import resourcemanager.presentation.categorias.Categorias;
import resourcemanager.presentation.categorias.ControllerCategorias;
import resourcemanager.presentation.categorias.ModelCategoria;
import resourcemanager.presentation.funcionarios.ControllerFuncionarios;
import resourcemanager.presentation.funcionarios.Funcionarios;
import resourcemanager.presentation.funcionarios.ModelFuncionario;
import resourcemanager.presentation.login.SessionManager;
import resourcemanager.logic.UsuarioSession;
import resourcemanager.presentation.login.ViewLogin;
import resourcemanager.presentation.recursos.ControllerRecursos;
import resourcemanager.presentation.recursos.ModelRecurso;
import resourcemanager.presentation.recursos.Recursos;
import resourcemanager.presentation.reservas.ControllerReservas;
import resourcemanager.presentation.reservas.ModelReserva;
import resourcemanager.presentation.reservas.Reservas;
import resourcemanager.presentation.estadisticas.ModelEstadisticas;
import resourcemanager.presentation.estadisticas.Estadisticas;
import resourcemanager.presentation.estadisticas.EstadisticasController;
import resourcemanager.presentation.calendarizacion.ModelCalendarizacion;
import resourcemanager.presentation.calendarizacion.Calendarizacion;
import resourcemanager.presentation.calendarizacion.CalendarizacionController;
import resourcemanager.presentation.actividades.ModelActividades;
import resourcemanager.presentation.actividades.Actividades;
import resourcemanager.presentation.actividades.ActividadesController;

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

        if (view.getBtnRecursos() != null) {
            view.getBtnRecursos().addActionListener(e -> abrirRecursos());
        }
        if (view.getBtnFuncionarios() != null) {
            view.getBtnFuncionarios().addActionListener(e -> abrirFuncionarios());
        }
        if (view.getBtnCategorias() != null) {
            view.getBtnCategorias().addActionListener(e -> abrirCategorias());
        }

        // Listeners para matrices y estadísticas
        if (view.getBtnCalendarizacion() != null) {
            view.getBtnCalendarizacion().addActionListener(e -> abrirCalendarizacion());
        }
        if (view.getBtnActividades() != null) {
            view.getBtnActividades().addActionListener(e -> abrirActividades());
        }
        if (view.getBtnEstadisticas() != null) {
            view.getBtnEstadisticas().addActionListener(e -> abrirEstadisticas());
        }

        view.getBtnLogout().addActionListener(e -> cerrarSesion());
    }

    private void abrirEstadisticas() {
        try {
            ModelEstadisticas model = new ModelEstadisticas();
            Estadisticas vistaEst = new Estadisticas();
            EstadisticasController controller = new EstadisticasController(model, vistaEst);

            JDialog ventanaContainer = new JDialog(view, "Estadísticas del Sistema", true);
            ventanaContainer.getContentPane().add(vistaEst.getMainPanel());
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir Estadísticas: " + ex.getMessage());
        }
    }

    private void abrirCalendarizacion() {
        try {
            ModelCalendarizacion model = new ModelCalendarizacion();
            Calendarizacion vistaCal = new Calendarizacion();
            CalendarizacionController controller = new CalendarizacionController(model, vistaCal);

            JDialog ventanaContainer = new JDialog(view, "Calendarización de Recursos", true);
            ventanaContainer.getContentPane().add(vistaCal.getMainPanel());
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir Calendarización de Recursos: " + ex.getMessage());
        }
    }

    private void abrirActividades() {
        try {
            ModelActividades model = new ModelActividades();
            Actividades vistaAct = new Actividades();
            ActividadesController controller = new ActividadesController(model, vistaAct);

            JDialog ventanaContainer = new JDialog(view, "Programación de Actividades", true);
            ventanaContainer.getContentPane().add(vistaAct.getMainPanel());
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir Programación de Actividades: " + ex.getMessage());
        }
    }

    private void abrirRecursos() {
        if (!SessionManager.esAdmin()) {
            view.mostrarMensaje("Acceso denegado: No tiene permisos de administrador.");
            return;
        }

        try {
            RecursoService recursoService = RecursoService.getInstance();
            CategoriaService categoriaService = CategoriaService.getInstance();
            ModelRecurso modelRecurso = new ModelRecurso();
            Recursos vistaRecursos = new Recursos();

            ControllerRecursos controllerRecursos = new ControllerRecursos(
                    modelRecurso,
                    vistaRecursos,
                    recursoService,
                    categoriaService
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

    private void abrirCategorias() {
        if (!SessionManager.esAdmin()) {
            view.mostrarMensaje("Acceso denegado: No tiene permisos de administrador.");
            return;
        }

        try {
            CategoriaService categoriaService = CategoriaService.getInstance();
            ModelCategoria modelCategoria = new ModelCategoria();
            Categorias vistaCategorias = new Categorias();

            ControllerCategorias controllerCategorias = new ControllerCategorias(
                    modelCategoria,
                    vistaCategorias,
                    categoriaService
            );

            vistaCategorias.setControllerCategorias(controllerCategorias);

            JDialog ventanaContainer = new JDialog(view, "Gestión de Categorías", true);
            ventanaContainer.getContentPane().add(vistaCategorias);
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir el módulo de Categorías: " + ex.getMessage());
        }
    }

    private void abrirFuncionarios() {
        if (!SessionManager.esAdmin()) {
            view.mostrarMensaje("Acceso denegado: No tiene permisos de administrador.");
            return;
        }

        try {
            FuncionarioService funcionarioService = FuncionarioService.getInstance();
            ModelFuncionario modelFuncionario = new ModelFuncionario();
            Funcionarios vistaFuncionarios = new Funcionarios();

            ControllerFuncionarios controllerFuncionarios = new ControllerFuncionarios(
                    modelFuncionario,
                    vistaFuncionarios,
                    funcionarioService
            );

            vistaFuncionarios.setControllerFuncionarios(controllerFuncionarios);

            JDialog ventanaContainer = new JDialog(view, "Gestión de Funcionarios", true);
            ventanaContainer.getContentPane().add(vistaFuncionarios);
            ventanaContainer.pack();
            ventanaContainer.setLocationRelativeTo(view);
            ventanaContainer.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir el módulo de Funcionarios: " + ex.getMessage());
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