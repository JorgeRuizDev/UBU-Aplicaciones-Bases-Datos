package lsi.ubu.PracticaJDBC2021;

import java.io.Closeable;
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
			if (e.getErrorCode() == SQLException_banco.NO_EXISTE_CLIENTE
				&& e.getMessage().equals("No existe cliente con ese DNI"))
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
			if (e.getErrorCode() == SQLException_banco.NO_EXISTE_CTA &&
					e.getMessage().equals("No existe ese nro de cuenta corriente"))
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
			try {
				closeIt(conn);
				closeIt(st_check);
				closeIt(rs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("--------------------------------");
		//Pruebo que ese cliente ya existía con esa cuenta
		//volviendo a insertar altaAutorizado(1, 12345678);
		try {
			altaAutorizado(1, 12345678);
			System.out.println("Autorizacion repetida MAL");
		} catch (SQLException_banco e) {
			if (e.getErrorCode() == SQLException_banco.AUTORIZACION_REPETIDA
				&& e.getMessage().equals("Ese cliente ya habia sido autorizado para esa cuenta"))
				System.out.println("Autorizacion repetida OK");
			else
				System.out.println("Autorizacion repetida MAL");
		}


		System.out.println("--------------------------------");
		// SUBIR NOTA
		//Pruebo que el método solo ha realizado dos inserts:
		// El del Test 3 y el del Test 5.

		// No deberían verse reflejados los inserts que lancen excepciones
		try {
			assert st_check != null;
			conn = p.getConnection();
			st_check = conn.createStatement();

			altaAutorizado(1, 11111111);
			rs = st_check.executeQuery("select IDCTA||IDCLI" +
					" from CUENTAS_CLIENTES");
			StringBuilder recibido = new StringBuilder();
			String esperado = "1112";


			while(rs.next()) {
				recibido.append(rs.getString(1));
			}
			System.out.println(recibido);
			if (!recibido.toString().equals(esperado)) {
				System.out.println("Segundo Insert MAL:");
				System.out.println("La base de datos no se ha actualizado correctamente.");
			} else {
				System.out.println("Segundo Insert OK:");
				System.out.println("La base de datos se ha actualiazado correctamente.");
			}
		} catch (SQLException e) {
			l.error(e.getMessage());

			for (var st : e.getStackTrace())
				l.error(String.valueOf(st));
			System.out.println("Segundo Insert MAL");
		}
		finally{
			try {
				closeIt(conn);
				closeIt(st_check);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Método que permite reflejar en la base de datos la asociación
	 * entre un usuario y una cuenta corriente.
	 *
	 * Actualiza la tabla C
	 *
	 * @author Jorge Ruiz Gómez
	 * @param cta Entero con el ID de la cuenta a actualizar
	 * @param dni Entero con el ID del cliente a actualizar.
	 * @throws SQLException_banco: Excepción por fallo en los datos.
	 */
	public static void altaAutorizado(int cta, int dni) throws SQLException_banco {

		PoolDeConexiones pool = PoolDeConexiones.getInstance();
		Connection conn = null;

		TableError errors = new OracleTableError();

		PreparedStatement fetchCliente = null;
		PreparedStatement insertaCliente = null;

		ResultSet resCliente = null;

		// Cambio correspondiente a una opción para subir nota.
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

					// SUBIR NOTA
					if (rowCount == 0) {
						throw new SQLException_banco(SQLException_banco.EJECUCION_SIN_CAMBIOS);
					} else if (rowCount != 1) {
						throw new SQLException_banco(SQLException_banco.CAMBIOS_INESPERADOS);
					}
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
			// Relanzamos la excepción
			throw e;
		} catch (Exception e) {
			// Atrapamos cualquier excepción.
			logException(e);
			rollback(conn);

		} finally {
			try {
				closeIt(conn);
				closeIt(fetchCliente);
				closeIt(insertaCliente);
				closeIt(resCliente);
			} catch (Exception e) {
				// Logueamos CUALQUIER excepción.
				logException(e);
			}

		}


	}

	/**
	 * Método de cierre de recursos.
	 *  SUBIR NOTA
	 * @author Jorge Ruiz Gómez
	 * @param object El objeto AutoCloseable a cerrar.
	 * @throws Exception: Una excepción, muy probablemente SQLException.
	 */
	public static void closeIt(AutoCloseable object) throws Exception {
		/*
		 Sabemos que todos los recursos de la práctica extieden de la interfaz
		 AutoCloseable, por lo que podríamos usar un Try-With-Resoruces.

		 En este caso no hemso usado dicha estructura porque no nos da tanta
		 flexibilidad a la hora de tratar la excepciones como usar un Finally.
		*/
		if (object != null)
			object.close();
	}

	/**
	 * SUBIR NOTA
	 * Método que añade al log la excepción.
	 * @param e: Una excepción
	 */
	public static void logException(Exception e) {
		l.error(e.getMessage());
		l.error(e.getLocalizedMessage());

		for (var st : e.getStackTrace())
			l.info(st.toString());
	}

	/**
	 * SUBIR NOTA
	 * Método que realiza un rollback en la transacción.
	 * @param conn Conexión en la que está ocurriendo la transacción.
	 */
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


