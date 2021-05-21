package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Conductor;

import javax.persistence.EntityManager;
import java.util.List;

public class ConductorDAO extends JpaDAO<Conductor, String> {
	/**
	 * Constructor.
	 *
	 * @param em
	 */
	public ConductorDAO(EntityManager em) {
		super(em);
	}

	@Override
	public List<Conductor> findAll() {
		return null;
	}
}
