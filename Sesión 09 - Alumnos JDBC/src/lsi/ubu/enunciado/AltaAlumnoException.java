package lsi.ubu.enunciado;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AltaAlumnoException:
 * Implementa las excepciones contextualizadas de la transaccion
 * de AltaAlumno
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jesús Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @version 1.0
 * @since 1.0 
 */
public class AltaAlumnoException extends SQLException {

	
	private static final long serialVersionUID = 1L;
	
	public static final int NO_EXISTE_ASIG_O_GRUPO = 1;
	public static final int SIN_PLAZAS = 2;
		
	private int codigo; // = -1;
	private String mensaje;

	private static Logger l = LoggerFactory.getLogger(AltaAlumnoException.class);	

	public AltaAlumnoException(int code) {
		/*
		 * A completar por el alumno
		 */

		l.error(mensaje);

		// Traza_de_pila
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			l.info(ste.toString());		}

	}

	@Override
	public String getMessage() { // Redefinicion del metodo de la clase
									// Exception
		return mensaje;
	}

	@Override
	public int getErrorCode() { // Redefinicion del metodo de la clase
								// SQLException
		return codigo;
	}

}
