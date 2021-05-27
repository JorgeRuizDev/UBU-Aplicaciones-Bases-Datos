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

	@Override
	public List<Incidencia> findAll() {
		return getEntityManager().createNamedQuery("Incidencia.findAll", Incidencia.class).getResultList();
	}

	public List<TipoIncidenciaRanking> consultarTipoIncidenciaRanking() throws PersistenceException {

		Query query = getEntityManager().createQuery("SELECT ti.descripcion as des, COUNT(i.tipoIncidencia) from Incidencia i right join i.tipoIncidencia ti group by ti.descripcion order by des desc");
		//query = getEntityManager().createQuery("SELECT ti.descripcion, i.id from TipoIncidencia ti join ti.incidencias i");
		List<TipoIncidenciaRanking> lista = new LinkedList<>();

		List<Object[]> resultados = query.getResultList();

		if (resultados == null || resultados.size() == 0) {
			throw new IncidentException(IncidentError.NO_INCIDENTS_REGISTERED);
		}

		for (Object[] e : resultados)
			lista.add(new TipoIncidenciaRanking(e[0].toString(), (long) e[1]));

		return lista;
	}

}
