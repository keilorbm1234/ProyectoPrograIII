package resourcemanager.presentation.categorias;

import resourcemanager.data.CategoriaXmlDao;
import resourcemanager.logic.Categoria;
import resourcemanager.logic.PdfService;
import resourcemanager.logic.ValidationException;
import javax.swing.JTable;
import java.util.List;

public class ControllerCategorias {
    private final ModelCategoria model;
    private final Categorias view;
    private final CategoriaXmlDao categoriaDao;

    public ControllerCategorias(ModelCategoria model, Categorias view, CategoriaXmlDao categoriaDao) {
        this.model = model;
        this.view = view;
        this.categoriaDao = categoriaDao;

        cargarCategorias();
    }

    public void cargarCategorias() {
        try {
            List<Categoria> lista = categoriaDao.readAll();
            model.setCategorias(lista);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar categorias: " + ex.getMessage());
        }
    }

    public void buscarCategorias(String texto){
        try {
            List<Categoria> lista = categoriaDao.buscarPorDescripcion(texto);
            view.cargarTabla(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al buscar categorias: " + ex.getMessage());
        }
    }

    public void guardarCategorias(String id, String descripcion){
        try {
            if(descripcion == null || descripcion.trim().isEmpty()){
                throw new ValidationException("Debe ingresar la descripcion de la categoria.");
            }
            if(id == null || id.trim().isEmpty()){
                String nuevoId = generarSiguienteId();
                Categoria nueva = new Categoria(nuevoId, descripcion.trim());
                categoriaDao.create(nueva);
                view.mostrarMensajeExito("Categoria creada con exito (ID: " + nuevoId + ")");
            } else {
                Categoria existente = new Categoria(id, descripcion.trim());
                categoriaDao.update(existente);
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
            if(id == null || id.trim().isEmpty()){
                throw new ValidationException("Debe seleccionar una categoria de la lista para borrar.");
            }
            categoriaDao.delete(id);
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
            new PdfService().print(destino, "Listado de Categorias", null, tabla);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }

    public String generarSiguienteId() throws Exception {
        String ultimoId = categoriaDao.obtenerUltimoId();
        if(ultimoId == null || ultimoId.isEmpty()){
            return "CAT-000001";
        }
        String[] partes = ultimoId.split("-");
        int numero = Integer.parseInt(partes[partes.length - 1]);
        return String.format("CAT-%06d", numero + 1);
    }
}
