package es.ubu.lsi.model.asociacion;

import java.io.Serializable;
import java.lang.String;
import java.math.BigInteger;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DireccionPostal
 *
 */
@Embeddable
public class DireccionPostal implements Serializable {

	
	private String direccion;
	private String ciudad;
	private BigInteger codigoPostal;
	private static final long serialVersionUID = 1L;

	public DireccionPostal() {
		super();
	}   
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}   
	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}   
	public BigInteger getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(BigInteger codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
   
}
