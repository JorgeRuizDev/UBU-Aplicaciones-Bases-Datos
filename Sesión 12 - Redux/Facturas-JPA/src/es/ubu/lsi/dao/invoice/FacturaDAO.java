package es.ubu.lsi.dao.invoice;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.invoice.Factura;

import javax.persistence.EntityManager;

public class FacturaDAO extends JpaDAO<Factura, Long> {
	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public FacturaDAO(EntityManager em) {
		super(em);
	}
}
