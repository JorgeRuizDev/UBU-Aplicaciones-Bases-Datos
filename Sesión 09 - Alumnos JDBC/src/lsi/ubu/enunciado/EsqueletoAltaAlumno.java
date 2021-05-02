package lsi.ubu.enunciado;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsi.ubu.util.ExecuteScript;
import lsi.ubu.util.PoolDeConexiones;

/**
 * AltaAlumno:
 * Implementa el alta de un alumno en un grupo de una asignatura
 * segun PDF de la carpeta enunciado
 *
 * @author <a href="mailto:jmaudes@ubu.es">Jesus Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raul Marticorena</a>
 * @version 1.0
 * @since 1.0
 */
public class EsqueletoAltaAlumno {

	private static final Logger logger = LoggerFactory.getLogger(EsqueletoAltaAlumno.class);

	private static final String script_path = "sql/";


	public static void main(String[] args) throws SQLException {

		ExecuteScript.run(script_path + "AltaAlumno.sql");

		PoolDeConexiones p = PoolDeConexiones.getInstance();

		Connection con = null;
		ResultSet rs = null;
		Statement st = null;

		//public static void matricular(String p_alumno, String p_asig, int p_grupo) throws SQLExceptionProblema6{

		try {
			//Matriculamos en el grupo lleno			
			try {
				matricular("Julian", "OFIM", 1); //sale mal porque el grupo esta lleno
				System.out.println("NO se da cuenta de que el grupo esta lleno MAL");
			} catch (AltaAlumnoException e) {
				if (e.getErrorCode() == AltaAlumnoException.SIN_PLAZAS) {
					System.out.println("Se da cuenta de que el grupo esta lleno OK");
				}
			}

			try {
				matricular("Pedro", "OFIM", 3); //sale mal porque no existe el grupo
				System.out.println("NO se da cuenta de que el grupo no existe MAL");
			} catch (AltaAlumnoException e) {
				if (e.getErrorCode() == AltaAlumnoException.NO_EXISTE_ASIG_O_GRUPO) {
					System.out.println("Se da cuenta de que el grupo no existe OK");
				}
			}

			try {
				matricular("Pedro", "ALGEBRA", 1); //sale mal porque no existe la asignatura
				System.out.println("NO se da cuenta de que la asignatura no existe MAL");
			} catch (AltaAlumnoException e) {
				if (e.getErrorCode() == AltaAlumnoException.NO_EXISTE_ASIG_O_GRUPO) {
					System.out.println("Se da cuenta de que la asignatura no existe OK");
				}
			}

			con = p.getConnection();
			matricular("Pedro", "FPROG", 1);

			st = con.createStatement();
			rs = st.executeQuery("select idgrupo||grupos.asignatura||plazaslibres||idmatricula||alumno" +
					" from grupos left join matriculas on(matriculas.grupo=grupos.idGrupo)");
			String resultado = "";
			while (rs.next()) {
				resultado += rs.getString(1);
			}
			//System.out.println(resultado);
			String teniaQueDar = "1OFIM01PEPE1FPROG31PEPE1OFIM02ANA1FPROG32ANA1OFIM03JUAN1FPROG33JUAN1OFIM04LUIS1FPROG34LUIS2OFIM15ANTONIO2FPROG45ANTONIO2OFIM16MERCEDES2FPROG46MERCEDES2OFIM17JESUS2FPROG47JESUS1OFIM011Pedro1FPROG311Pedro10OFIM4";

			if (resultado.equals(teniaQueDar)) {
				System.out.println("Matricula OK");
			} else {
				System.out.println("Matricula MAL");
			}


		} catch (SQLException e) {
			if (con != null) con.rollback();
			logger.error(e.getMessage());
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}


		System.out.println("FIN.............");
	}


	public static void matricular(String arg_alumno, String arg_asig, int arg_grupo) throws SQLException {
		PoolDeConexiones pool = PoolDeConexiones.getInstance();


		try (
				Connection conn = pool.getConnection();

				PreparedStatement fetchAsignatura = conn.prepareStatement("" +
								"        select IDGRUPO, ASIGNATURA, PLAZASLIBRES" +
								"        from grupos" +
								"        where asignatura = ?" +
								"          and idGrupo = ?",
						ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);


				PreparedStatement matriculaAlumno = conn.prepareStatement("" +
						"   INSERT INTO matriculas" +
						"    VALUES (seq_matricula.nextval, ?, ?, ?)")

		) {
			fetchAsignatura.setString(1, arg_asig);
			fetchAsignatura.setInt(2, arg_grupo);
			ResultSet resAsignatura = fetchAsignatura.executeQuery();
			if (resAsignatura.next()) {
				if (resAsignatura.getInt("PLAZASLIBRES") < 1)
					throw new AltaAlumnoException(2);

			} else
				throw new AltaAlumnoException(1);

			matriculaAlumno.setString(1, arg_alumno);
			matriculaAlumno.setString(2, arg_asig);
			matriculaAlumno.setInt(3, arg_grupo);

			if (matriculaAlumno.executeUpdate() != 1) {
				throw new AltaAlumnoException(1);
			}

			// Actualizamos el número de plazas libres:
			//resAsignatura.updateInt(3, resAsignatura.getInt(3) - 1);

			logger.error("No Petó");

			conn.commit();
		} catch (AltaAlumnoException e) {
			// Rollback
			throw new AltaAlumnoException(e.getErrorCode());
		} catch (SQLException e) {
			// Rollback
			e.printStackTrace();
		}

	}


}
