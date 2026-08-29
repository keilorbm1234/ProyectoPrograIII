package cr.ac.una.resourcemanager.model;

public class Funcionario extends Usuario {
    private String nombre;
    private String telefono;

    public Funcionario() {
        super();
        this.rol = "Funcionario";
    }

    public Funcionario(String id, String clave, String nombre, String telefono) {
        super(id, clave, "Funcionario");
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return nombre + " (" + id + ")";
    }
}
