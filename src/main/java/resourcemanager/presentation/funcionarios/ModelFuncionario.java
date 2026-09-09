package resourcemanager.presentation.funcionarios;

import resourcemanager.logic.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class ModelFuncionario {
    private List<Funcionario> funcionarios = new ArrayList<>();

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
}
