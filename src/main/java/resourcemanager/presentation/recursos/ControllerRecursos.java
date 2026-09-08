package resourcemanager.presentation.recursos;
import resourcemanager.data.RecursoXmlDao;
import resourcemanager.logic.PdfService;
import resourcemanager.logic.Recurso;
import javax.swing.JTable;
import java.util.List;

public class ControllerRecursos {
    private final ModelRecurso model;
    private final Recursos view; //vista Swing
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
            view.mostrarError("Error al cargar recursos: " + ex.getMessage());
        }
    }

    public void imprimirRecursos(String destino, JTable tabla){
        try{
            new PdfService().print(destino, "Lista de Recursos", null, tabla);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }


}
