package resourcemanager.data;

import resourcemanager.logic.Recurso;
import resourcemanager.logic.ListaRecursos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecursoXmlDao implements DAO<Recurso, String> {
    private static final String rutaArchivo = "data/Recursos.xml";
    private List<Recurso> recursos;

    public RecursoXmlDao(){
        ListaRecursos coleccion = XmlManager.cargar(rutaArchivo, ListaRecursos.class);
        if(coleccion != null && coleccion.getRecursos() != null){
            this.recursos = new ArrayList<>(coleccion.getRecursos());
        } else  {
            this.recursos = new ArrayList<>();
        }
    }

    public void guardarEnXml() throws PersistenceException{
        try {
            ListaRecursos coleccion = new ListaRecursos();
            coleccion.setRecursos(this.recursos);
            XmlManager.guardar(coleccion, rutaArchivo, ListaRecursos.class);
        } catch (Exception e) {
            throw new PersistenceException("Error al guardar en el archivo XML de Recursos: " + e.getMessage());
        }
    }

    @Override
    public void create(Recurso entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new DuplicateEntityException("Ya existe un recurso registrado con el ID: " + entity.getId());
        }
        this.recursos.add(entity);
        guardarEnXml();
    }

    @Override
    public Optional<Recurso> read(String id) throws Exception {
        for(Recurso recurso : this.recursos){
            if(recurso.getId().equals(id)){
                return Optional.of(recurso);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Recurso entity) throws Exception {
        for(int i = 0; i < this.recursos.size(); i++){
            if(this.recursos.get(i).getId().equals(entity.getId())){
                this.recursos.set(i, entity);
                guardarEnXml();
                return;
            }
        }
        throw new PersistenceException("No existe un recurso registrado con el ID: " + entity.getId());
    }

    @Override
    public void delete(String id) throws Exception {
        for(int i = 0; i < this.recursos.size(); i++){
            if(this.recursos.get(i).getId().equals(id)){
                this.recursos.remove(i);
                guardarEnXml();
                return;
            }
        }
        throw new PersistenceException("Recurso no encontrado para eliminar con el ID: " + id);
    }

    @Override
    public List<Recurso> readAll() throws Exception {
        return new ArrayList<>(this.recursos);
    }

    //Filtrar recursos segun la categoria
    public List<Recurso> filtrarPorCategoria(String categoria){
        if(categoria == null || categoria.trim().isEmpty()){
            return  new ArrayList<>(this.recursos); //Devuelve todos los recursos
        }
        return this.recursos.stream()
                .filter(r -> r.getCategoria() != null && r.getCategoria().getId().equals(categoria)).collect(Collectors.toList());
    }
}
