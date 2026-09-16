package resourcemanager.presentation;

import resourcemanager.logic.CategoriaService;
import resourcemanager.logic.FuncionarioService;
import resourcemanager.logic.RecursoService;
import resourcemanager.logic.Funcionario;
import resourcemanager.logic.ReservaService;
import resourcemanager.presentation.actividades.Actividades;
import resourcemanager.presentation.actividades.ActividadesController;
import resourcemanager.presentation.actividades.ModelActividades;
import resourcemanager.presentation.calendarizacion.Calendarizacion;
import resourcemanager.presentation.calendarizacion.CalendarizacionController;
import resourcemanager.presentation.calendarizacion.ModelCalendarizacion;
import resourcemanager.presentation.cambioclave.ControllerCambioClave;
import resourcemanager.presentation.cambioclave.ModelCambioClave;
import resourcemanager.presentation.cambioclave.ViewCambioClave;
import resourcemanager.presentation.categorias.Categorias;
import resourcemanager.presentation.categorias.ControllerCategorias;
import resourcemanager.presentation.categorias.ModelCategoria;
import resourcemanager.presentation.estadisticas.Estadisticas;
import resourcemanager.presentation.estadisticas.EstadisticasController;
import resourcemanager.presentation.estadisticas.ModelEstadisticas;
import resourcemanager.presentation.funcionarios.ControllerFuncionarios;
import resourcemanager.presentation.funcionarios.Funcionarios;
import resourcemanager.presentation.funcionarios.ModelFuncionario;
import resourcemanager.presentation.login.SessionManager;
import resourcemanager.logic.UsuarioSession;
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
        configurarVista();
        initController();
    }

    public void configurarVista() {
        boolean esAdmin = SessionManager.esAdmin();
        Funcionario usuarioActual = (Funcionario) UsuarioSession.getUsuario();

        view.setTituloUsuario(usuarioActual.getId(), usuarioActual.getRol());

        if (esAdmin) {
            agregarPestanaFuncionarios();
            agregarPestanaCategorias();
            agregarPestanaRecursos();
        } else {
            agregarPestanaReservas();
        }

        agregarPestanaCalendarizacion();
        agregarPestanaActividades();
        agregarPestanaEstadisticas();

        view.setVisible(true);
    }

    private void initController() {
        view.getBtnCambiarClave().addActionListener(e -> abrirParaCambioClave());
        view.getBtnLogout().addActionListener(e -> cerrarSesion());
    }

    private void agregarPestanaEstadisticas() {
        try {
            ModelEstadisticas model = new ModelEstadisticas();
            Estadisticas vistaEst = new Estadisticas();
            new EstadisticasController(model, vistaEst);

            // Añadimos el título, su icono con la ruta, y el panel:
            view.agregarPestana("Estadísticas", new ImageIcon(getClass().getResource("/icons/statistics.png")), vistaEst.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar Estadísticas: " + ex.getMessage());
        }
    }

    private void agregarPestanaCalendarizacion() {
        try {
            ModelCalendarizacion model = new ModelCalendarizacion();
            Calendarizacion vistaCal = new Calendarizacion();
            new CalendarizacionController(model, vistaCal);

            view.agregarPestana("Calendarización", new ImageIcon(getClass().getResource("/icons/calendarizacion.png")), vistaCal.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar Calendarización: " + ex.getMessage());
        }
    }

    private void agregarPestanaActividades() {
        try {
            ModelActividades model = new ModelActividades();
            Actividades vistaAct = new Actividades();
            new ActividadesController(model, vistaAct);

            view.agregarPestana("Actividades", new ImageIcon(getClass().getResource("/icons/actividades.png")), vistaAct.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar Programación de Actividades: " + ex.getMessage());
        }
    }

    private void agregarPestanaRecursos() {
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

            view.agregarPestana("Recursos", new ImageIcon(getClass().getResource("/icons/recursos.png")), vistaRecursos.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar el módulo de Recursos: " + ex.getMessage());
        }
    }

    private void agregarPestanaCategorias() {
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

            view.agregarPestana("Categorías", new ImageIcon(getClass().getResource("/icons/categorias.png")), vistaCategorias.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar el módulo de Categorías: " + ex.getMessage());
        }
    }

    private void agregarPestanaFuncionarios() {
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

            view.agregarPestana("Funcionarios", new ImageIcon(getClass().getResource("/icons/funcionarios.png")), vistaFuncionarios.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar el módulo de Funcionarios: " + ex.getMessage());
        }
    }

    private void agregarPestanaReservas() {
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
            view.setControllerReservas(controllerReservas);
            controllerReservas.cargarCategoriasDisponibles();

            view.agregarPestana("Reservas", new ImageIcon(getClass().getResource("/icons/reservas.png")), vistaReservas.getMainPanel());
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar el módulo de Reservas: " + ex.getMessage());
        }
    }

    private void cerrarSesion() {
        UsuarioSession.logout();
        view.dispose();
        resourcemanager.Application.doLogin();
    }

    private void abrirParaCambioClave() {
        Funcionario currentUsuario = (Funcionario) UsuarioSession.getUsuario();
        if (currentUsuario == null) {
            view.mostrarMensaje("No existe una sesión activa.");
            return;
        }

        try {
            ModelCambioClave modelCambioClave = new ModelCambioClave(currentUsuario);
            ViewCambioClave vistaCambioClave = new ViewCambioClave(view, currentUsuario);

            ControllerCambioClave controllerCambioDeClave = new ControllerCambioClave(
                    modelCambioClave,
                    vistaCambioClave
            );
            vistaCambioClave.setController(controllerCambioDeClave);

            vistaCambioClave.pack();
            vistaCambioClave.setLocationRelativeTo(view);
            vistaCambioClave.setVisible(true);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al abrir módulo para cambiar Clave: " + ex.getMessage());
        }
    }
}