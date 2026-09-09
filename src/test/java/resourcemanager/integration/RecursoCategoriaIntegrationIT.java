package resourcemanager.integration;

import resourcemanager.logic.Categoria;
import resourcemanager.logic.CategoriaService;
import resourcemanager.logic.Recurso;
import resourcemanager.logic.RecursoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RecursoCategoriaIntegrationIT {

    @Test
    public void testFlujoCompletoCategoriaYRecurso() {
        CategoriaService categoriaService = CategoriaService.getInstance();
        RecursoService recursoService = RecursoService.getInstance();
        String idCategoria = null;

        try {
            idCategoria = categoriaService.guardarCategoria(null, "Categoria IT para Recursos");
            assertNotNull(idCategoria);
            final String idCategoriaCreada = idCategoria;

            Categoria categoriaCreada = categoriaService.getAllCategorias().stream()
                    .filter(c -> c.getId().equals(idCategoriaCreada))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("La categoria creada debe existir."));

            recursoService.guardar("IT-REC-01", categoriaCreada, "Recurso creado en prueba de integracion");

            List<Recurso> filtrados = recursoService.filtrarPorCategoria(idCategoria);
            assertTrue(filtrados.stream().anyMatch(r -> r.getId().equals("IT-REC-01")),
                    "El recurso debe encontrarse al filtrar por la categoria recien creada.");

            recursoService.eliminar("IT-REC-01");

            List<Recurso> despuesDeBorrarRecurso = recursoService.filtrarPorCategoria(idCategoria);
            assertFalse(despuesDeBorrarRecurso.stream().anyMatch(r -> r.getId().equals("IT-REC-01")));

        } catch (Exception e) {
            fail("La prueba de integracion Categoria+Recurso fallo: " + e.getMessage());
        } finally {
            if (idCategoria != null) {
                try {
                    categoriaService.borrar(idCategoria);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
