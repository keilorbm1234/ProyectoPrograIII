package resourcemanager.logic;

import resourcemanager.data.FuncionarioXmlDAO;

import java.util.Optional;

public class AuthService {
    private static AuthService instance;
    private final FuncionarioXmlDAO funcionarioDAO;

    public AuthService(FuncionarioXmlDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService(new FuncionarioXmlDAO());
        }
        return instance;
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

    public void cambiarClave(String idUsuario, String claveActual, String claveNueva) throws Exception {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ValidationException("No se pudo identificar al usuario.");
        }

        Optional<Funcionario> funcionarioOpt = funcionarioDAO.read(idUsuario);
        if (funcionarioOpt.isEmpty()) {
            throw new ValidationException("No se encontró el usuario indicado.");
        }

        Funcionario funcionario = funcionarioOpt.get();

        if (claveActual == null || !funcionario.getClave().equals(claveActual)) {
            throw new ValidationException("La clave actual es incorrecta.");
        }
        if (claveNueva == null || claveNueva.trim().isEmpty()) {
            throw new ValidationException("Debe ingresar la nueva clave.");
        }
        if (claveNueva.equals(claveActual)) {
            throw new ValidationException("La nueva clave debe ser diferente a la actual.");
        }

        funcionario.setClave(claveNueva);
        funcionarioDAO.update(funcionario);

        Usuario actual = UsuarioSession.getUsuario();
        if (actual != null && actual.getId().equals(idUsuario)) {
            actual.setClave(claveNueva);
        }
    }
}
