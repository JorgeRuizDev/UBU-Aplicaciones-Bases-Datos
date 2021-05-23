package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Incidencia;
import es.ubu.lsi.model.asociacion.IncidenciaPK;

import javax.persistence.EntityManager;
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
}
