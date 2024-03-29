package es.ubu.lsi.service.asociacion;

/**
 * Error code.
 * 
 * Listado de posibles errores que se pueden producir.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jesús Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @author <a href="mailto:mmabad@ubu.es">Mario Martínez</a>
 * @since 1.0
 *
 */
public enum IncidentError {
	
	// If we have more error messages, add new values to the the end of enum type...
	NOT_EXIST_INCIDENT_TYPE("No existe tipo de incidencia"),
	NOT_AVAILABLE_POINTS("No tiene puntos disponibles para la sanción"),
	ERROR_IN_DATE("Fecha y/o hora incorrecta"),
	NOT_EXIST_ASSOCIATION("No existe la asociacion"),
	NO_INCIDENTS_IN_DB("La base de datos no tiene ningun tipo de incidencia registrada"),
	NO_ASSOCIATION_IN_DB("No hay asociaciones registradas en la base de datos"),
	NOT_EXIST_DRIVER("No existe conductor"),
	STRING_LENGTH_EXCEDED("La cadena introducida es demasiado larga"),
	ILLEGAL_INT("El valor del entero no es apto"),
	;
	/** Text. */
	private String text;
	
	/** Constructor. */
	private IncidentError(String text) {
		this.text = text;
	}

	/**
	 * Gets text.
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}
}
