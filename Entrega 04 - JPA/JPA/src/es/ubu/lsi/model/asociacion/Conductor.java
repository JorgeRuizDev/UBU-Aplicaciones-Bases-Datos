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
	@Column(name = "NIF", length = 10)
	private String nif;

	@Embedded
	private DireccionPostal direccionPostal;

	@Column(name = "apellido", length = 50, insertable = false, updatable = false)
	private String apellido;

	@Column(name = "apellido", length = 50)
	private String nombre;

	@Column(name = "puntos", precision = 3, scale = 0, columnDefinition = "12")
	private BigDecimal puntos;

	//bi-directional many-to-many association to Asociacion
	@ManyToMany(mappedBy="conductores")
	private Set<Asociacion> asociaciones;

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

	public void setDireccionPostal(DireccionPostal dp) {
		this.direccionPostal = dp;
	}

	public DireccionPostal getDireccionPostal() {
		return this.direccionPostal;
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

	public Set<Asociacion> getAsociaciones() {
		return this.asociaciones;
	}

	public void setAsociaciones(Set<Asociacion> asociaciones) {
		this.asociaciones = asociaciones;
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