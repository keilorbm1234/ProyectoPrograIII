package cr.ac.una.resourcemanager.dao;

import cr.ac.una.resourcemanager.model.Funcionario;
import cr.ac.una.resourcemanager.model.ListaFuncionarios;
import cr.ac.una.resourcemanager.util.XmlManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioXmlDAO implements DAO<Funcionario, String>{
    private static final String rutaArchivo = "data/Funcionarios.xml";
    private List<Funcionario> funcionarios;

    public FuncionarioXmlDAO(){
        ListaFuncionarios coleccion = XmlManager.cargar(rutaArchivo, ListaFuncionarios.class);
        if(coleccion != null && coleccion.getFuncionarios() != null){
            this.funcionarios = new ArrayList<>(coleccion.getFuncionarios());
        } else  {
            this.funcionarios = new ArrayList<>(); 
        }
    }

    private void guardarEnXml(){
        ListaFuncionarios coleccion = new ListaFuncionarios();
        coleccion.setFuncionarios(this.funcionarios);
        XmlManager.guardar(coleccion, rutaArchivo, ListaFuncionarios.class);
    }

    @Override
    public void create(Funcionario entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new Exception("Ya existe un funcionario con el ID: " + entity.getId());
        }
        this.funcionarios.add(entity);
        guardarEnXml();
    }

    @Override
    public Optional<Funcionario> read(String id) throws Exception {
        for(Funcionario funcionario : this.funcionarios){
            if(funcionario.getId().equals(id)){
                return Optional.of(funcionario);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Funcionario entity) throws Exception {
        for(int i = 0; i < this.funcionarios.size(); i++){
            if(this.funcionarios.get(i).getId().equals(entity.getId())){
                this.funcionarios.set(i, entity);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("No existe un funcionario con el ID: " + entity.getId());
    }

    @Override
    public void delete(String id) throws Exception {
        for(int i = 0; i < this.funcionarios.size(); i++){
            if(this.funcionarios.get(i).getId().equals(id)){
                this.funcionarios.remove(i);
                guardarEnXml();
                return;
            }
        }
        throw new Exception("Funcionario no encontrado para eliminar con ID: " + id);
    }

    @Override
    public List<Funcionario> readAll() throws Exception {
        return new ArrayList<>(this.funcionarios);
    }

    public Optional<Funcionario> encontrarPorContrasena(String id, String clave){
        if(id == null || clave == null) {
            return Optional.empty();
        }
        return this.funcionarios.stream().filter(f-> id.equals(f.getId())
                && clave.equals(f.getClave())).findFirst();
    }
}
