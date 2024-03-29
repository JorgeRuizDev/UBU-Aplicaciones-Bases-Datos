package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
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

	@Column(name = "NOMBRE", length = 50, nullable = false)
	private String nombre;

	@Column(name = "APELLIDO", length = 50, nullable = false)
	private String apellido;

	// scale = 0 no es necesario, JPA tiene ese valor por defecto.
	@Column(name = "PUNTOS", precision = 3, scale = 0, columnDefinition = "12")
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



	@Override
	public String toString() {
		return "Conductor{" +
				"nif='" + nif + '\'' +
				", direccionPostal=" + direccionPostal +
				", apellido='" + apellido + '\'' +
				", nombre='" + nombre + '\'' +
				", puntos=" + puntos +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Conductor)) return false;
		Conductor conductor = (Conductor) o;
		return Objects.equals(nif, conductor.nif) && Objects.equals(direccionPostal, conductor.direccionPostal) && Objects.equals(apellido, conductor.apellido) && Objects.equals(nombre, conductor.nombre) && Objects.equals(puntos, conductor.puntos) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nif, direccionPostal, apellido, nombre, puntos );
	}
}