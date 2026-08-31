package cr.ac.una.resourcemanager.dao;

import cr.ac.una.resourcemanager.model.Recurso;
import cr.ac.una.resourcemanager.model.ListaRecursos;
import cr.ac.una.resourcemanager.util.XmlManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void guardarEnXml(){
        ListaRecursos coleccion = new ListaRecursos();
        coleccion.setRecursos(this.recursos);
        XmlManager.guardar(coleccion, rutaArchivo, ListaRecursos.class);
    }

    @Override
    public void create(Recurso entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new Exception("Ya existe un recurso registrado con el ID" + entity.getId());
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
        throw new Exception("No existe un recurso registrado con el ID" + entity.getId());
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
        throw new Exception("Recurso no encontrado para eliminar con el ID" + id);
    }

    @Override
    public List<Recurso> readAll() throws Exception {
        return new ArrayList<>(this.recursos);
    }
}
