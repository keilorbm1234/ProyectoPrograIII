package resourcemanager.logic;

import resourcemanager.data.CategoriaXmlDao;

import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    private static CategoriaService instance;
    private final CategoriaXmlDao categoriaDao;

    private CategoriaService(CategoriaXmlDao categoriaDao) {
        this.categoriaDao = categoriaDao;
    }

    public static synchronized CategoriaService getInstance() {
        if (instance == null) {
            instance = new CategoriaService(new CategoriaXmlDao());
        }
        return instance;
    }

    public List<Categoria> getAllCategorias() throws Exception {
        return categoriaDao.readAll();
    }

    public List<String> getNombreCategorias() {
        ArrayList<String> listaCategorias = new ArrayList<>();
        try {

            for (Categoria categoria : getAllCategorias()) {
                listaCategorias.add(categoria.getDescripcion());
            }
        }catch(Exception e) {
            listaCategorias.add("Error: " + e.getMessage());
        }
        return listaCategorias;
    }

    public List<Categoria> buscarPorDescripcion(String descripcion) throws Exception {
        return categoriaDao.buscarPorDescripcion(descripcion);
    }

    public String guardarCategoria(String id, String descripcion) throws Exception {
        if(descripcion == null || descripcion.trim().isEmpty()){
            throw new ValidationException("Debe ingresar la descripcion de la categoria.");
        }
        if(id == null || id.trim().isEmpty()){
            String nuevoId = generarSiguienteId();
            Categoria nueva = new Categoria(nuevoId, descripcion.trim());
            categoriaDao.create(nueva);
            return nuevoId;
        } else {
            Categoria existente = new Categoria(id, descripcion.trim());
            categoriaDao.update(existente);
            return id;
        }
    }

    public void borrar(String id) throws Exception {
        if(id == null || id.trim().isEmpty()){
            throw new ValidationException("Debe seleccionar una categoria de la lista para borrar.");
        }
        categoriaDao.delete(id);
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
