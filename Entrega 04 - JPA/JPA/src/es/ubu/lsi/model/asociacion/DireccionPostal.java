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
	private static final long serialVersionUID = 1L;

	@Column(name = "direccion", length = 100)
	private String direccion;

	@Column(name="ciudad", length = 20)
	private String ciudad;

	@Column(name = "cp", length = 5)
	private BigInteger cp;

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

	public BigInteger getCP() {
		return this.cp;
	}
	public void setCP(BigInteger cp) {
		this.cp = cp;
	}
}
