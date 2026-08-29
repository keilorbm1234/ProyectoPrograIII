package cr.ac.una.resourcemanager.model;

import cr.ac.una.resourcemanager.util.LocalDateAdapter;
import cr.ac.una.resourcemanager.util.LocalTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva {
    private String id; // ej: RES-000001
    private String actividad;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaInicio;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaFin;

    private Funcionario funcionario;

    @XmlElementWrapper(name = "recursosAsignados")
    @XmlElement(name = "recurso")
    private List<Recurso> recursosAsignados = new ArrayList<>();

    private String estado; // "ACTIVA", "CANCELADA"

    public Reserva() {}

    public Reserva(String id, String actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Funcionario funcionario, String estado) {
        this.id = id;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.funcionario = funcionario;
        this.estado = estado;
        this.recursosAsignados = new ArrayList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getActividad() { return actividad; }
    public void setActividad(String actividad) { this.actividad = actividad; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public List<Recurso> getRecursosAsignados() { return recursosAsignados; }
    public void setRecursosAsignados(List<Recurso> recursosAsignados) { this.recursosAsignados = recursosAsignados; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
