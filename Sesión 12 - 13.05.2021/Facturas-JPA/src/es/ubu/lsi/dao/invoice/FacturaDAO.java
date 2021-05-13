package es.ubu.lsi.dao.invoice;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.invoice.Facturas;

import javax.persistence.EntityManager;

public class FacturaDAO extends JpaDAO<Facturas, /*Identificador de la clase*/ Long> {

    /**
     * Constructor.
     *
     * @param em
     */
    public FacturaDAO(EntityManager em) {
        super(em);
    }

    // métodos del repositorio
}
