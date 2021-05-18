package es.ubu.lsi.model.invoice;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
	@OneToMany(mappedBy="factura")
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

}