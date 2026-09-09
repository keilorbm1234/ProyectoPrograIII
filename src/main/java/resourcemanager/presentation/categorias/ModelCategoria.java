package resourcemanager.presentation.categorias;

import resourcemanager.logic.Categoria;
import java.util.ArrayList;
import java.util.List;

public class ModelCategoria {
    private List<Categoria> categorias = new ArrayList<>();

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
