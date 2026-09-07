package cr.ac.una.resourcemanager.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "listaCategorias")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListaCategorias {
    @XmlElement(name = "categoria")
    private List<Categoria> categorias;

    public ListaCategorias() {
        this.categorias = new ArrayList<>();
    }

    public ListaCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
