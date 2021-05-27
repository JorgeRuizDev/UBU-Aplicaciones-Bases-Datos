package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;


/**
 * The persistent class for the INCIDENCIA database table.
 */
@Entity
@NamedQuery(name = "Incidencia.findAll", query = "SELECT i FROM Incidencia i")
public class Incidencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IncidenciaPK id;

	@Column(name="ANOTACION")
	@Lob
	private String anotacion;

	//bi-directional many-to-one association to Conductor
	//insertable,udpatable = false porque ya la tenemos definiada en IncidenciaPK
	@ManyToOne
	@JoinColumn(name = "NIF", insertable=false, updatable=false)
	private Conductor conductor;

	//bi-directional many-to-one association to Tipoincidencia
	@ManyToOne
	@JoinColumn(name = "IDTIPO")
	private TipoIncidencia tipoIncidencia;

	public Incidencia() {
	}


	public Incidencia(Conductor conductor, Date fecha, TipoIncidencia tipo) {
		this.id = new IncidenciaPK(conductor.getNif(), fecha);
		this.conductor = conductor;
		this.tipoIncidencia = tipo;
		this.anotacion= "";
	}

	public Incidencia(Conductor conductor, TipoIncidencia tipoIncidencia) {
		this.conductor = conductor;
		this.tipoIncidencia = tipoIncidencia;
	}

	public IncidenciaPK getId() {
		return this.id;
	}

	public void setId(IncidenciaPK id) {
		this.id = id;
	}

	public String getAnotacion() {
		return this.anotacion;
	}

	public void setAnotacion(String anotacion) {
		this.anotacion = anotacion;
	}

	public Conductor getConductor() {
		return this.conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	public TipoIncidencia getTipoIncidencia() {
		return this.tipoIncidencia;
	}

	public void setTipoIncidencia(TipoIncidencia tipoIncidencia) {
		this.tipoIncidencia = tipoIncidencia;
	}

	@Override
	public String toString() {
		return "Incidencia{" +
				"id=" + id +
				", anotacion='" + anotacion + '\'' +
				", conductor=" + "_" +
				", tipoIncidencia=" + tipoIncidencia +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Incidencia)) return false;
		Incidencia that = (Incidencia) o;
		return Objects.equals(id, that.id) && Objects.equals(anotacion, that.anotacion) && Objects.equals(conductor, that.conductor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, anotacion);
	}
}