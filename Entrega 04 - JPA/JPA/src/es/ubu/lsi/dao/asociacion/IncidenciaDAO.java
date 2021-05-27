package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Incidencia;
import es.ubu.lsi.model.asociacion.IncidenciaPK;
import es.ubu.lsi.model.asociacion.TipoIncidenciaRanking;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.asociacion.IncidentError;
import es.ubu.lsi.service.asociacion.IncidentException;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

public class IncidenciaDAO extends JpaDAO<Incidencia, IncidenciaPK> {

	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public IncidenciaDAO(EntityManager em) {
		super(em);
	}

	/**
	 * @{inheritdoc}
	 * @return @{inheritdoc}
	 */
	@Override
	public List<Incidencia> findAll() {
		return getEntityManager().createNamedQuery("Incidencia.findAll", Incidencia.class).getResultList();
	}

	/**
	 * Obtiene una lista con los distintos tipos de incidencias y su número de incidencias activas.
	 * @return Lista
	 * @throws PersistenceException Si la base de datos no tiene ningún tipo de incidencia.
	 */
	public List<TipoIncidenciaRanking> consultarTipoIncidenciaRanking() throws PersistenceException {

		List<TipoIncidenciaRanking> lista = new LinkedList<>();

		Query query = getEntityManager().createNamedQuery("Incidencia.consultarRanking");

		List<Object[]> resultados = query.getResultList();

		if (resultados == null || resultados.size() == 0) {
			throw new IncidentException(IncidentError.NO_INCIDENTS_IN_DB);
		}

		for (Object[] e : resultados)
			lista.add(new TipoIncidenciaRanking(e[0].toString(), (long) e[1]));

		return lista;
	}

}
