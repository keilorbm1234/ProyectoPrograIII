package resourcemanager.presentation.funcionarios;

import resourcemanager.logic.Funcionario;
import resourcemanager.logic.FuncionarioService;
import resourcemanager.logic.PdfService;
import resourcemanager.logic.ValidationException;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.JTable;
import java.util.List;

public class ControllerFuncionarios {
    private final ModelFuncionario model;
    private final Funcionarios view;
    private final FuncionarioService funcionarioService;

    public ControllerFuncionarios(ModelFuncionario model, Funcionarios view, FuncionarioService funcionarioService) {
        this.model = model;
        this.view = view;
        this.funcionarioService = funcionarioService;

        cargarFuncionarios();
    }

    public void cargarFuncionarios() {
        try {
            List<Funcionario> lista = funcionarioService.obtenerTodos();
            model.setFuncionarios(lista);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar funcionarios: " + ex.getMessage());
        }
    }

    public void buscarFuncionarios(String texto) {
        try {
            List<Funcionario> lista = funcionarioService.buscarPorIdONombre(texto);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al buscar funcionarios: " + ex.getMessage());
        }
    }

    public void guardarFuncionario(String id, String nombre, String telefono) {
        try {
            String idResultante = funcionarioService.guardar(id, nombre, telefono);
            cargarFuncionarios();
            view.mostrarMensajeExito("Funcionario guardado con exito (ID: " + idResultante + ")");
            view.limpiarCampos();
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al guardar el funcionario: " + ex.getMessage());
        }
    }

    public void borrarFuncionario(String id) {
        try {
            funcionarioService.borrar(id);
            cargarFuncionarios();
            view.limpiarCampos();
            view.mostrarMensajeExito("Funcionario borrado con exito.");
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al borrar el funcionario: " + ex.getMessage());
        }
    }

    public void imprimirFuncionarios(String destino, JTable tabla) {
        try {
            List<String> columnas = TablaExportadora.extraerColumnas(tabla);
            List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
            new PdfService().print(destino, "Listado de Funcionarios", null, columnas, filas);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }
}
