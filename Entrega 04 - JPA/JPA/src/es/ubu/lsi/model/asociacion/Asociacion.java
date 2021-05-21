package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the ASOCIACION database table.
 * 
 */
@Entity
@NamedQuery(name="Asociacion.findAll", query="SELECT a FROM Asociacion a")
public class Asociacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ASOCIACION_IDASOC_GENERATOR", sequenceName="TIPOINCIDENCIA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ASOCIACION_IDASOC_GENERATOR")

	@Column(name="idasoc", length = 3)
	private String idasoc;

	@Column(name="nombre", length = 50)
	private String nombre;

	private DireccionPostal direccionPostal;

	//bi-directional many-to-many association to Conductor
	@ManyToMany
	@JoinTable(
		name="ASOCIACION_CONDUCTOR"
		, joinColumns={
			@JoinColumn(name="IDASOC")
			}
		, inverseJoinColumns={
			@JoinColumn(name="NIF")
			}
		)
	private Set<Conductor> conductors;

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

	public Set<Conductor> getConductors() {
		return this.conductors;
	}

	public void setConductors(Set<Conductor> conductors) {
		this.conductors = conductors;
	}

}