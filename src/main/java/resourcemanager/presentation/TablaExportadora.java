package resourcemanager.presentation;

import javax.swing.JTable;
import java.util.List;
import java.util.ArrayList;

//Se extrae el contenido de la JTable para que se pueda pasar a servicios de la capa logica sin
//que esa capa dependa de swing.

public class TablaExportadora {

    private TablaExportadora() {
    }

    public static List<String> extraerColumnas(JTable tabla) {
        List<String> columnas = new ArrayList<>();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            columnas.add(tabla.getColumnName(i));
        }
        return columnas;
    }

    public static List<List<String>> extraerFilas(JTable tabla) {
        List<List<String>> filas = new ArrayList<>();
        int totalColumnas = tabla.getColumnCount();
        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            List<String> valoresFila = new ArrayList<>();
            for (int col = 0; col < tabla.getColumnCount(); col++) {
                Object valor = tabla.getValueAt(fila, col);
                valoresFila.add(valor != null ? valor.toString() : "");
            }
            filas.add(valoresFila);
        }
        return filas;
    }
}

