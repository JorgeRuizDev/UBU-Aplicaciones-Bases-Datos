package es.ubu.lsi.model.invoice;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "FACTURAS")
//BOrramos el namedQuery
public class Facturas {
    @Id
    @Column(name = "NRO")
    private Long nro;

    @Column(name = "CLIENTE", length = 10)
    private String cliente;

    @Column(name = "FECHA")
    private java.sql.Date fecha;

    @Column(name = "TOTAL", precision = 8, scale = 2)
    private Long total;

    public DireccionFacturacion getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(DireccionFacturacion direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    @Embedded
    private DireccionFacturacion direccionFacturacion;


    private Set<Lineasfactura> lineasFacturasSet() {
        return null;
    }

    public Long getNro() {
        return this.nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getCliente() {
        return this.cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public java.sql.Date getFecha() {
        return this.fecha;
    }

    public void setFecha(java.sql.Date fecha) {
        this.fecha = fecha;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
