package resourcemanager.data;

import resourcemanager.logic.Categoria;
import resourcemanager.logic.ListaCategorias;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        try {
            ListaCategorias coleccion = new ListaCategorias();
            coleccion.setCategorias(this.categorias);
            XmlManager.guardar(coleccion, rutaArchivo, ListaCategorias.class);
        } catch (Exception e) {
            throw new PersistenceException("Error al guardar en el archivo XML: " + e.getMessage());
        }
    }

    @Override
    public void create(Categoria entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new DuplicateEntityException("Ya existe una categoria con el ID: " + entity.getId());
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
        throw new PersistenceException("No existe una categoria con el ID: " + entity.getId());
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
        throw new PersistenceException("Categoria no encontrada para eliminar con el ID: " + id);
    }

    @Override
    public List<Categoria> readAll() throws Exception {
        return new ArrayList<>(this.categorias);
    }

    //Buscar por descripcion
    public List<Categoria> buscarPorDescripcion(String descripcion){
        if(descripcion == null || descripcion.trim().isEmpty()){
            return new ArrayList<>(this.categorias);
        }
        String searchLower = descripcion.toLowerCase();
        return this.categorias.stream()
                .filter(c-> c.getDescripcion().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
    }

    //Autogeneracion de ID
    public String obtenerUltimoId(){
        if(this.categorias.isEmpty()){
            return null;
        }
        return this.categorias.get(this.categorias.size()-1).getId();
    }
}
