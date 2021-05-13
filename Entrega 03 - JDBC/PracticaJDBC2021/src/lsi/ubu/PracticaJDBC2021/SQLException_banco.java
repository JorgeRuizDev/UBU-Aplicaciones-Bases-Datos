package lsi.ubu.PracticaJDBC2021;

import java.sql.SQLException;

import lsi.ubu.OracleTableError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLException_banco extends SQLException {


	public static final int NO_EXISTE_CLIENTE = -1;
	public static final int NO_EXISTE_CTA = -2;
	public static final int AUTORIZACION_REPETIDA = -3;
	public static final int CAMBIOS_INESPERADOS = -4;
	public static final int EJECUCION_SIN_CAMBIOS = -5;
	public static final int ERROR_INESPERADO = -2000;
	private static final long serialVersionUID = 1L;
	private static Logger l = null;
	private final int codigo; // = -1;
	private String mensaje;

	public SQLException_banco(int code) {
		codigo = code;
		build_message();

		l = LoggerFactory.getLogger(SQLException_banco.class);
		l.error(mensaje);

		// Traza_de_pila
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			l.info(ste.toString());
		}
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

	private void build_message() {
		switch (this.codigo) {
			case ERROR_INESPERADO:
				mensaje = "Transacción Fallida, error inesperado, consulte el log para más información";
				break;
			case NO_EXISTE_CLIENTE:
				mensaje = "No existe cliente con ese DNI";
				break;
			case NO_EXISTE_CTA:
				mensaje = "No existe ese nro de cuenta corriente";
				break;
			case AUTORIZACION_REPETIDA:
				mensaje = "Ese cliente ya habia sido autorizado para esa cuenta";
				break;

			//SUBIR NOTA: Nuevas excepciones:
			case CAMBIOS_INESPERADOS:
				mensaje = "La actualización de la base de datos ha actualizado más filas de las esperadas";
				break;
			case EJECUCION_SIN_CAMBIOS:
				mensaje = "La actualización no ha actualizado ninguna fila, error desconocido";
				break;
			default:
				mensaje = "Código de error desconocido";
				break;
		}
	}
}
