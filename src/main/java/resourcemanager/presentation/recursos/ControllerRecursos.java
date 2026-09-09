package resourcemanager.presentation.recursos;

import resourcemanager.logic.PdfService;
import resourcemanager.logic.Recurso;
import resourcemanager.logic.RecursoService;
import resourcemanager.presentation.TablaExportadora;

import javax.swing.JTable;
import java.util.List;

public class ControllerRecursos {
    private final ModelRecurso model;
    private final Recursos view;
    private final RecursoService recursoService;

    public ControllerRecursos(ModelRecurso model, Recursos view, RecursoService recursoService) {
        this.model = model;
        this.view = view;
        this.recursoService = recursoService;

        cargarRecursos();
    }

    public void cargarRecursos() {
        try {
            List<Recurso> lista = recursoService.getAllRecursos();
            model.setRecursos(lista);
        } catch (Exception ex) {
            view.mostrarError("Error al cargar recursos: " + ex.getMessage());
        }
    }

    public void imprimirRecursos(String destino, JTable tabla){
        try{
            List<String> columnas = TablaExportadora.extraerColumnas(tabla);
            List<List<String>> filas = TablaExportadora.extraerFilas(tabla);
            new PdfService().print(destino, "Lista de Recursos", null, columnas, filas);
        } catch (Exception ex) {
            view.mostrarError("Error al generar el PDF: " + ex.getMessage());
        }
    }
}
