package resourcemanager.data;

import resourcemanager.logic.Funcionario;
import resourcemanager.logic.ListaFuncionarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FuncionarioXmlDAO implements DAO<Funcionario, String>{
    private static final String rutaArchivo = "data/Funcionarios.xml";

    private List<Funcionario> cargarLista(){
        ListaFuncionarios coleccion = XmlManager.cargar(rutaArchivo, ListaFuncionarios.class);
        if(coleccion != null && coleccion.getFuncionarios() != null){
            return new ArrayList<>(coleccion.getFuncionarios());
        }
        return new ArrayList<>();
    }

    private void guardarEnXml(List<Funcionario> funcionarios){
        try {
            ListaFuncionarios coleccion = new ListaFuncionarios();
            coleccion.setFuncionarios(funcionarios);
            XmlManager.guardar(coleccion, rutaArchivo, ListaFuncionarios.class);
        } catch (Exception e){
          throw new PersistenceException("Error al guardar en el archivo XML de Funcionarios: " + e.getMessage());
        }
    }

    @Override
    public void create(Funcionario entity) throws Exception {
        List<Funcionario> funcionarios = cargarLista();
        boolean existe = funcionarios.stream().anyMatch(f -> f.getId().equals(entity.getId()));
        if(existe) {
            throw new DuplicateEntityException("Ya existe un funcionario con el ID: " + entity.getId());
        }
        funcionarios.add(entity);
        guardarEnXml(funcionarios);
    }

    @Override
    public Optional<Funcionario> read(String id) throws Exception {
        for(Funcionario funcionario : cargarLista()){
            if(funcionario.getId().equals(id)){
                return Optional.of(funcionario);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Funcionario entity) throws Exception {
        List<Funcionario> funcionarios = cargarLista();
        for(int i = 0; i < funcionarios.size(); i++){
            if(funcionarios.get(i).getId().equals(entity.getId())){
                funcionarios.set(i, entity);
                guardarEnXml(funcionarios);
                return;
            }
        }
        throw new PersistenceException("No existe un funcionario con el ID: " + entity.getId());
    }

    @Override
    public void delete(String id) throws Exception {
        List<Funcionario> funcionarios = cargarLista();
        for(int i = 0; i < funcionarios.size(); i++){
            if(funcionarios.get(i).getId().equals(id)){
                funcionarios.remove(i);
                guardarEnXml(funcionarios);
                return;
            }
        }
        throw new PersistenceException("Funcionario no encontrado para eliminar con ID: " + id);
    }

    @Override
    public List<Funcionario> readAll() throws Exception {
        return cargarLista();
    }

    public Optional<Funcionario> encontrarPorContrasena(String id, String clave){
        if(id == null || clave == null) {
            return Optional.empty();
        }
        return cargarLista().stream().filter(f-> id.equals(f.getId())
                && clave.equals(f.getClave())).findFirst();
    }

    //Buscar por ID o nombre
    public List<Funcionario> buscarPorIdONombre(String busqueda){
        List<Funcionario> funcionarios = cargarLista();
        if(busqueda == null || busqueda.trim().isEmpty()){
            return funcionarios;
        }
        String searchLower = busqueda.toLowerCase();
        return funcionarios.stream()
                .filter(f-> f.getId().toLowerCase().contains(searchLower)
                        || f.getNombre().toLowerCase().contains(searchLower))
                .collect(Collectors.toList());
    }
}
