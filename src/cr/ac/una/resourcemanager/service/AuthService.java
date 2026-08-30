package cr.ac.una.resourcemanager.service;

import cr.ac.una.resourcemanager.dao.DAO;
import cr.ac.una.resourcemanager.dao.FuncionarioXmlDAO;
import cr.ac.una.resourcemanager.model.Funcionario;
import cr.ac.una.resourcemanager.model.Usuario;
import java.util.Optional;

public class AuthService {
    private final FuncionarioXmlDAO funcionarioDAO;

    public AuthService(FuncionarioXmlDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }
    public Usuario login(String id, String clave) throws Exception {
        // Buscar el funcionario en el archivo XML a través del DAO
        Optional<Funcionario> funcionarioOpt = funcionarioDAO.read(id);

        // Validar existencia del usuario
        if (funcionarioOpt.isEmpty()) {
            throw new Exception("Usuario o clave incorrectos");
        }

        Funcionario funcionario = funcionarioOpt.get();

        // Validar coincidencia de la contraseña
        if (!funcionario.getClave().equals(clave)) {
            throw new Exception("Usuario o clave incorrectos");
        }

        //Guardar la sesión global una vez autenticado
        UsuarioSession.setUsuario(funcionario);

        return funcionario;
    }
}
