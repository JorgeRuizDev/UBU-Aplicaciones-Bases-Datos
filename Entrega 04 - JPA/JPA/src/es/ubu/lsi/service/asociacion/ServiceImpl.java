package es.ubu.lsi.service.asociacion;

import es.ubu.lsi.dao.asociacion.AsociacionDAO;
import es.ubu.lsi.dao.asociacion.ConductorDAO;
import es.ubu.lsi.dao.asociacion.IncidenciaDAO;
import es.ubu.lsi.dao.asociacion.TipoIncidenciaDAO;
import es.ubu.lsi.model.asociacion.*;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class ServiceImpl extends PersistenceService implements Service {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceImpl.class);

	/**
	 * Función que permite loguear una excepción.
	 *
	 * @param e Excepción a Loguear.
	 */
	private static void logException(Exception e) {
		logger.error(e.getLocalizedMessage());

		for (StackTraceElement trace : e.getStackTrace()) {
			logger.info(trace.toString());
		}
	}

	/**
	 * Comprobar nifs.
	 * @param nif Lanza una excepción si el nif no es válido.
	 * @throws IncidentException excepción.
	 */
	private void checkNif(String nif) throws IncidentException {
		assert nif != null;
		if (nif.length() != 9){
			throw new IncidentException(IncidentError.STRING_LENGTH_EXCEDED);
		}

		/*
		 char letraControl = nif.charAt(nif.length()-1);

		 Aquí se comprobaría que el nif es válido, como se usan nifs inventados, no vamos comprobar nada
		 */


	}



	/**
	 * @param fecha @{inheritdoc}
	 * @param nif   @{inheritdoc}
	 * @param tipo  @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
	 */
	@Override
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException {
		EntityManager em = this.createSession();

		checkNif(nif);

		// Si la fecha introducida es superior a la actual: No se pueden insertar incidencias del futuro:
		if (fecha == null || fecha.compareTo(new Date(System.currentTimeMillis())) > 0){
			throw new IncidentException(IncidentError.ERROR_IN_DATE);
		}

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

		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} finally {
			close(em);
		}
	}

	/**
	 * @param nif @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
	 */
	@Override
	public void indultar(String nif) throws PersistenceException {
		EntityManager em = createSession();
		logger.info("\n\nInicio transacción indultar");

		checkNif(nif);

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
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
			throw e;

		} finally {
			close(em);
		}
	}

	/**
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
	 */
	@Override
	public List<Asociacion> consultarAsociaciones() throws PersistenceException {

		EntityManager em = createSession();
		logger.info("\n\nInicio transacción consultarAsociaciones");
		try {
			beginTransaction(em);

			AsociacionDAO asociacionDAO = new AsociacionDAO(em);

			List<Asociacion> asociaciones = asociacionDAO.findAllWithGraph();


			if (asociaciones == null || asociaciones.size() == 0)
				throw new IncidentException(IncidentError.NO_ASSOCIATION_IN_DB);

			commitTransaction(em);

			return asociaciones;
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} finally {
			close(em);
		}
	}


	/**
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
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
		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} finally {
			close(em);
		}

	}

	/**
	 * @param descripcion @{inheritdoc}
	 * @param valor       @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
	 */
	@Override
	public void insertarTipoIncidencia(String descripcion, int valor) throws PersistenceException {

		EntityManager em = createSession();
		logger.info("\n\nInicio transacción insertarTipoIncidencia");


		// length=30 no produce validación, únicamente lanza una SQLEXCEPTION.
		// https://www.baeldung.com/jpa-size-length-column-differences
		//
		if (descripcion.length() > TipoIncidencia.getLongDescripcion()) {
			throw new IncidentException(IncidentError.STRING_LENGTH_EXCEDED);
		}

		if (valor < 0) {
			throw new IncidentException(IncidentError.ILLEGAL_INT);
		}


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
	 * @param idasoc @{inheritdoc}
	 * @return @{inheritdoc}
	 * @throws PersistenceException @{inheritdoc}
	 * @{inheritdoc}
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

		} catch (Exception e) {
			rollbackTransaction(em);
			logException(e);
			throw e;
		} finally {
			close(em);
		}

	}
}
