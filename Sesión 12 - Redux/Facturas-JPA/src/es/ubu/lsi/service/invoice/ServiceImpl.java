package es.ubu.lsi.service.invoice;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.invoice.LineaFacturaDAO;
import es.ubu.lsi.model.invoice.Lineasfactura;
import es.ubu.lsi.model.invoice.LineasfacturaPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

/**
 * Transaction service solution.
 *
 * @author <a href="mailto:jmaudes@ubu.es">Jesús Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @author <a href="mailto:mmabad@ubu.es">Mario Martínez</a>
 * @since 1.0
 */
public class ServiceImpl extends PersistenceService implements Service { // complete with extends and implements

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ServiceImpl.class);

	/**
	 * {@inheritDoc}.
	 *
	 * @param line {@inheritDoc}
	 * @param nro  {@inheritDoc}
	 * @throws PersistenceException {@inheritDoc}
	 */
	@Override
	public void borrarLinea(int line, int nro)
			throws PersistenceException {
		EntityManager em = this.createSession();
		try {
			beginTransaction(em);

			// 1º Creamos el DAO
			LineaFacturaDAO lineaDAO = new LineaFacturaDAO(em);

			// 2º Creamos la PK compuesta de LineaFactura:
			LineasfacturaPK id = new LineasfacturaPK(nro, line);

			// 3º Obtenemos la línea ( .findById() se encuentra en el DAO)
			Lineasfactura lineaFacturaBorrar = lineaDAO.findById(id);


			// Para borrar la línea, le tenemos que pasar al Entity Manger la Entidad a borrar
			if (lineaFacturaBorrar != null) {
				System.out.println(lineaFacturaBorrar);
				lineaDAO.remove(lineaFacturaBorrar);
			} else {
				rollbackTransaction(em);
				throw new InvoiceException(InvoiceError.NOT_EXIST_INVOICE_LINE);
			}


			commitTransaction(em);
		}catch(InvoiceException e){
			logger.error("Exception");
			logger.error(e.getLocalizedMessage());
			throw e;

		} catch (Exception ex) {
			logger.error("Exception");
			ex.printStackTrace();
			if (em.getTransaction().isActive()) {
				System.out.println("Commit rollback");
				em.getTransaction().rollback();
			}
			logger.error(ex.getLocalizedMessage());
		} finally {
			this.close(em);
		}
	}
}


