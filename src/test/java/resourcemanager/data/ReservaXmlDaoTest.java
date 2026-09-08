package resourcemanager.data;

import resourcemanager.logic.Reserva;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class ReservaXmlDaoTest {

    @Test
    public void testCrearYLeerReserva() {
        try {
            ReservaXmlDao dao = new ReservaXmlDao();
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setId("TEST-RES-01");

            dao.create(nuevaReserva);

            Optional<Reserva> reservaGuardada = dao.read("TEST-RES-01");
            assertTrue(reservaGuardada.isPresent(), "La reserva debería existir en el XML tras guardarla.");
            assertEquals("TEST-RES-01", reservaGuardada.get().getId(), "El ID de la reserva debe coincidir.");

            dao.delete("TEST-RES-01");

            Optional<Reserva> reservaEliminada = dao.read("TEST-RES-01");
            assertFalse(reservaEliminada.isPresent(), "La reserva ya no debería existir tras eliminarla.");

        } catch (Exception e) {
            fail("La prueba de Reserva falló al lanzar una excepción: " + e.getMessage());
        }
    }
}
