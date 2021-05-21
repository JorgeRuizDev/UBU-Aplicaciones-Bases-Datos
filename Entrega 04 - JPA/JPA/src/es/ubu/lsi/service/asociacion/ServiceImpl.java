package es.ubu.lsi.service.asociacion;

import es.ubu.lsi.dao.asociacion.ConductorDAO;
import es.ubu.lsi.dao.asociacion.IncidenciaDAO;
import es.ubu.lsi.model.asociacion.Asociacion;
import es.ubu.lsi.model.asociacion.Conductor;
import es.ubu.lsi.model.asociacion.Incidencia;
import es.ubu.lsi.model.asociacion.TipoIncidenciaRanking;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

import javax.persistence.EntityManager;
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

		for (StackTraceElement trace : e.getStackTrace()){
			logger.info(e.toString());
		}
	}

	@Override
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException {
		EntityManager em = this.createSession();
		try {
			beginTransaction(em);
			System.out.println("HOLA");
			logger.error("hooola");
			IncidenciaDAO incidenciaDAO = new IncidenciaDAO(em);

			ConductorDAO conductorDAO = new ConductorDAO(em);

			Conductor conductor = conductorDAO.findById(nif);
			if (conductor == null){
				throw new IncidentException(IncidentError.NOT_EXIST_DRIVER);
			}




		} catch (IncidentException e) {
			logException(e);
			rollbackTransaction(em);
			throw e;
		} catch (Exception e){
			logException(e);
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

	}

	@Override
	public int consultarNumeroConductoresConIncidenciasEnAsoc(String idasoc) throws PersistenceException {
		return 0;
	}
}
