package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 * The primary key class for the INCIDENCIA database table.
 * 
 */
@Embeddable
public class IncidenciaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date fecha;

	@Column(insertable=false, updatable=false)
	private String nif;

	public IncidenciaPK() {
	}

	public IncidenciaPK(String nif, Date fecha){
		this.nif = nif;
		this.fecha = fecha;
	}

	public java.util.Date getFecha() {
		return this.fecha;
	}
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}
	public String getNif() {
		return this.nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}

	@Override
	public String toString() {
		return "IncidenciaPK{" +
				"fecha=" + fecha +
				", nif='" + nif + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IncidenciaPK)) return false;
		IncidenciaPK that = (IncidenciaPK) o;
		return Objects.equals(fecha, that.fecha) && Objects.equals(nif, that.nif);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fecha, nif);
	}
}