package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import java.lang.String;
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
   
}
