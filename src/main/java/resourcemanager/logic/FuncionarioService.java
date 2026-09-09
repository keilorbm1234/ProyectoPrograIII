package resourcemanager.logic;

import resourcemanager.data.FuncionarioXmlDAO;

import java.util.List;
import java.util.Optional;

public class FuncionarioService {
    private static FuncionarioService instance;
    private final FuncionarioXmlDAO funcionarioDao;

    private FuncionarioService(FuncionarioXmlDAO funcionarioDao) {
        this.funcionarioDao = funcionarioDao;
    }

    public static synchronized FuncionarioService getInstance() {
        if (instance == null) {
            instance = new FuncionarioService(new FuncionarioXmlDAO());
        }
        return instance;
    }

    public List<Funcionario> obtenerTodos() throws Exception {
        return funcionarioDao.readAll();
    }

    public List<Funcionario> buscarPorIdONombre(String texto) {
        return funcionarioDao.buscarPorIdONombre(texto);
    }

    /**
     * Crea o actualiza un funcionario. Si es nuevo, su clave por defecto
     * es igual a su ID (regla de negocio del enunciado).
     * @return el id del funcionario creado o actualizado.
     */
    public String guardar(String id, String nombre, String telefono) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Debe ingresar el ID del funcionario.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidationException("Debe ingresar el nombre del funcionario.");
        }

        String idLimpio = id.trim();
        String telefonoLimpio = telefono != null ? telefono.trim() : "";

        Optional<Funcionario> existente = funcionarioDao.read(idLimpio);
        if (existente.isPresent()) {
            Funcionario actualizado = existente.get();
            actualizado.setNombre(nombre.trim());
            actualizado.setTelefono(telefonoLimpio);
            funcionarioDao.update(actualizado);
        } else {
            Funcionario nuevo = new Funcionario(idLimpio, idLimpio, nombre.trim(), telefonoLimpio);
            funcionarioDao.create(nuevo);
        }
        return idLimpio;
    }

    public void borrar(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Debe seleccionar un funcionario de la lista para borrar.");
        }
        funcionarioDao.delete(id);
    }
}
