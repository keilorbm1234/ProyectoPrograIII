package resourcemanager.data;

import resourcemanager.logic.Funcionario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class FuncionarioXmlDaoTest {

    @Test
    public void testCrearYLeerFuncionario() {
        try {
            FuncionarioXmlDAO dao = new FuncionarioXmlDAO();

            Funcionario nuevoFuncionario = new Funcionario();
            nuevoFuncionario.setId("TEST-FUNC-01");
            nuevoFuncionario.setNombre("Funcionario Prueba JUnit");

            dao.create(nuevoFuncionario);

            Optional<Funcionario> funcionarioGuardado = dao.read("TEST-FUNC-01");
            assertTrue(funcionarioGuardado.isPresent(), "El funcionario debería existir en el XML tras guardarlo.");
            assertEquals("Funcionario Prueba JUnit", funcionarioGuardado.get().getNombre(), "El nombre debe coincidir.");

            dao.delete("TEST-FUNC-01");

            Optional<Funcionario> funcionarioEliminado = dao.read("TEST-FUNC-01");
            assertFalse(funcionarioEliminado.isPresent(), "El funcionario ya no debería existir tras eliminarlo.");

        } catch (Exception e) {
            fail("La prueba de Funcionario falló al lanzar una excepción: " + e.getMessage());
        }
    }
}
