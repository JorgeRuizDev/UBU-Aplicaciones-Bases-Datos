package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;


/**
 * The persistent class for the CONDUCTOR database table.
 * 
 */
@Entity
@NamedQuery(name="Conductor.findAll", query="SELECT c FROM Conductor c")
public class Conductor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CONDUCTOR_NIF_GENERATOR", sequenceName="TIPOINCIDENCIA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONDUCTOR_NIF_GENERATOR")
	private String nif;

	private String apellido;

	private String ciudad;

	private String cp;

	private String direccion;

	private String nombre;

	private BigDecimal puntos;

	//bi-directional many-to-many association to Asociacion
	@ManyToMany(mappedBy="conductors")
	private Set<Asociacion> asociacions;

	//bi-directional many-to-one association to Incidencia
	@OneToMany(mappedBy="conductor")
	private Set<Incidencia> incidencias;

	public Conductor() {
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCp() {
		return this.cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPuntos() {
		return this.puntos;
	}

	public void setPuntos(BigDecimal puntos) {
		this.puntos = puntos;
	}

	public Set<Asociacion> getAsociacions() {
		return this.asociacions;
	}

	public void setAsociacions(Set<Asociacion> asociacions) {
		this.asociacions = asociacions;
	}

	public Set<Incidencia> getIncidencias() {
		return this.incidencias;
	}

	public void setIncidencias(Set<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

	public Incidencia addIncidencia(Incidencia incidencia) {
		getIncidencias().add(incidencia);
		incidencia.setConductor(this);

		return incidencia;
	}

	public Incidencia removeIncidencia(Incidencia incidencia) {
		getIncidencias().remove(incidencia);
		incidencia.setConductor(null);

		return incidencia;
	}

}