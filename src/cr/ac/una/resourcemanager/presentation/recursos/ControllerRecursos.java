package cr.ac.una.resourcemanager.presentation.recursos;
import cr.ac.una.resourcemanager.data.RecursoXmlDao;
import cr.ac.una.resourcemanager.logic.Recurso;
import java.util.List;
public class ControllerRecursos {
    private final ModelRecurso model;
    private final Recursos view; // Tu vista Swing
    private final RecursoXmlDao recursoDao;

    public ControllerRecursos(ModelRecurso model, Recursos view, RecursoXmlDao recursoDao) {
        this.model = model;
        this.view = view;
        this.recursoDao = recursoDao;

        cargarRecursos();
    }

    public void cargarRecursos() {
        try {
            List<Recurso> lista = recursoDao.readAll();
            model.setRecursos(lista);
        } catch (Exception ex) {
            // view.mostrarError("Error al cargar recursos: " + ex.getMessage());
        }
    }


}
