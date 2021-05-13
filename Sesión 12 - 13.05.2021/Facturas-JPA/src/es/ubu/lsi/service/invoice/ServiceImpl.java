package es.ubu.lsi.service.invoice;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import es.ubu.lsi.dao.invoice.LineasFacturaDAO;
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
 *
 */
public class ServiceImpl { // complete with extends and implements

	/** Logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ServiceImpl.class);

	/**
	 * {@inheritDoc}.
	 * @param line {@inheritDoc}
	 * @param nro {@inheritDoc}
	 * 
	 * @throws PersistenceException {@inheritDoc}
	 */
	@Override
	public void borrarLinea(int line, int nro)
			throws PersistenceException {
		EntityManager em = this.createSession();
		try {
			beginTransaction(em);

			// DAO
			LineasFacturaDAO lineaDAO = new LineasFacturaDAO(em);


			// Paso 1:
			LineaFacturaId  id = new LineaFacturaId(linea, nro);
			/* Añadir un segundo constructor apra que puede admitir ambos parámetros */

			/*
			public LineaFacturaId(int linea, int factura){
				this.linea = linea;
				this.factura = factura;
			}*/
			LineaFactura lineaFacturaBorrar = lineaDAO.findById(id);
			// Ya hemos localizado el elemento, debemos commmprobar si el elemento existe o no existe.


			if (lineaFacturaBorrrar != null){

			}
			else{ throw new InvoiceException(InvoiceError.NOT_EXIST_INVOICE_LINE);}

			commitTransaction(em);
			
		// add catchs...
		} finally {
			// close resources
		}
	}
}
