package es.ubu.lsi.model.invoice;

import javax.persistence.*;

@Entity()//Entity vacio.
@Table(name = "LINEASFACTURA")
public class Lineasfactura {

    /*
    Esto es de una clase generada por Eclipse que no hemos podido generar.

    Especificamos los dos atributos que componen la clave primera, es mejor hacerlo
    directamente sobre la clase, pero se cumplen si lo invocamos desde este lado.
    @EmbeddedId
    @AttributeOverride(({name="LINEA", column = @Column(name="LINEA", nullable=false, precision = 22, scale = 0)},
     @AttributeOverride(name="nro", column = @Column(name="NRO", nullable = flase, precision = 22, scale=0)) // El override se lo hemos añadido a posterior
    private LineaFacturaId id;
    */

    @Id
    @Column(name = "LINEA")
    private Long linea;

    @Id
    @Column(name = "NRO")
    private Long nro;

    @Column(name = "DESCRIPCION", length=10)
    private String descripcion;

    @Column(name = "UNIDADES", precision = 22, scale =0)
    private Long unidades;

    @Column(name = "IMPORTE", precision = 7)
    private Long importe;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="NRO", nullable = false, updatable = false, insertable = false)
    private Facturas factura;
    //Mejor renombrar la clase a Factura, no Facturas




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
