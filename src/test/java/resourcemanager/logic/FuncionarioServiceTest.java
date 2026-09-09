package resourcemanager.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class FuncionarioServiceTest {

    @Test
    public void testGuardarValidaCamposObligatorios() {
        FuncionarioService service = FuncionarioService.getInstance();

        assertThrows(ValidationException.class,
                () -> service.guardar("", "Nombre", "88888888"),
                "Debe rechazar un funcionario sin id.");

        assertThrows(ValidationException.class,
                () -> service.guardar("FUNC-TEST-01", "", "88888888"),
                "Debe rechazar un funcionario sin nombre.");
    }

    @Test
    public void testCrearAplicaClavePorDefectoIgualAlId() {
        try {
            FuncionarioService service = FuncionarioService.getInstance();

            String id = service.guardar("FUNC-TEST-01", "Funcionario de Prueba JUnit", "88888888");
            assertEquals("FUNC-TEST-01", id);

            List<Funcionario> todos = service.obtenerTodos();
            Funcionario creado = todos.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
            assertNotNull(creado, "El funcionario creado debe aparecer en el listado.");
            assertEquals(id, creado.getClave(),
                    "La clave por defecto de un funcionario nuevo debe ser igual a su ID.");

            service.guardar(id, "Funcionario de Prueba JUnit (editado)", "99999999");
            List<Funcionario> actualizados = service.obtenerTodos();
            Funcionario editado = actualizados.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
            assertNotNull(editado);
            assertEquals("Funcionario de Prueba JUnit (editado)", editado.getNombre());
            assertEquals(id, editado.getClave(),
                    "Editar el funcionario no debe modificar su clave existente.");

            List<Funcionario> encontrados = service.buscarPorIdONombre("Prueba JUnit");
            assertTrue(encontrados.stream().anyMatch(f -> f.getId().equals(id)),
                    "La busqueda por nombre debe encontrar al funcionario.");

            service.borrar(id);
            List<Funcionario> despuesDeBorrar = service.obtenerTodos();
            assertFalse(despuesDeBorrar.stream().anyMatch(f -> f.getId().equals(id)));
        } catch (Exception e) {
            fail("La prueba de FuncionarioService fallo al lanzar una excepcion inesperada: " + e.getMessage());
        }
    }
}
