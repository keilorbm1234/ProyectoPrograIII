package resourcemanager.presentation;

import org.junit.jupiter.api.Test;
import resourcemanager.presentation.categorias.Categorias;
import resourcemanager.presentation.funcionarios.Funcionarios;
import resourcemanager.presentation.recursos.Recursos;

import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormUiLoaderTest {

    @Test
    void recursosCargaElFormularioEnMainPanel() {
        JPanel panel = new Recursos().getMainPanel();
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void funcionariosCargaElFormularioEnMainPanel() {
        JPanel panel = new Funcionarios().getMainPanel();
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void categoriasCargaElFormularioEnMainPanel() {
        JPanel panel = new Categorias().getMainPanel();
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }
}
