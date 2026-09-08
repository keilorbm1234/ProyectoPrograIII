package resourcemanager.data;

import resourcemanager.logic.Recurso;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class RecursoXmlDaoTest {

    @Test
    public void testCrearYLeerRecurso() {
        try {
            RecursoXmlDao dao = new RecursoXmlDao();
            Recurso nuevoRecurso = new Recurso();
            nuevoRecurso.setId("TEST-REC-01");

            dao.create(nuevoRecurso);

            Optional<Recurso> recursoGuardado = dao.read("TEST-REC-01");
            assertTrue(recursoGuardado.isPresent(), "El recurso debería existir en el XML tras guardarlo.");
            assertEquals("TEST-REC-01", recursoGuardado.get().getId(), "El ID del recurso debe coincidir.");

            dao.delete("TEST-REC-01");

            Optional<Recurso> recursoEliminado = dao.read("TEST-REC-01");
            assertFalse(recursoEliminado.isPresent(), "El recurso ya no debería existir tras eliminarlo.");

        } catch (Exception e) {
            fail("La prueba de Recurso falló al lanzar una excepción: " + e.getMessage());
        }
    }
}
