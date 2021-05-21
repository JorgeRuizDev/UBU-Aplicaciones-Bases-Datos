package es.ubu.lsi.service.asociacion;

import java.util.Date;
import java.util.List;

import es.ubu.lsi.model.asociacion.Asociacion;
import es.ubu.lsi.model.asociacion.TipoIncidenciaRanking;
import es.ubu.lsi.service.PersistenceException;

/**
 * Transaction service.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jesús Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @author <a href="mailto:mmabad@ubu.es">Mario Martínez</a>
 * @since 1.0
 *
 */
public interface Service {

	/** Máximo de puntos con el que se inicia el carné por puntos. */
	public static final int MAXIMO_PUNTOS = 12;

	/**
	 * Alta de una nueva incidencia sobre un conductor.
	 * 
	 * @param fecha fecha
	 * @param nif   nif
	 * @param tipo  tipo de incidencia
	 * @throws PersistenceException si se produce un error
	 * @see es.ubu.lsi.service.asociacion.IncidentError
	 */
	public void insertarIncidencia(Date fecha, String nif, long tipo) throws PersistenceException;

	/**
	 * Elimina todas las incidencias de un conductor poniendo sus puntos de nuevo a
	 * máximo.
	 * 
	 * @param nif nif
	 * @throws PersistenceException si se produce un error
	 * @see es.ubu.lsi.service.asociacion.IncidentError
	 */
	public void indultar(String nif) throws PersistenceException;

	/**
	 * Consulta las asociaciones. En este caso en particular es importante recuperar
	 * toda la información vinculada a los conductores, incidencias y tipo de
	 * incidencia para su visualización posterior.
	 * 
	 * @return asociaciones
	 * @throws PersistenceException si se produce un error
	 */
	public List<Asociacion> consultarAsociaciones() throws PersistenceException;

	/**
	 * Consulta el ranking de tipos de incidencias devolviendo descripcion y número
	 * de apariciones de cada tipo de incidencia (utilizando una clase de envoltura
	 * TipoIncidenciaRanking.
	 * 
	 * @return ranking con la descrición y frecuencia de cada tipo de incidencia
	 *         ordenado de mayor a menor frecuencia
	 * @throws PersistenceException si se produce un error
	 */
	public List<TipoIncidenciaRanking> consultarRanking() throws PersistenceException;

	/**
	 * Inserta un nuevo tipo de incidencia.
	 * 
	 * @param descripcion descripción
	 * @param valor       valor de puntos a restar
	 * @throws PersistenceException si se produce un error
	 */
	public void insertarTipoIncidencia(String descripcion, int valor) throws PersistenceException;

	/**
	 * Consulta el número de conductores pertenecientes a una asociación que tienen
	 * alguna incidencia.
	 * 
	 * @param idasoc identificador de asociación
	 * @return número de conductores de la asociación con incidencias
	 * @throws PersistenceException si se produce un error
	 */
	public int consultarNumeroConductoresConIncidenciasEnAsoc(String idasoc) throws PersistenceException;
}
