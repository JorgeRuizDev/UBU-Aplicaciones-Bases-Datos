package lsi.ubu.PracticaJDBC2021;

import java.sql.SQLException;

import lsi.ubu.OracleTableError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLException_banco extends SQLException {


	public static final int NO_EXISTE_CLIENTE = -1;
	public static final int NO_EXISTE_CTA = -2;
	public static final int AUTORIZACION_REPETIDA = -3;
	private static final long serialVersionUID = 1L;
	private static Logger l = null;
	private final int codigo; // = -1;
	private String mensaje;

	public SQLException_banco(int code) {
		codigo = code;


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
			case NO_EXISTE_CLIENTE:
				mensaje = "No existe cliente con ese DNI";
				break;
			case NO_EXISTE_CTA:
				mensaje = "No existe ese nro de cuenta corriente";
				break;
			case AUTORIZACION_REPETIDA:
				mensaje = "Ese cliente ya había sido autorizado para esa cuenta";
				break;
			default:
				mensaje = "Código de error desconocido";
				break;
		}
	}
}
