package es.ubu.lsi.dao.invoice;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.invoice.Lineasfactura;

import javax.persistence.EntityManager;

public class LineasFacturaDAO extends JpaDAO<Lineasfactura, LineasFacturaID> {
    public LineasfacturaDAO(EntityManager e){
        super(e);
    }



    // Getter y Setters
}
