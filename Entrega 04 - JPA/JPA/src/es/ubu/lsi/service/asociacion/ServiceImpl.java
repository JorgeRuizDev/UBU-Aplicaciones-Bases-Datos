package es.ubu.lsi.service.asociacion;

import es.ubu.lsi.dao.asociacion.ConductorDAO;
import es.ubu.lsi.dao.asociacion.IncidenciaDAO;
import es.ubu.lsi.dao.asociacion.TipoIncidenciaDAO;
import es.ubu.lsi.model.asociacion.*;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceImpl extends PersistenceService implements Service{

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceImpl.class);


	private static void logException(Exception e){
		logger.error(e.getLocalizedMessage());
		System.out.println("Habemus Excepción");
		System.out.println(e.getLocalizedMessage());
		for (StackTraceElement trace : e.getStackTrace()){
			logger.info(e.toString());
			System.out.println(e.toString());
		}
	}

	@Override
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException {
		EntityManager em = this.createSession();
		try {
			beginTransaction(em);
			IncidenciaDAO incidenciaDAO = new IncidenciaDAO(em);
			TipoIncidenciaDAO tipoInciDAO = new TipoIncidenciaDAO(em);
			ConductorDAO conductorDAO = new ConductorDAO(em);

			Conductor conductor = conductorDAO.findById(nif);

			if (conductor == null){
				throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
			}

			TipoIncidencia tipoIncidencia = tipoInciDAO.findById(tipo);

			if (tipoIncidencia == null){
				throw new IncidentException(IncidentError.NOT_EXIST_INCIDENT_TYPE);
			}


			conductor.setPuntos(new BigDecimal(Math.max(conductor.getPuntos().subtract(tipoIncidencia.getValor()).intValue(), 0)));

			conductorDAO.persist(conductor);

			Incidencia nuevaIncidencia = new Incidencia(conductor, fecha, tipoIncidencia);

			incidenciaDAO.persist(nuevaIncidencia);

			System.out.println(incidenciaDAO.findById(new IncidenciaPK(nif, fecha)));

			commitTransaction(em);

		} catch (IncidentException e) {
			logException(e);
			rollbackTransaction(em);
			throw e;
		} catch (Exception e){
			rollbackTransaction(em);
			logException(e);
		}
		finally {
			close(em);
		}
	}

	@Override
	public void indultar(String nif) throws PersistenceException {

	}

	@Override
	public List<Asociacion> consultarAsociaciones() throws PersistenceException {
		return null;
	}

	@Override
	public List<TipoIncidenciaRanking> consultarRanking() throws PersistenceException {
		return new LinkedList<>();
	}

	@Override
	public void insertarTipoIncidencia(String descripcion, int valor) throws PersistenceException {

		EntityManager em = createSession();

		try{
			beginTransaction(em);
			TipoIncidenciaDAO incidenciaDAO = new TipoIncidenciaDAO(em);

			TipoIncidencia nuevoTipoIncidencia = new TipoIncidencia(descripcion, new BigDecimal(valor));

			incidenciaDAO.persist(nuevoTipoIncidencia);





			commitTransaction(em);
		}
		catch(Exception e){
			logException(e);
			throw e;
		}
		finally {
			close(em);
		}


	}

	@Override
	public int consultarNumeroConductoresConIncidenciasEnAsoc(String idasoc) throws PersistenceException {
		return 0;
	}
}
