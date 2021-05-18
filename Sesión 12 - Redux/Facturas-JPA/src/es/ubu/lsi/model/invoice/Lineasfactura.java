package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LINEASFACTURA database table.
 * 
 */
@Entity
@NamedQuery(name="Lineasfactura.findAll", query="SELECT l FROM Lineasfactura l")
public class Lineasfactura implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LineasfacturaPK id;

	private String descripcion;

	private BigDecimal importe;

	private BigDecimal unidades;

	//bi-directional many-to-one association to Factura
	@ManyToOne
	@JoinColumn(name="NRO")
	private Factura factura;

	public Lineasfactura() {
	}

	public LineasfacturaPK getId() {
		return this.id;
	}

	public void setId(LineasfacturaPK id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getImporte() {
		return this.importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getUnidades() {
		return this.unidades;
	}

	public void setUnidades(BigDecimal unidades) {
		this.unidades = unidades;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

}