package es.ubu.lsi.dao.invoice;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.invoice.Lineasfactura;
import es.ubu.lsi.model.invoice.LineasfacturaPK;

import javax.persistence.EntityManager;

public class LineaFacturaDAO extends JpaDAO<Lineasfactura, LineasfacturaPK> {

	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public LineaFacturaDAO(EntityManager em) {
		super(em);
	}
}
