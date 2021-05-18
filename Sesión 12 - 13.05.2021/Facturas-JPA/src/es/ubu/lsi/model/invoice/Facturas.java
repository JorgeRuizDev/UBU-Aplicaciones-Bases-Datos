package es.ubu.lsi.model.invoice;

import javax.persistence.*;

@Entity
@Table(name = "FACTURAS")
public class Facturas {
    @Id
    @Column(name = "NRO")
    private Long nro;

    @Column(name = "CLIENTE")
    private String cliente;

    @Column(name = "FECHA")
    private java.sql.Date fecha;

    @Column(name = "TOTAL")
    private Long total;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "CP")
    private String cp;

    @Column(name = "CIUDAD")
    private String ciudad;

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
