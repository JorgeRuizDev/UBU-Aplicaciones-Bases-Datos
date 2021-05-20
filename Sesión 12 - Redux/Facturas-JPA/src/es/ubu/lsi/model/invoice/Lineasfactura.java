package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * The persistent class for the LINEASFACTURA database table.
 * 
 */
@Entity
@Table(name="LINEASFACTURA")
//@NamedQuery(name="Lineasfactura.findAll", query="SELECT l FROM Lineasfactura l")
public class Lineasfactura implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride( name="linea", column = @Column(name="LINEA", nullable= false, precision=22, scale=0)),
			@AttributeOverride(name="nro", column = @Column(name="NRO", nullable= false, precision=22, scale=0))
					})
	private LineasfacturaPK id;

	@Column(name="DESCRIPCION", length=10)
	private String descripcion;

	@Column(name="IMPORTE", precision=7)
	private BigDecimal importe;

	@Column(name="UNIDADES", precision=22, scale=0)
	private BigDecimal unidades;

	//bi-directional many-to-one association to Factura
	@ManyToOne
	@JoinColumn(name="NRO", insertable = false,nullable = false,  updatable = false)
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lineasfactura)) return false;
		Lineasfactura that = (Lineasfactura) o;
		return Objects.equals(id, that.id) && Objects.equals(descripcion, that.descripcion) && Objects.equals(importe, that.importe) && Objects.equals(unidades, that.unidades) && Objects.equals(factura, that.factura);
	}

	@Override
	public String toString() {
		return "Lineasfactura{" +
				"id=" + id +
				", descripcion='" + descripcion + '\'' +
				", importe=" + importe +
				", unidades=" + unidades +
				", factura=" + factura +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, descripcion, importe, unidades, factura);
	}
}