package es.ubu.lsi.dao.invoice;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.invoice.Factura;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class FacturaDAO extends JpaDAO<Factura, Long> {
	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public FacturaDAO(EntityManager em) {
		super(em);
	}

	/**
	 * Query all invoices;
	 * @return pues la factura xD.
	 */
	public List<Factura> findAll(){
		try{
			TypedQuery<Factura> query = getEntityManager().createNamedQuery("Factura.findAll", Factura.class);
			return query.getResultList();
		}catch(Exception e){
		    throw e;
		}
	}

	/**
	 * Devuelve todas las facturas con sus correspondientes lineas.
	 * @return facturas.
	 *
	 */
	public List<Factura> findAllWithLines(){
		try{
			TypedQuery<Factura> query = getEntityManager().createNamedQuery("Factura.findAllWithLines", Factura.class);
			return query.getResultList();
		}
		catch(Exception e){
			throw e;
		}
	}
}
