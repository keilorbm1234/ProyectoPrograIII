package resourcemanager.presentation.categorias;

import resourcemanager.logic.Categoria;
import resourcemanager.logic.CategoriaService;
import resourcemanager.logic.PdfService;
import resourcemanager.logic.ValidationException;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.JTable;
import java.util.List;

public class ControllerCategorias {
    private final ModelCategoria model;
    private final Categorias view;
    private final CategoriaService categoriaService;

    public ControllerCategorias(ModelCategoria model, Categorias view, CategoriaService categoriaService) {
        this.model = model;
        this.view = view;
        this.categoriaService = categoriaService;

        cargarCategorias();
    }

    public void cargarCategorias() {
        try {
            List<Categoria> lista = categoriaService.getAllCategorias();
            model.setCategorias(lista);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar categorias: " + ex.getMessage());
        }
    }

    public void buscarCategorias(String texto){
        try {
            List<Categoria> lista = categoriaService.buscarPorDescripcion(texto);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al buscar categorias: " + ex.getMessage());
        }
    }

    public void guardarCategorias(String id, String descripcion){
        try {
            boolean esNueva = (id == null || id.trim().isEmpty());
            String idResultante = categoriaService.guardarCategoria(id, descripcion);

            if (esNueva) {
                view.mostrarMensajeExito("Categoria creada con exito (ID: " + idResultante + ")");
            } else {
                view.mostrarMensajeExito("Categoria actualizada con exito.");
            }

            cargarCategorias();
            view.limpiarCampos();
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al guardar categoria: " + ex.getMessage());
        }
    }

    public void borrarCategoria(String id){
        try {
            categoriaService.borrar(id);
            cargarCategorias();
            view.limpiarCampos();
            view.mostrarMensajeExito("Categoria borrada con exito.");
        } catch (ValidationException ex) {
            view.mostrarError(ex.getMessage());
        } catch (Exception ex) {
            view.mostrarError("Error al borrar categoria: " + ex.getMessage());
        }
    }

    public void imprimirCategorias(String destino, JTable tabla){
        try {
            List<String> columnas = TablaExportadora.extraerColumnas(tabla);
            List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
            new PdfService().print(destino, "Listado de Categorias", null, columnas, filas);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }
}
