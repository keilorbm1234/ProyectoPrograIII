package cr.ac.una.resourcemanager.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ListaRecursos")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListaRecursos {
    @XmlElement(name = "recurso")
    private List<Recurso> recursos;

    public ListaRecursos() {
        this.recursos = new ArrayList<>();
    }

    public ListaRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }
}
