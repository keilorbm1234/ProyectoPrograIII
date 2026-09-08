package resourcemanager.data;

import resourcemanager.logic.Categoria;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class CategoriaXmlDaoTest {

    @Test
    public void testCrearYLeerCategoria() {
        try {
            CategoriaXmlDao dao = new CategoriaXmlDao();

            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setId("CAT-TEST-01");
            nuevaCategoria.setDescripcion("Categoría de Prueba JUnit");

            dao.create(nuevaCategoria);

            Optional<Categoria> categoriaGuardada = dao.read("CAT-TEST-01");
            assertTrue(categoriaGuardada.isPresent(), "La categoría debería existir en el XML tras guardarla.");
            assertEquals("Categoría de Prueba JUnit", categoriaGuardada.get().getDescripcion(), "La descripción debe coincidir.");

            dao.delete("CAT-TEST-01");

            Optional<Categoria> categoriaEliminada = dao.read("CAT-TEST-01");
            assertFalse(categoriaEliminada.isPresent(), "La categoría ya no debería existir tras eliminarla.");

        } catch (Exception e) {
            fail("La prueba falló al lanzar una excepción inesperada: " + e.getMessage());
        }
    }
}