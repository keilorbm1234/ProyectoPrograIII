package resourcemanager.logic;

import resourcemanager.data.RecursoXmlDao;

import java.util.List;

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
