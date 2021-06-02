package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the ASOCIACION database table.
 * 
 */
@Entity
@NamedEntityGraph(
		name="graph.AsociacionesConductoresIncidenciasTipos",
		attributeNodes = {@NamedAttributeNode(value="conductores", subgraph = "subgraph.conductoresAsociaciones")},

		subgraphs = {
				@NamedSubgraph(
						name="subgraph.conductoresAsociaciones",
						attributeNodes = @NamedAttributeNode(value="incidencias", subgraph = "subgraph.incidencias")
				),
				@NamedSubgraph(
						name="subgraph.incidencias",
						attributeNodes = @NamedAttributeNode(value="tipoIncidencia")
				)
		}
)
@NamedQuery(name="Asociacion.findAll", query="SELECT a FROM Asociacion a")
public class Asociacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDASOC", length = 3)
	private String idasoc;

	@Column(name="nombre", length = 50, nullable = false, unique = true)
	private String nombre;

	@Embedded
	private DireccionPostal direccionPostal;

	//bi-directional many-to-many association to Conductor
	@ManyToMany
	@JoinTable(
		name="ASOCIACION_CONDUCTOR"
		, joinColumns={
			@JoinColumn(name="IDASOC"),
			}
		, inverseJoinColumns={
			@JoinColumn(name="NIF")
			}
		)
	private Set<Conductor> conductores;

	public Asociacion() {
	}

	public String getIdasoc() {
		return this.idasoc;
	}

	public void setIdasoc(String idasoc) {
		this.idasoc = idasoc;
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

	public Set<Conductor> getConductores() {
		return this.conductores;
	}

	public void setConductores(Set<Conductor> conductors) {
		this.conductores = conductors;
	}

	@Override
	public String toString() {
		return "Asociacion{" +
				"idasoc='" + idasoc + '\'' +
				", nombre='" + nombre + '\'' +
				", direccionPostal=" + direccionPostal +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Asociacion)) return false;
		Asociacion that = (Asociacion) o;
		return Objects.equals(idasoc, that.idasoc) && Objects.equals(nombre, that.nombre) && Objects.equals(direccionPostal, that.direccionPostal) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idasoc, nombre, direccionPostal);
	}
}