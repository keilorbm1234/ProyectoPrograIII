package resourcemanager.data;

import resourcemanager.logic.Funcionario;
import resourcemanager.logic.ListaFuncionarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        try {
            ListaFuncionarios coleccion = new ListaFuncionarios();
            coleccion.setFuncionarios(this.funcionarios);
            XmlManager.guardar(coleccion, rutaArchivo, ListaFuncionarios.class);
        } catch (Exception e){
          throw new PersistenceException("Error al guardar en el archivo XML de Funcionarios: " + e.getMessage());
        }
    }

    @Override
    public void create(Funcionario entity) throws Exception {
        if(read(entity.getId()).isPresent()) {
            throw new DuplicateEntityException("Ya existe un funcionario con el ID: " + entity.getId());
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
        throw new PersistenceException("No existe un funcionario con el ID: " + entity.getId());
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
        throw new PersistenceException("Funcionario no encontrado para eliminar con ID: " + id);
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

    //Buscar por ID o nombre
    public List<Funcionario> buscarPorIdONombre(String busqueda){
        if(busqueda == null || busqueda.trim().isEmpty()){
            return new ArrayList<>(this.funcionarios);
        }
        String searchLower = busqueda.toLowerCase();
        return this.funcionarios.stream()
                .filter(f-> f.getId().toLowerCase().contains(searchLower)
                        || f.getNombre().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
    }
}
