package resourcemanager.presentation;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Carga un .form del GUI Designer de IntelliJ en tiempo de ejecución y asigna
 * los campos "binding" por reflexión.
 *
 * Esto evita que las pantallas queden en blanco cuando IntelliJ recompila con
 * javac (sin javac2) y pisa las clases instrumentadas por Maven.
 */
public final class FormUiLoader {
    private FormUiLoader() {}

    public static void load(Object view) {
        if (view == null) {
            throw new IllegalArgumentException("La vista no puede ser nula.");
        }
        try (InputStream in = abrirFormulario(view.getClass())) {
            Element root = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(in)
                    .getDocumentElement();
            Element gridRaiz = primerHijo(root, "grid");
            if (gridRaiz == null) {
                throw new IllegalStateException("El .form no tiene un grid raíz.");
            }
            construir(gridRaiz, view);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "No se pudo cargar la interfaz de " + view.getClass().getSimpleName() + ": " + ex.getMessage(),
                    ex);
        }
    }

    private static InputStream abrirFormulario(Class<?> tipo) throws Exception {
        String relativo = tipo.getName().replace('.', '/') + ".form";
        InputStream in = tipo.getClassLoader().getResourceAsStream(relativo);
        if (in != null) {
            return in;
        }
        Path[] candidatos = new Path[] {
                Paths.get("src/main/java", relativo),
                Paths.get(System.getProperty("user.dir", "."), "src/main/java", relativo)
        };
        for (Path path : candidatos) {
            if (Files.isRegularFile(path)) {
                return Files.newInputStream(path);
            }
        }
        throw new IllegalStateException("No se encontró el archivo de formulario: " + relativo);
    }

    private static Component construir(Element elemento, Object view) throws Exception {
        String tag = elemento.getTagName();
        Component componente;
        switch (tag) {
            case "grid" -> componente = crearGrid(elemento, view);
            case "component" -> componente = crearComponente(elemento, view);
            case "scrollpane" -> componente = crearScroll(elemento, view);
            case "hspacer", "vspacer" -> componente = new Spacer();
            default -> throw new IllegalStateException("Elemento de formulario no soportado: " + tag);
        }
        asignarBinding(elemento, view, componente);
        return componente;
    }

    private static JPanel crearGrid(Element grid, Object view) throws Exception {
        int filas = entero(grid, "row-count", 1);
        int columnas = entero(grid, "column-count", 1);
        boolean mismoAncho = "true".equalsIgnoreCase(grid.getAttribute("same-size-horizontally"));
        boolean mismoAlto = "true".equalsIgnoreCase(grid.getAttribute("same-size-vertically"));
        int hgap = entero(grid, "hgap", -1);
        int vgap = entero(grid, "vgap", -1);
        Insets margen = leerMargen(grid);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayoutManager(filas, columnas, margen, hgap, vgap, mismoAncho, mismoAlto));

        Element hijos = primerHijo(grid, "children");
        if (hijos != null) {
            NodeList nodos = hijos.getChildNodes();
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (!(nodo instanceof Element hijo)) {
                    continue;
                }
                Component hijoComp = construir(hijo, view);
                Element constraints = primerHijo(hijo, "constraints");
                panel.add(hijoComp, leerConstraints(constraints));
            }
        }
        return panel;
    }

    private static Component crearComponente(Element componente, Object view) throws Exception {
        String clase = componente.getAttribute("class");
        Object instancia = Class.forName(clase).getDeclaredConstructor().newInstance();
        if (!(instancia instanceof Component swing)) {
            throw new IllegalStateException("La clase del formulario no es un Component: " + clase);
        }
        aplicarPropiedades(componente, swing);
        return swing;
    }

    private static JScrollPane crearScroll(Element scroll, Object view) throws Exception {
        JScrollPane pane = new JScrollPane();
        Element hijos = primerHijo(scroll, "children");
        if (hijos != null) {
            Element componente = primerHijo(hijos, "component");
            if (componente != null) {
                Component hijo = construir(componente, view);
                pane.setViewportView(hijo);
            }
        }
        return pane;
    }

    private static void aplicarPropiedades(Element componente, Component swing) {
        Element properties = primerHijo(componente, "properties");
        if (properties == null) {
            return;
        }
        NodeList nodos = properties.getChildNodes();
        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            if (!(nodo instanceof Element prop)) {
                continue;
            }
            String nombre = prop.getTagName();
            String valor = prop.getAttribute("value");
            if ("text".equals(nombre) && swing instanceof JComponent) {
                try {
                    swing.getClass().getMethod("setText", String.class).invoke(swing, valor);
                } catch (Exception ignored) {
                    // Algunos componentes no tienen setText.
                }
            }
        }
    }

    private static void asignarBinding(Element elemento, Object view, Component componente) throws Exception {
        if (!elemento.hasAttribute("binding")) {
            return;
        }
        String nombre = elemento.getAttribute("binding");
        Field campo = buscarCampo(view.getClass(), nombre);
        campo.setAccessible(true);
        campo.set(view, componente);
    }

    private static Field buscarCampo(Class<?> tipo, String nombre) throws NoSuchFieldException {
        Class<?> actual = tipo;
        while (actual != null) {
            try {
                return actual.getDeclaredField(nombre);
            } catch (NoSuchFieldException ex) {
                actual = actual.getSuperclass();
            }
        }
        throw new NoSuchFieldException(nombre);
    }

    private static GridConstraints leerConstraints(Element constraints) {
        Element grid = constraints == null ? null : primerHijo(constraints, "grid");
        if (grid == null) {
            return new GridConstraints();
        }
        int row = entero(grid, "row", 0);
        int col = entero(grid, "column", 0);
        int rowSpan = entero(grid, "row-span", 1);
        int colSpan = entero(grid, "col-span", 1);
        int vPolicy = entero(grid, "vsize-policy", 3);
        int hPolicy = entero(grid, "hsize-policy", 3);
        int anchor = entero(grid, "anchor", 0);
        int fill = entero(grid, "fill", 3);
        Dimension preferred = leerPreferredSize(grid);
        return new GridConstraints(
                row, col, rowSpan, colSpan, anchor, fill, hPolicy, vPolicy,
                null, preferred, null);
    }

    private static Dimension leerPreferredSize(Element grid) {
        Element preferred = primerHijo(grid, "preferred-size");
        if (preferred == null) {
            return null;
        }
        int width = entero(preferred, "width", -1);
        int height = entero(preferred, "height", -1);
        return new Dimension(width, height);
    }

    private static Insets leerMargen(Element grid) {
        Element margin = primerHijo(grid, "margin");
        if (margin == null) {
            return new Insets(0, 0, 0, 0);
        }
        return new Insets(
                entero(margin, "top", 0),
                entero(margin, "left", 0),
                entero(margin, "bottom", 0),
                entero(margin, "right", 0));
    }

    private static Element primerHijo(Element padre, String tag) {
        NodeList nodos = padre.getChildNodes();
        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            if (nodo instanceof Element elemento && tag.equals(elemento.getTagName())) {
                return elemento;
            }
        }
        return null;
    }

    private static int entero(Element elemento, String atributo, int defecto) {
        String valor = elemento.getAttribute(atributo);
        if (valor == null || valor.isBlank()) {
            return defecto;
        }
        return Integer.parseInt(valor);
    }
}
