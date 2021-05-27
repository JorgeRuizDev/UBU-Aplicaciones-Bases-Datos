package es.ubu.lsi.service.asociacion;

import es.ubu.lsi.dao.asociacion.AsociacionDAO;
import es.ubu.lsi.dao.asociacion.ConductorDAO;
import es.ubu.lsi.dao.asociacion.IncidenciaDAO;
import es.ubu.lsi.dao.asociacion.TipoIncidenciaDAO;
import es.ubu.lsi.model.asociacion.*;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceImpl extends PersistenceService implements Service {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceImpl.class);

	/**
	 * Función que permite loguear una excepción.
	 * @param e Excepción a Loguear.
	 */
	private static void logException(Exception e) {
		logger.error(e.getLocalizedMessage());
		System.err.println("Habemus Excepción");
		//System.out.println(e.getLocalizedMessage());
		for (StackTraceElement trace : e.getStackTrace()) {
			logger.info(e.toString());
			//System.out.println(e.toString());
		}
	}

	/**
	 * @{inheritdoc}
	 * @param fecha @{inheritdoc}
	 * @param nif   @{inheritdoc}
	 * @param tipo  @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException {
		EntityManager em = this.createSession();
		try {
			beginTransaction(em);
			IncidenciaDAO incidenciaDAO = new IncidenciaDAO(em);
			TipoIncidenciaDAO tipoInciDAO = new TipoIncidenciaDAO(em);
			ConductorDAO conductorDAO = new ConductorDAO(em);

			Conductor conductor = conductorDAO.findById(nif);

			if (conductor == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
			}

			TipoIncidencia tipoIncidencia = tipoInciDAO.findById(tipo);

			if (tipoIncidencia == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_INCIDENT_TYPE);
			}

			// restamos puntos
			conductor.setPuntos(new BigDecimal(Math.max(conductor.getPuntos().subtract(tipoIncidencia.getValor()).intValue(), 0)));

			conductorDAO.persist(conductor);

			Incidencia nuevaIncidencia = new Incidencia(conductor, fecha, tipoIncidencia);

			incidenciaDAO.persist(nuevaIncidencia);


			commitTransaction(em);

		} catch (IncidentException e) {
			logException(e);
			rollbackTransaction(em);
			throw e;
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
		} finally {
			close(em);
		}
	}

	/**
	 * @{inheritdoc}
	 * @param nif @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public void indultar(String nif) throws PersistenceException {
		EntityManager em = createSession();
		logger.info("\n\nInicio transacción indultar");
		try {
			beginTransaction(em);

			ConductorDAO conductorDAO = new ConductorDAO(em);
			IncidenciaDAO incidenciaDAO = new IncidenciaDAO(em);

			Conductor conductor = conductorDAO.findById(nif);

			if (conductor == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
			}

			conductor.setPuntos(new BigDecimal(12));

			Set<Incidencia> incidencias = conductor.getIncidencias();
			for (Incidencia incidencia : incidencias) {
				incidenciaDAO.remove(incidencia);
			}

			commitTransaction(em);
		} catch (IncidentException e) {
			rollbackTransaction(em);
			logException(e);
			throw e;

		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
		} finally {
			close(em);
		}
	}

	/**
	 * @{inheritdoc}
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public List<Asociacion> consultarAsociaciones() throws PersistenceException {

		EntityManager em = createSession();
		logger.info("\n\nInicio transacción consultarAsociaciones");
		try {
			beginTransaction(em);




			commitTransaction(em);

			throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
		} catch (PersistenceException e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
		} finally {
			close(em);
		}
		return null;
	}


	/**
	 * @{inheritdoc}
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public List<TipoIncidenciaRanking> consultarRanking() throws PersistenceException {
		EntityManager em = createSession();
		logger.info("\n\nInicio transacción consultarRanking");

		try {
			beginTransaction(em);

			IncidenciaDAO incidenciaDAO = new IncidenciaDAO(em);
			List<TipoIncidenciaRanking> ranking = incidenciaDAO.consultarTipoIncidenciaRanking();

			commitTransaction(em);
			return ranking;
		} catch (PersistenceException e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
		} finally {
			close(em);
		}
		return null;
	}

	/**
	 * @{inheritdoc}
	 * @param descripcion @{inheritdoc}
	 * @param valor       @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public void insertarTipoIncidencia(String descripcion, int valor) throws PersistenceException {

		EntityManager em = createSession();
		logger.info("\n\nInicio transacción insertarTipoIncidencia");

		try {
			beginTransaction(em);
			TipoIncidenciaDAO incidenciaDAO = new TipoIncidenciaDAO(em);

			TipoIncidencia nuevoTipoIncidencia = new TipoIncidencia(descripcion, new BigDecimal(valor));

			incidenciaDAO.persist(nuevoTipoIncidencia);

			commitTransaction(em);
		} catch (Exception e) {
			logException(e);
			throw e;
		} finally {
			close(em);
		}


	}

	/**
	 * @{inheritdoc}
	 * @param idasoc @{inheritdoc}
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 */
	@Override
	public int consultarNumeroConductoresConIncidenciasEnAsoc(String idasoc) throws PersistenceException {
		EntityManager em = createSession();
		logger.info("\n\nInicio transacción consultarNumeroConductoresConIncidenciasEnAsoc");

		try {
			beginTransaction(em);
			AsociacionDAO asociacionDAO = new AsociacionDAO(em);
			Asociacion asociacionBuscada = asociacionDAO.findById(idasoc);

			if (asociacionBuscada == null) {
				throw new IncidentException(IncidentError.NOT_EXIST_ASSOCIATION);
			}

			int nConductoresIncidencias = 0;

			for (Conductor conductor : asociacionBuscada.getConductores()) {
				if (conductor.getIncidencias().size() > 0) {
					nConductoresIncidencias++;
				}
			}

			commitTransaction(em);
			return nConductoresIncidencias;

		} catch (IncidentException e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
		} finally {
			close(em);
		}
		return -1;
	}
}
