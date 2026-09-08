package cr.ac.una.resourcemanager.integration;

import cr.ac.una.resourcemanager.data.ReservaXmlDao;
import cr.ac.una.resourcemanager.logic.Reserva;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class ReservaIntegrationIT {

    @Test
    public void testReservaWorkflowIntegration() {
        try {
            ReservaXmlDao reservaDao = new ReservaXmlDao();

            Reserva reserva = new Reserva();
            reserva.setId("IT-RES-01");
            reserva.setActividad("Prueba de Integración Failsafe");
            reservaDao.create(reserva);

            Optional<Reserva> encontrada = reservaDao.read("IT-RES-01");
            assertTrue(encontrada.isPresent(), "La reserva debió ser persistida correctamente en la integración.");
            assertEquals("Prueba de Integración Failsafe", encontrada.get().getActividad(), "La actividad debe coincidir.");

            reservaDao.delete("IT-RES-01");

            Optional<Reserva> eliminada = reservaDao.read("IT-RES-01");
            assertFalse(eliminada.isPresent(), "El registro de integración debe ser eliminado.");

        } catch (Exception e) {
            fail("La prueba de integración falló al lanzar una excepción: " + e.getMessage());
        }
    }
}
