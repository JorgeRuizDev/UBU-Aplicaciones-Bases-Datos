package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Conductor;
import es.ubu.lsi.model.asociacion.TipoIncidencia;

import javax.persistence.EntityManager;
import java.util.List;

public class TipoIncidenciaDAO extends JpaDAO<TipoIncidencia, Long> {
	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public TipoIncidenciaDAO(EntityManager em) {
		super(em);
	}

	@Override
	public List<TipoIncidencia> findAll() {
		return getEntityManager().createNamedQuery("TipoIncidencia.findAll", TipoIncidencia.class).getResultList();
	}
}
