package es.ubu.lsi.model.invoice;

import javax.persistence.*;

@Entity
@Table(name = "LINEASFACTURA")
public class Lineasfactura {
    @Id
    @Column(name = "LINEA")
    private Long linea;

    @Id
    @Column(name = "NRO")
    private Long nro;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "UNIDADES")
    private Long unidades;

    @Column(name = "IMPORTE")
    private Long importe;

    public Long getLinea() {
        return this.linea;
    }

    public void setLinea(Long linea) {
        this.linea = linea;
    }

    public Long getNro() {
        return this.nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUnidades() {
        return this.unidades;
    }

    public void setUnidades(Long unidades) {
        this.unidades = unidades;
    }

    public Long getImporte() {
        return this.importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }
}
