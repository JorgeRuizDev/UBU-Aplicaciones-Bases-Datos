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
	private String idasoc;

	private String ciudad;

	private String cp;

	private String direccion;

	private String nombre;

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

	public Set<Conductor> getConductors() {
		return this.conductors;
	}

	public void setConductors(Set<Conductor> conductors) {
		this.conductors = conductors;
	}

}