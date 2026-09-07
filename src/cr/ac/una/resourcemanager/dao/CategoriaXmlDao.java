package cr.ac.una.resourcemanager.dao;

import cr.ac.una.resourcemanager.model.Categoria;
import cr.ac.una.resourcemanager.model.ListaCategorias;
import cr.ac.una.resourcemanager.util.XmlManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaXmlDao implements DAO<Categoria, String>{
    private static final String rutaArchivo = "data/Categorias.xml";
    private List<Categoria> categorias;

    public CategoriaXmlDao(){
        ListaCategorias coleccion = XmlManager.cargar(rutaArchivo, ListaCategorias.class);
        if(coleccion != null && coleccion.getCategorias() != null){
            this.categorias = new ArrayList<>(coleccion.getCategorias());
        } else  {
            this.categorias = new ArrayList<>();
        }
    }

    public void guardarEnXml(){
        ListaCategorias coleccion = new ListaCategorias();
        coleccion.setCategorias(this.categorias);
        XmlManager.guardar(coleccion, rutaArchivo, ListaCategorias.class);
    }

    @Override
    public void create(Categoria entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new Exception("Ya existe una categoria con ese id" + entity.getId());
        }
        this.categorias.add(entity);
        guardarEnXml();
    }

    @Override
    public Optional<Categoria> read(String id) throws Exception {
        for(Categoria categoria : this.categorias){
            if(categoria.getId().equals(id)){
                return Optional.of(categoria);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Categoria entity) throws Exception {
        for(int i = 0; i < this.categorias.size(); i++){
            if(this.categorias.get(i).getId().equals(entity.getId())){
                this.categorias.set(i, entity);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("No existe una categoria con ese id" + entity.getId());
    }

    @Override
    public void delete(String id) throws Exception {
        for(int i = 0; i < this.categorias.size(); i++){
            if(this.categorias.get(i).getId().equals(id)){
                this.categorias.remove(i);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("Categoria no encontrada para eliminar con el id" + id);
    }

    @Override
    public List<Categoria> readAll() throws Exception {
        return new ArrayList<>(this.categorias);
    }
}
