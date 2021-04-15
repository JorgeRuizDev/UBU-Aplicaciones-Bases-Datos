package lsi.ubu.tenis;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsi.ubu.util.ExecuteScript;
import lsi.ubu.util.PoolDeConexiones;

/**
 * Pista de Tenis:
 * Implementa una reserva y una anulacion de reserva de una pista de tenis por un socio
 * Es un ejercicio de iniciacion, pues no tiene todavia tratamiento de excepciones
 *
 * @author <a href="mailto:jmaudes@ubu.es">Jesus Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raul Marticorena</a>
 * @version 1.0
 * @since 1.0
 */
public class PlantillaPistasDeTenis {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PlantillaPistasDeTenis.class);

	public static void main(String[] argv) throws SQLException {

		/*
		 * Main conteniendo el test
		 */
		logger.info("Comienzo test pista de tenis ...");
		// Crea las tablas y filas de prueba
		ExecuteScript.run("sql/carga_tenis.sql");

		LocalDate hoy = LocalDate.now();
		Date ahoraSQLDate = Date.valueOf(hoy);

		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			PoolDeConexiones pool = PoolDeConexiones.getInstance();

			/* Prueba de reservarPista***************************** */
			// Prueba 1 (tabla de psitas vacia)
			boolean retorno = reservarPista("Ana", ahoraSQLDate, 11);
			if (retorno)
				System.out.println("MAL: No pasa la prueba de la reserva de la pista vacia");
			else
				System.out.println("OK: Si pasa la prueba de la reserva de la pista vacia");

			// Prueba 2 (inserto 2 pistas)
			/* Crea una conexion e inserta 2 pistas usando la secuencia
			 * haz commit y cierra la conexion
			 */

			con = pool.getConnection();
			st = con.prepareStatement("INSERT INTO PISTAS VALUES (SEC_NUM_PISTA.NEXTVAL)");
			st.executeUpdate();//Inserta pista 1
			st.executeUpdate();//Inserta pista 2
			con.commit();

			st.close();
			st = con.prepareStatement("SELECT fecha, hora, pista, socio FROM reservas");

			retorno = reservarPista("Ana", ahoraSQLDate, 11);
			boolean todoOK = true;
			if (retorno) {
				rs = st.executeQuery();
				int i = 0;
				while (rs.next()) {

					if (i > 0) {
						System.out.println("MAL: Mas de 1 fila en la prueba de la reserva correcta");
						break;
					}

					i++;

					Date d = rs.getDate("fecha");
					todoOK = todoOK && (d.compareTo(ahoraSQLDate) == 0);

					int h = rs.getInt("hora");
					todoOK = todoOK & (h == 11);

					rs.getInt("pista");
					todoOK = todoOK & !rs.wasNull();

					String s = rs.getString("socio");
					todoOK = todoOK & (s.compareTo("Ana") == 0);
				}

				if (todoOK && i == 1)
					System.out.println("OK: Si pasa la prueba de la reserva correcta");
				else if (i == 0)
					System.out.println("MAL: No se ha insertado ninguna reserva");
				else
					System.out.println("MAL: Dato incorrecto en la prueba de la reserva correcta");
			} else
				System.out.println("MAL: No pasa la prueba de la reserva correcta");


			/* Prueba de anularReserva******************************** */
			// Ese socio no tenia reservada esa pista a esa fecha y hora
			retorno = anularReserva("Ana", ahoraSQLDate, 1, 1);
			if (retorno)
				System.out.println("MAL: No pasa la prueba de la anulacion incorrecta");
			else
				System.out.println("OK: Si pasa la prueba de la anulacion incorrecta");

			rs.close();
			st.close();

			st = con.prepareStatement("DELETE FROM RESERVAS");
			st.executeUpdate();
			st = con.prepareStatement("INSERT INTO RESERVAS VALUES (trunc(current_date), 11, 1, 'Ana')");
			st.executeUpdate();
			con.commit();

			st = con.prepareStatement("SELECT count(*) FROM reservas");

			// Anulacion OK => Tiene que volver a dejar vacia la tabla de reservas
			retorno = anularReserva("Ana", ahoraSQLDate, 11, 1);
			if (retorno) {
				rs = st.executeQuery();
				rs.next();
				if (rs.getInt(1) == 0)
					System.out.println("OK: Si pasa la prueba de la anulacion correcta");
				else
					System.out.println("MAL: En la prueba de la anulacion correcta no tenia que haber filas");
			} else
				System.out.println("MAL: No pasa la prueba de la anulacion correcta");

			con.commit();

		} catch (Exception e) {
			con.rollback();
			logger.error(e.getMessage());
			logger.error(e.getLocalizedMessage());

		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}

	/**
	 * Anula una reserva de un socio, en una pista, fecha y hora determinadas.
	 *
	 * @param socio Nombre del socio que hizo la reserva.
	 * @param fecha Fecha de la reserva.
	 * @param hora  Hora de la reserva.
	 * @param pista Numero de la pista reservada.
	 * @return true si la operacion tuvo exito, false en caso contrario.
	 * @throws SQLException en caso de error imprevisto SQL o del SGBD
	 */
	public static boolean anularReserva(String socio, Date fecha, int hora,
	                                    int pista) throws SQLException {

		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		boolean retorno = true;
		Connection conn = null;
		PreparedStatement st = null;

		try {
			conn = pool.getConnection();
			st = conn.prepareStatement(
					"DELETE from Reservas " +
							"where Fecha = ? and Hora = ? and Pista = ? and socio = ?"
			);

			st.setDate(1, fecha);
			st.setInt(2, hora);
			st.setInt(3, pista);
			st.setString(4, socio);

			int rs = st.executeUpdate();
			if (rs == 0)
				retorno = false;
			conn.commit();
		} catch (SQLException e) {
			/*
			 Si hay un error, devuelve false, hace rollback y a�ade un mensaje al logger.
			 */

			//Faltan un par de cosas a rellenar por el alumno
			assert conn != null;
			conn.rollback();
			retorno = false;
			logger.error(e.getMessage());
			throw e;
		} finally {
			if (st != null) st.close();
			if (conn != null) conn.close();
		}

		return retorno;
	}

	/**
	 * Realiza una reserva para un socio en una fecha y hora determinadas. El sistema se encargara de buscar una pista libre
	 * en la fecha y hora solicitadas.
	 *
	 * @param socio Nombre del socio que realiza la reserva.
	 * @param fecha Fecha en la que se desea reservar una pista.
	 * @param hora  Hora en la que se desea reservar una pista.
	 * @return true si la operacion tuvo exito, false en caso contrario.
	 * @throws SQLException en caso de error imprevisto SQL o del SGBD
	 */
	public static boolean reservarPista(String socio, Date fecha, int hora) throws SQLException {

		PoolDeConexiones pool = PoolDeConexiones.getInstance();

		boolean retorno = true;
		Connection conn = null;
		PreparedStatement st_select = null, st_insert = null;
		ResultSet rs = null;

		try {
			conn = pool.getConnection();
			st_select = conn.prepareStatement("" +
					"        SELECT nro " +
					"        FROM pistas " +
					"        WHERE nro NOT IN ( " +
					"            SELECT pista " +
					"            FROM reservas " +
					"            WHERE " +
					"                trunc(fecha) = trunc(?) AND " +
					"                hora = ?) " +
					"        order by nro ");

			st_insert = conn.prepareStatement("INSERT INTO reservas VALUES (?, ?, ?, ?)"); // fecha, hora, pista, socio

			// Rellenamos las consultas:

			st_select.setDate(1, fecha);
			st_select.setInt(2, hora);

			rs = st_select.executeQuery();
			if (rs.next()){
				st_insert.setDate(1, fecha);
				st_insert.setInt(2, hora);
				st_insert.setInt(3, rs.getInt("nro"));
				st_insert.setString(4, socio);

				st_insert.executeUpdate();

				/*
				No es necesario contrlar esto, si no se realiza la el insert se lanza una excepción.
				if(st_select.executeUpdate() == 0){
					retorno = false;
				}
				*/
				conn.commit();
			}
			else{
				conn.rollback();
				retorno = false;
			}


		} catch (SQLException e) {
			//Faltan un par de cosas a rellenar por el alumno
			retorno = false;

			assert conn != null;
			conn.rollback();
			logger.error(e.getMessage());
			throw e;
		} finally {

			if (rs != null) rs.close();
			if (st_select != null) st_select.close();
			if (st_insert != null) st_insert.close();

			if (conn != null) conn.close();
		}

		return retorno;
	}

}
