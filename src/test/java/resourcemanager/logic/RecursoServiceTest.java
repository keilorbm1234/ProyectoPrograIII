package resourcemanager.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RecursoServiceTest {

    @Test
    public void testGuardarValidaCamposObligatorios() {
        RecursoService service = RecursoService.getInstance();

        assertThrows(ValidationException.class,
                () -> service.guardar("", new Categoria("CAT-X", "X"), "Descripcion"),
                "Debe rechazar un recurso sin id.");

        assertThrows(ValidationException.class,
                () -> service.guardar("REC-TEST-01", new Categoria("CAT-X", "X"), ""),
                "Debe rechazar un recurso sin descripcion.");

        assertThrows(ValidationException.class,
                () -> service.guardar("REC-TEST-01", null, "Descripcion"),
                "Debe rechazar un recurso sin categoria.");
    }

    @Test
    public void testCrearActualizarYFiltrarPorCategoria() {
        try {
            RecursoService service = RecursoService.getInstance();
            Categoria categoriaPrueba = new Categoria("CAT-TEST-REC", "Categoria para prueba de Recurso");

            String id = service.guardar("REC-TEST-01", categoriaPrueba, "Recurso de prueba JUnit");
            assertEquals("REC-TEST-01", id);

            List<Recurso> filtrados = service.filtrarPorCategoria("CAT-TEST-REC");
            assertTrue(filtrados.stream().anyMatch(r -> r.getId().equals("REC-TEST-01")),
                    "El recurso debe aparecer al filtrar por su categoria.");

            List<Recurso> encontradosPorTexto = service.buscar("CAT-TEST-REC", "prueba");
            assertTrue(encontradosPorTexto.stream().anyMatch(r -> r.getId().equals("REC-TEST-01")),
                    "La busqueda combinada de categoria + texto debe encontrar el recurso.");

            service.guardar("REC-TEST-01", categoriaPrueba, "Recurso de prueba JUnit (editado)");
            List<Recurso> todos = service.getAllRecursos();
            Recurso editado = todos.stream().filter(r -> r.getId().equals("REC-TEST-01")).findFirst().orElse(null);
            assertNotNull(editado);
            assertEquals("Recurso de prueba JUnit (editado)", editado.getDescripcion());

            service.eliminar("REC-TEST-01");
            List<Recurso> despuesDeBorrar = service.getAllRecursos();
            assertFalse(despuesDeBorrar.stream().anyMatch(r -> r.getId().equals("REC-TEST-01")));
        } catch (Exception e) {
            fail("La prueba de RecursoService fallo al lanzar una excepcion inesperada: " + e.getMessage());
        }
    }
}
