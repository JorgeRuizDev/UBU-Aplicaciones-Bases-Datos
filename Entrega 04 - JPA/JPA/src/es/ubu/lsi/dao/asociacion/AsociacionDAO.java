package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Asociacion;
import es.ubu.lsi.model.asociacion.Conductor;

import javax.persistence.EntityManager;
import java.util.List;

public class AsociacionDAO extends JpaDAO<Asociacion, String> {
	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public AsociacionDAO(EntityManager em) {
		super(em);
	}

	@Override
	public List<Asociacion> findAll() {
		return getEntityManager().createNamedQuery("Asociacion.findAll", Asociacion.class).getResultList();
	}
}
