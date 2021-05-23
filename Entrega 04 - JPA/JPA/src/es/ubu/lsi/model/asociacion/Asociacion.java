package es.ubu.lsi.model.asociacion;

import org.hibernate.annotations.GenericGenerator;

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
	@Column(name="IDASOC", length = 3)
	private String idasoc;

	@Column(name="nombre", length = 50)
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

}