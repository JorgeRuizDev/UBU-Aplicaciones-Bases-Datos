package es.ubu.lsi.dao.asociacion;

import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.asociacion.Asociacion;
import es.ubu.lsi.model.asociacion.Conductor;

import javax.persistence.EntityGraph;
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

	/**
	 * @{inheritdoc}
	 * @return @{inheritdoc}
	 */
	@Override
	public List<Asociacion> findAll() {
		return getEntityManager().createNamedQuery("Asociacion.findAll", Asociacion.class).getResultList();
	}

	/**
	 *
	 */
	public List<Asociacion> findAllWithGraph(){
		EntityGraph<?> grafo = getEntityManager().createEntityGraph("graph.AsociacionesConductoresIncidenciasTipos");

		return getEntityManager().createNamedQuery("Asociacion.findAll", Asociacion.class)
				.setHint("javax.persistence.fetchGraph", grafo).getResultList();
	}
}
