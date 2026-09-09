package resourcemanager.presentation.recursos;

import resourcemanager.logic.Categoria;
import resourcemanager.logic.CategoriaService;
import resourcemanager.logic.PdfService;
import resourcemanager.logic.Recurso;
import resourcemanager.logic.RecursoService;
import resourcemanager.logic.ValidationException;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.JTable;
import java.util.List;

public class ControllerRecursos {
    private final ModelRecurso model;
    private final Recursos view;
    private final RecursoService recursoService;
    private final CategoriaService categoriaService;

    public ControllerRecursos(ModelRecurso model, Recursos view, RecursoService recursoService, CategoriaService categoriaService) {
        this.model = model;
        this.view = view;
        this.recursoService = recursoService;
        this.categoriaService = categoriaService;

        cargarCategorias();
        cargarRecursos();
    }

    public void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.getAllCategorias();
            view.cargarCategorias(categorias);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar categorias: " + ex.getMessage());
        }
    }

    public void cargarRecursos() {
        try {
            List<Recurso> lista = recursoService.getAllRecursos();
            model.setRecursos(lista);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar recursos: " + ex.getMessage());
        }
    }

    public void buscarRecursos(String categoriaId, String textoDescripcion) {
        try {
            List<Recurso> lista = recursoService.buscar(categoriaId, textoDescripcion);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al buscar recursos: " + ex.getMessage());
        }
    }

    public void guardarRecurso(String id, Categoria categoria, String descripcion) {
        try {
            String idResultante = recursoService.guardar(id, categoria, descripcion);
            cargarRecursos();
            view.mostrarMensajeExito("Recurso guardado con exito (ID: " + idResultante + ")");
            view.limpiarCampos();
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al guardar el recurso: " + ex.getMessage());
        }
    }

    public void borrarRecurso(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                throw new ValidationException("Debe seleccionar un recurso de la lista para borrar.");
            }
            recursoService.eliminar(id);
            cargarRecursos();
            view.limpiarCampos();
            view.mostrarMensajeExito("Recurso borrado con exito.");
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al borrar el recurso: " + ex.getMessage());
        }
    }

    public void imprimirRecursos(String destino, JTable tabla){
        try{
            List<String> columnas = TablaExportadora.extraerColumnas(tabla);
            List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
            new PdfService().print(destino, "Lista de Recursos", null, columnas, filas);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }
}
