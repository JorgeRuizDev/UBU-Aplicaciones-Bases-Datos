package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import java.lang.String;
import java.util.Objects;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DirecciónFacturación
 *
 */
@Embeddable
public class DireccionFacturacion implements Serializable {

	
	private String direccion;
	private String cp;
	private String ciudad;
	private static final long serialVersionUID = 1L;

	public DireccionFacturacion() {
		super();
	}   
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}   
	public String getCp() {
		return this.cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}   
	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DireccionFacturacion)) return false;
		DireccionFacturacion that = (DireccionFacturacion) o;
		return Objects.equals(direccion, that.direccion) && Objects.equals(cp, that.cp) && Objects.equals(ciudad, that.ciudad);
	}

	@Override
	public String toString() {
		return "DireccionFacturacion{" +
				"direccion='" + direccion + '\'' +
				", cp='" + cp + '\'' +
				", ciudad='" + ciudad + '\'' +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(direccion, cp, ciudad);
	}
}
