package cr.ac.una.resourcemanager.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "funcionarios")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListaFuncionarios {
    @XmlElement(name = "funcionario")
    private List<Funcionario> funcionarios = new ArrayList<Funcionario>();

    public ListaFuncionarios() {}

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
}
