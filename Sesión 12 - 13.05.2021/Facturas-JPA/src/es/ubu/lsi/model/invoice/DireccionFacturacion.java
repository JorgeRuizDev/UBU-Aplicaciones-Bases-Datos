package es.ubu.lsi.model.invoice;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
/**
 * Debemos añadirlo al persistance.xml
 *
 * En el Hiberante provider, > Managed Classes, add class.
 */
public class DireccionFacturacion  implements Serializable {
    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "CP")
    private String cp;

    @Column(name = "CIUDAD")
    private String ciudad;


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
    };
}
