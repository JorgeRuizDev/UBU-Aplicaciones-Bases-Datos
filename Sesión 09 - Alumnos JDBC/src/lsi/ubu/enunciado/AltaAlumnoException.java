package lsi.ubu.enunciado;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AltaAlumnoException:
 * Implementa las excepciones contextualizadas de la transaccion
 * de AltaAlumno
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jes�s Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Ra�l Marticorena</a>
 * @version 1.0
 * @since 1.0 
 */
public class AltaAlumnoException extends SQLException {

	
	private static final long serialVersionUID = 1L;
	
	public static final int NO_EXISTE_ASIG_O_GRUPO = 1;
	public static final int SIN_PLAZAS = 2;
		
	private final int codigo; // = -1;
	private final String mensaje;

	private static final Logger l = LoggerFactory.getLogger(AltaAlumnoException.class);

	public AltaAlumnoException(int code) {

		codigo = code;

		switch (codigo){
			case 1:
				mensaje = "Probablemente la asignatura o el grupo no exista.";
				break;
			case 2:
				mensaje = "No hay plazas para ese grupo";
				break;
			default:
				mensaje = "";

		}
		//l.error(mensaje);

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
