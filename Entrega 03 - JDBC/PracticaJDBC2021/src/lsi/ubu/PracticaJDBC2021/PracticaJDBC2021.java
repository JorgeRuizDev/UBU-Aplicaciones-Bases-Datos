package lsi.ubu.PracticaJDBC2021;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lsi.ubu.util.ExecuteScript;
import lsi.ubu.OracleTableError;
import lsi.ubu.util.PoolDeConexiones;
import lsi.ubu.TableError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticaJDBC2021 {

	private static final String script_path = "sql/";
	private static PoolDeConexiones p = null;
	private static Logger l = null;

	public static void main(String[] argv) {
		inicializaciones();
		pruebaAltaAutorizado();

		System.out.println("FIN.............");
	}

	static void pruebaAltaAutorizado() {

		creaTablas();

		//Pruebo a dar de alta un autorizado que no existe
		//Doy de alta en la cuenta 1 al cliente con DNI 44444 que no existe
		try {
			altaAutorizado(1, 44444);
			System.out.println("Cliente inexistente MAL");
		} catch (SQLException_banco e) {
			if (e.getErrorCode() == SQLException_banco.NO_EXISTE_CLIENTE)
				System.out.println("Cliente inexistente OK");
			else
				System.out.println("Cliente inexistente MAL");
		}
		System.out.println("--------------------------------");

		//Pruebo a dar de alta un autorizado en una cuenta que no existe
		//cuenta=2 y el dni=12345678
		try {
			altaAutorizado(2, 12345678);
			System.out.println("Cuenta inexistente MAL");
		} catch (SQLException_banco e) {
			if (e.getErrorCode() == SQLException_banco.NO_EXISTE_CTA)
				System.out.println("Cuenta inexistente OK");
			else
				System.out.println("Cuenta inexistente MAL");
		}
		System.out.println("--------------------------------");
		//Caso correcto
		//cuenta=1 y el dni=12345678

		Connection conn = null;
		Statement st_check = null;
		ResultSet rs = null;

		try {
			altaAutorizado(1, 12345678);

			conn = p.getConnection();
			st_check = conn.createStatement();
			rs = st_check.executeQuery(
					"SELECT * FROM cuentas_clientes " +
							"WHERE idCli = 1 AND idCta = 1");

			if (rs.next()) {
				System.out.println("Caso todo correcto OK");
			} else {
				System.out.println("Caso todo correcto  MAL");
			}

		} catch (SQLException e) {
			l.error(e.getMessage());
		} finally {


		}
		System.out.println("--------------------------------");
		//Pruebo que ese cliente ya exist?a con esa cuenta
		//volviendo a insertar altaAutorizado(1, 12345678);
		try {
			altaAutorizado(1, 12345678);
			System.out.println("Autorizaci?n repetida MAL");
		} catch (SQLException_banco e) {
			if (e.getErrorCode() == SQLException_banco.AUTORIZACION_REPETIDA)
				System.out.println("Autorizaci?n repetida OK");
			else
				System.out.println("Autorizaci?n repetida MAL");
		}

	}

	public static void altaAutorizado(int cta, int dni) throws SQLException_banco {
		// Inserta en CUENTAS_CLIENTES la asociación cta dni

		PoolDeConexiones pool = PoolDeConexiones.getInstance();
		Connection conn = null;

		TableError errors = new OracleTableError();

		PreparedStatement fetchCliente = null;
		PreparedStatement insertaCliente = null;

		ResultSet resCliente = null;

		TableError tablaErrores = new OracleTableError();

		try {
			// Obtén una conexión de la pool de conexiones.
			conn = pool.getConnection();

			fetchCliente = conn.prepareStatement("" +
					" select IDCLIENTE" +
					" from CLIENTES" +
					" where DNI = ?");

			insertaCliente = conn.prepareStatement("" +
					" insert into CUENTAS_CLIENTES(IDCTA, IDCLI)" +
					" values (?, ?)");

			fetchCliente.setInt(1, dni);

			resCliente = fetchCliente.executeQuery();

			if (!resCliente.next()) {

				throw new SQLException_banco(SQLException_banco.NO_EXISTE_CLIENTE);
			} else {
				insertaCliente.setInt(1, cta);
				insertaCliente.setInt(2, resCliente.getInt("IDCLIENTE"));


				try {
					int rowCount = insertaCliente.executeUpdate();
				} catch (SQLException e) {
					/*
					 * Capturo la Excepción en este bloque de código y no en el catch
					 * exterior porque da una mayor flexibilidad en la aplicación en un futuro.
					 *
					 * Podría capturar un FK_VIOLATED en el catch exterior, pero si añado otra
					 * sentencia que pueda lanzar el mismo error, el responder a ambos FK_VIOLATED
					 * con "No existe ese nro de cuenta corriente" no es la mejor opción, ya que
					 * puede estar relacionado con otra tabla totalmente distinta.
					 * */

					if (tablaErrores.checkExceptionToCode(e, TableError.FK_VIOLATED)) {
						throw new SQLException_banco(SQLException_banco.NO_EXISTE_CTA);
					} else if (tablaErrores.checkExceptionToCode(e, TableError.UNQ_VIOLATED)) {
						throw new SQLException_banco(SQLException_banco.AUTORIZACION_REPETIDA);
					} else {
						throw e;
					}
				}
			}

			conn.commit();

		} catch (SQLException_banco e) {
			rollback(conn);
			logException(e);
			// Relanzamos la excepción
			throw e;
		} catch (Exception e) {
			// Atrapamos cualquier excepción.
			rollback(conn);
			logException(e);
		} finally {
			try {
				if (conn != null) conn.close();
				if (fetchCliente != null) fetchCliente.close();
				if (insertaCliente != null) insertaCliente.close();
				if (resCliente != null) resCliente.close();
			} catch (Exception e) {
				// Logueamos CUALQUIER excepción.
				logException(e);
			}

		}


	}

	public static void logException(Exception e) {
		l.error(e.getMessage());
		l.error(e.getLocalizedMessage());

		for (var st : e.getStackTrace())
			l.info(st.toString());
	}

	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				logException(e);
			}
		}
	}

	static public void creaTablas() {
		ExecuteScript.run(script_path + "script_PracticaJDBC2021.sql");
	}

	static public void inicializaciones() {
		try {
			//Acuerdate de q la primera vez tienes que crear el .bindings con:
			// PoolDeConexiones.reconfigurarPool();

			// Inicializacion de Pool
			p = PoolDeConexiones.getInstance();

			// Inicializacion del Logger
			l = LoggerFactory.getLogger(PracticaJDBC2021.class);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}  


