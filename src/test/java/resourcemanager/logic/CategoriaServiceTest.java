package resourcemanager.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CategoriaServiceTest {

    @Test
    public void testGuardarValidaDescripcionVacia() {
        CategoriaService service = CategoriaService.getInstance();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.guardarCategoria(null, ""),
                "Debe rechazar una categoria sin descripcion.");
        assertTrue(ex.getMessage().toLowerCase().contains("descripcion"));
    }

    @Test
    public void testCrearAutogeneraIdYPermiteActualizar() {
        try {
            CategoriaService service = CategoriaService.getInstance();

            String idGenerado = service.guardarCategoria(null, "Categoria de Prueba JUnit");
            assertNotNull(idGenerado, "El id autogenerado no debe ser nulo.");
            assertTrue(idGenerado.startsWith("CAT-"), "El id autogenerado debe usar el prefijo CAT-.");

            List<Categoria> todas = service.getAllCategorias();
            assertTrue(todas.stream().anyMatch(c -> c.getId().equals(idGenerado)),
                    "La categoria creada debe aparecer en el listado.");

            String idActualizado = service.guardarCategoria(idGenerado, "Categoria de Prueba JUnit (editada)");
            assertEquals(idGenerado, idActualizado, "Al editar, el id debe mantenerse igual.");

            List<Categoria> encontradas = service.buscarPorDescripcion("editada");
            assertTrue(encontradas.stream().anyMatch(c -> c.getId().equals(idGenerado)),
                    "La busqueda por descripcion debe encontrar la categoria editada.");

            service.borrar(idGenerado);

            List<Categoria> despuesDeBorrar = service.getAllCategorias();
            assertFalse(despuesDeBorrar.stream().anyMatch(c -> c.getId().equals(idGenerado)),
                    "La categoria borrada no debe seguir apareciendo en el listado.");
        } catch (Exception e) {
            fail("La prueba de CategoriaService fallo al lanzar una excepcion inesperada: " + e.getMessage());
        }
    }

    @Test
    public void testBorrarSinIdLanzaValidationException() {
        CategoriaService service = CategoriaService.getInstance();
        assertThrows(ValidationException.class, () -> service.borrar(""),
                "Debe rechazar el borrado si no se selecciona un id.");
    }
}
