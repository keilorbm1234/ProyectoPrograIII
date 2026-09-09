package resourcemanager.logic;

import resourcemanager.data.RecursoXmlDao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecursoService {
    private static RecursoService instance;
    private final RecursoXmlDao recursoDao;

    private RecursoService(RecursoXmlDao recursoDao) {
        this.recursoDao = recursoDao;
    }

    public static synchronized RecursoService getInstance() {
        if (instance == null) {
            instance = new RecursoService(new RecursoXmlDao());
        }
        return instance;
    }

    public List<Recurso> getAllRecursos() throws Exception {
        return recursoDao.readAll();
    }

    public List<Recurso> filtrarPorCategoria(String categoria){
        return recursoDao.filtrarPorCategoria(categoria);
    }

    public List<Recurso> buscar(String categoriaId, String texto) throws Exception {
        List<Recurso> base = (categoriaId == null || categoriaId.trim().isEmpty())
                ? recursoDao.readAll()
                : recursoDao.filtrarPorCategoria(categoriaId);

        if (texto == null || texto.trim().isEmpty()) {
            return base;
        }
        String textoLower = texto.toLowerCase();
        return base.stream()
                .filter(r -> r.getDescripcion() != null && r.getDescripcion().toLowerCase().contains(textoLower))
                .collect(Collectors.toList());
    }

    public String guardar(String id, Categoria categoria, String descripcion) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("Debe ingresar el ID del recurso.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ValidationException("Debe ingresar la descripcion del recurso.");
        }
        if (categoria == null) {
            throw new ValidationException("Debe seleccionar una categoria para el recurso.");
        }

        String idLimpio = id.trim();
        Optional<Recurso> existente = recursoDao.read(idLimpio);
        if (existente.isPresent()) {
            Recurso actualizado = existente.get();
            actualizado.setCategoria(categoria);
            actualizado.setDescripcion(descripcion.trim());
            recursoDao.update(actualizado);
        } else {
            recursoDao.create(new Recurso(idLimpio, categoria, descripcion.trim()));
        }
        return idLimpio;
    }

    public void crear(Recurso recurso) throws Exception {
        recursoDao.create(recurso);
    }

    public void actualizar(Recurso recurso) throws Exception {
        recursoDao.update(recurso);
    }

    public void eliminar(String id) throws Exception {
        recursoDao.delete(id);
    }
}
