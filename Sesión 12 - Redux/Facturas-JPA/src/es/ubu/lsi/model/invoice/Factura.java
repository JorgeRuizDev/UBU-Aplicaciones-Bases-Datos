package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the FACTURAS database table.
 * 
 */
@Entity
@Table(name="FACTURAS")
//SOBRA: @NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f")
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NRO")
	private long nro;


	@Column(name="CLIENTE", length=10)
	private String cliente;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA")
	private Date fecha;

	@Column(name="TOTAL", precision=8, scale = 2)
	private BigDecimal total;

	@Embedded
	private DireccionFacturacion direccionFacturacion;


	public DireccionFacturacion getDireccionFacturacion() {
		return direccionFacturacion;
	}

	public void setDireccionFacturacion(DireccionFacturacion direccionFacturacion) {
		this.direccionFacturacion = direccionFacturacion;
	}



	//bi-directional many-to-one association to Lineasfactura
	@OneToMany(mappedBy="factura", cascade = CascadeType.ALL)
	private List<Lineasfactura> lineasfacturas;



	public Factura() {
	}

	public long getNro() {
		return this.nro;
	}

	public void setNro(long nro) {
		this.nro = nro;
	}


	public String getCliente() {
		return this.cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<Lineasfactura> getLineasfacturas() {
		return this.lineasfacturas;
	}

	public void setLineasfacturas(List<Lineasfactura> lineasfacturas) {
		this.lineasfacturas = lineasfacturas;
	}

	public Lineasfactura addLineasfactura(Lineasfactura lineasfactura) {
		getLineasfacturas().add(lineasfactura);
		lineasfactura.setFactura(this);

		return lineasfactura;
	}

	public Lineasfactura removeLineasfactura(Lineasfactura lineasfactura) {
		getLineasfacturas().remove(lineasfactura);
		lineasfactura.setFactura(null);

		return lineasfactura;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Factura)) return false;
		Factura factura = (Factura) o;
		return nro == factura.nro && Objects.equals(cliente, factura.cliente) && Objects.equals(fecha, factura.fecha) && Objects.equals(total, factura.total) && Objects.equals(direccionFacturacion, factura.direccionFacturacion) && Objects.equals(lineasfacturas, factura.lineasfacturas);
	}

	@Override
	public String toString() {
		return "Factura{" +
				"nro=" + nro +
				", cliente='" + cliente + '\'' +
				", fecha=" + fecha +
				", total=" + total +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(nro, cliente, fecha, total, direccionFacturacion, lineasfacturas);
	}
}