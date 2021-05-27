package es.ubu.lsi.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.model.asociacion.Asociacion;
import es.ubu.lsi.model.asociacion.Conductor;
import es.ubu.lsi.model.asociacion.Incidencia;
import es.ubu.lsi.model.asociacion.TipoIncidenciaRanking;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.asociacion.IncidentError;
import es.ubu.lsi.service.asociacion.IncidentException;
import es.ubu.lsi.service.asociacion.Service;
import es.ubu.lsi.service.asociacion.ServiceImpl;
import es.ubu.lsi.test.util.ExecuteScript;
import es.ubu.lsi.test.util.PoolDeConexiones;

/**
 * Test client.
 * 
 * @author <a href="mailto:jmaudes@ubu.es">Jesús Maudes</a>
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @author <a href="mailto:mmabad@ubu.es">Mario Martínez</a>
 * @since 1.0
 */
public class TestClient {

	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(TestClient.class);

	/** Connection pool. */
	private static PoolDeConexiones pool;

	/** Path. */
	private static final String SCRIPT_PATH = "sql/";

	/** Simple date format. */
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	/**
	 * Main.
	 * 
	 * @param args arguments.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Iniciando...");
			init();
			System.out.println("Probando el servicio...");
			testService();
			System.out.println("FIN.............");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error grave en la aplicación {}", ex.getMessage());
		}
	}

	/**
	 * Init pool.
	 */
	static public void init() {
		try {
			// Acuerdate de q la primera vez tienes que crear el .bindings con:
			// PoolDeConexiones.reconfigurarPool();
			// Inicializacion de Pool
			pool = PoolDeConexiones.getInstance();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Create tables.
	 */
	static public void createTables() {
		ExecuteScript.run(SCRIPT_PATH + "script.sql");
	}

	/**
	 * Test service using JDBC and JPA.
	 */
	static void testService() throws Exception {
		createTables();
		Service implService = null;
		try {
			// JPA Service
			implService = new ServiceImpl();
			System.out.println("Framework y servicio iniciado...");
			
			// consultar el ranking inicial...
			consultarRanking(implService);
			
			// insertar nuevo tipo de incidencia
			insertarNuevoTipoIncidencia(implService);

			// insertar incidencia para el conductor 10000000A con 3 puntos de penalización
			insertarIncidenciaCorrecta(implService);

			// intenta insertar con tipo de incidencia que no existe
			insertarIncidenciaConTipoIncidenciaErroneo(implService);

			// indultar a Juana con nif 10000000C, borrando sus dos incidencias y con 12
			// puntos
			indultarConductorConDosIncidencias(implService);
			
			// intenta indultar a un conductor que no existe
			indultarAUnConductorQueNoExiste(implService);
			
			// consultar número de conductores con incidencias
			consultarNumeroConductoresConIncidencias(implService);

			// consultar número de conductores con incidencias en asoc que no existe
			consultarNumeroConductoresConIncidenciasEnAsocQueNoExiste(implService);

			// comprueba que la consulta de pistas carga todos los datos
			consultarAsociacionesUsandoGrafo(implService);
		} catch (Exception e) { // for testing code...
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			pool = null;
		}
	} // testClient

	/**
	 * Indulta a conductor con dos incidencias.
	 * 
	 * @param implService implementación del servicio
	 * @throws Exception error en test
	 */
	private static void indultarConductorConDosIncidencias(Service implService) throws Exception {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			System.out.print("Indulto del conductor...\n");
			implService.indultar("10000000C");

			con = pool.getConnection();
			// Comprobar si la incidencia se ha añadido
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM INCIDENCIA WHERE NIF='10000000C'");
			if (!rs.next()) {
				System.out.println("\tOK todas las incidencias borradas del conductor indultado");
			} else {
				System.out.println("\tERROR alguna incidencia no borrada del conductor indultado");
			}
			rs.close();
			rs = st.executeQuery("SELECT puntos FROM CONDUCTOR WHERE NIF='10000000C'");
			int puntos = -1;
			if (rs.next()) {
				puntos = rs.getInt(1);
			}
			if (puntos == Service.MAXIMO_PUNTOS) {
				System.out.println("\tOK puntos bien iniciados con indulto ");
			} else {
				System.out.println("\tERROR puntos mal iniciados con indulto, tiene " + puntos + " puntos");
			}
			rs.close();
			rs = st.executeQuery("SELECT count(*) FROM INCIDENCIA WHERE NIF<>'10000000C'");
			rs.next();
			int contador = rs.getInt(1);
			if (contador == 8) {
				System.out.println("\tOK el número de incidencias de otros conductores es correcto");
			} else {
				System.out.println("\tERROR el número de incidencias de otros conductores no es correcto");
			}
			con.commit();
		} catch (Exception ex) {
			logger.error("ERROR grave en test. " + ex.getLocalizedMessage());
			assert con != null;
			con.rollback();
			throw ex;
		} finally {
			cerrarRecursos(con, st, rs);
		}
	}
	
	/**
	 * Intenta indultar a un conductor que no existe.
	 * 
	 * @param implService servicio
	 */
	private static void indultarAUnConductorQueNoExiste(Service implService) {
		try {
			System.out.println("Indultar a un conductor que no existe");
			implService.indultar("00000000Z");
			System.out.println("\tERROR NO detecta que NO existe el conductor y finaliza la transacción");

		} catch (IncidentException ex) {
			if (ex.getError() == IncidentError.NOT_EXIST_DRIVER) {
				System.out.println("\tOK detecta correctamente que NO existe ese conductor");
			} else {
				System.out.println("\tERROR detecta un error diferente al esperado:  " + ex.getError().toString());
			}
		} catch (PersistenceException ex) {
			logger.error("ERROR en transacción de indultar con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error en indultos en asociaciones", ex);
		} catch(Exception ex) {
			logger.error("ERROR GRAVE de programación en transacción de indultar con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error grave en indultos en asociaciones", ex);
		}
	}

	/**
	 * Inserta una incidencia correcta.
	 * 
	 * @param implService implementación del servicio
	 * @throws Exception error en test
	 */
	private static void insertarIncidenciaCorrecta(Service implService) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			System.out.println("Insertar incidencia correcta");
			implService.insertarIncidencia(dateformat.parse("15/05/2019 16:00"), "10000000A", 3); // 3 es moderada con 3
																									// puntos
			// insertamos incidencia descontando 3 puntos al conductor 10000000A que tenía 9
			// inicialmente

			con = pool.getConnection();

			// Comprobar si la incidencia se ha añadido
			st = con.createStatement();
			rs = st.executeQuery("SELECT fecha||'-'||nif||'-'||idtipo FROM INCIDENCIA ORDER BY fecha, nif, idtipo");

			StringBuilder resultado = new StringBuilder();
			while (rs.next()) {
				resultado.append(rs.getString(1));
				resultado.append("\n");
			}
			logger.debug(resultado.toString());
			String cadenaEsperada =
			// @formatter:off
			"11/04/19 12:00:00,000000-10000000A-2\n" +
			"12/04/19 11:00:00,000000-10000000B-2\n" +
			"12/04/19 12:00:00,000000-10000000C-2\n" +
			"12/04/19 12:00:00,000000-20000000C-2\n" +
			"12/04/19 13:00:00,000000-10000000C-3\n" +
			"12/04/19 13:00:00,000000-20000000C-3\n" +
			"13/04/19 14:00:00,000000-30000000A-3\n" +			
			"13/04/19 15:00:00,000000-30000000B-2\n" +
			"13/04/19 16:00:00,000000-30000000C-1\n" +
			"15/05/19 16:00:00,000000-10000000A-3\n"; // nueva fila
			// @formatter:on
	
			if (cadenaEsperada.equals(resultado.toString())) {
				System.out.println("\tOK incidencia bien insertada");
			} else {
				System.out.println("\tERROR incidencia mal insertada");
			}
			rs.close();
			rs = st.executeQuery("SELECT puntos FROM conductor WHERE NIF='10000000A'");
			StringBuilder resultadoEsperadoPuntos = new StringBuilder();
			while (rs.next()) {
				resultadoEsperadoPuntos.append(rs.getString(1));
			}
			String puntosEsperados = "3"; // le deberíamos descontar 3 puntos quendado 6-3 = 3 puntos.
			if (puntosEsperados.equals(resultadoEsperadoPuntos.toString())) {
				System.out.println("\tOK actualiza bien los puntos del conductor");
			} else {
				System.out.println("\tERROR no descuenta bien los puntos de la incidencia del conductor");
			}
			con.commit();
		} catch (Exception ex) {
			logger.error("ERROR grave en test. " + ex.getLocalizedMessage());
			assert con != null;
			con.rollback();
			throw ex;
		} finally {
			cerrarRecursos(con, st, rs);
		}
	}

	/**
	 * Intenta insertar una incidencia cuyo tipo no existe.
	 * 
	 * @param implService servicio
	 */
	private static void insertarIncidenciaConTipoIncidenciaErroneo(Service implService) {
		try {
			System.out.println("Insertar incidencia con tipo erróneo");
			// fecha y usuario correcto
			implService.insertarIncidencia(dateformat.parse("15/05/2019 17:00"), "10000000A", 99); // 99 NO EXISTE
			System.out.println("\tERROR NO detecta que NO existe el tipo de incidencia y finaliza la transacción");

		} catch (IncidentException ex) {
			if (ex.getError() == IncidentError.NOT_EXIST_INCIDENT_TYPE) {
				System.out.println("\tOK detecta correctamente que NO existe ese tipo de incidencia");
			} else {
				System.out.println("\tERROR detecta un error diferente al esperado:  " + ex.getError().toString());
			}
		} catch (PersistenceException ex) {
			logger.error("ERROR en transacción de inserción de incidencia con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error en inserción de incidencia en asociaciones", ex);
		} catch(Exception ex) {
			logger.error("ERROR grave de programación en transacción de inserción de incicencia con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error grave en inserción de incidencia en asociaciones", ex);
		}
	}

	/**
	 * Prueba consulta de asociaciones, cargando datos completos desde un grafo de
	 * entidades.
	 * 
	 * @param implService implementación del servicio
	 */
	private static void consultarAsociacionesUsandoGrafo(Service implService) {
		try {
			System.out.println("Información completa con grafos de entidades...");
			List<Asociacion> asociaciones = implService.consultarAsociaciones();		
			for (Asociacion asociacion : asociaciones) {
				System.out.println(asociacion.toString());
				Set<Conductor> conductores = asociacion.getConductores();
				for (Conductor conductor : conductores) {
					System.out.println("\t" + conductor.toString());
					Set<Incidencia> incidencias = conductor.getIncidencias();
					for (Incidencia incidencia : incidencias) {
						System.out.println("\t\t" + incidencia.toString());
					}
				}
			}
			System.out.println("\tOK Sin excepciones en la consulta completa y acceso posterior");
		} catch (PersistenceException ex) {
			logger.error("\tERROR en transacción de consultas de asociaciones con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error en consulta de asociaciones", ex);
		}
	}


	/**
	 * Cierra recursos de la transacción.
	 * 
	 * @param con conexión
	 * @param st  sentencia
	 * @param rs  conjunto de datos
	 * @throws SQLException si se produce cualquier error SQL
	 */
	private static void cerrarRecursos(Connection con, Statement st, ResultSet rs) throws SQLException {
		if (rs != null && !rs.isClosed())
			rs.close();
		if (st != null && !st.isClosed())
			st.close();
		if (con != null && !con.isClosed())
			con.close();
	}
	
	
	/**
	 * Consulta el ranking de tipos de incidencia.
	 * 
	 * @param implService servicio
	 */
	private static void consultarRanking(Service implService) {
		try {
			// Ranking que se espera
			List<TipoIncidenciaRanking> tipoIncidenciaRankingEsperado = new ArrayList<>();
			tipoIncidenciaRankingEsperado.add(new TipoIncidenciaRanking("Muy grave", 1));
			tipoIncidenciaRankingEsperado.add(new TipoIncidenciaRanking("Moderada", 3));
			tipoIncidenciaRankingEsperado.add(new TipoIncidenciaRanking("Leve", 0));
			tipoIncidenciaRankingEsperado.add(new TipoIncidenciaRanking("Grave", 5));	


			List<TipoIncidenciaRanking> tipoIncidencias = null;
			System.out.println("Consulta de ranking de tipos de incidencia");
			tipoIncidencias = implService.consultarRanking();				

			if (tipoIncidencias.size() == 4) {
				System.out.println("\tOK número de tipo de incidencias correcto: 3");
				if (tipoIncidencias.equals(tipoIncidenciaRankingEsperado)) {
					System.out.println("\tOK coinciden los cuatro valores en el orden esperado.");
				} else {
					System.out.println(
							"\tERROR NO concide el ranking en valores o en orden, devuelve:\n " + tipoIncidencias.toString());
				}
			} else {
				System.out.println("\tERROR número de elementos en el ranking incorrecto: " + tipoIncidencias.size());
				System.out.println("\tERROR ha devuelto la siguiente colección: " + tipoIncidencias.toString());
			}
		} catch (PersistenceException ex) {
			logger.error("ERROR en transacción de consultas de asociaciones con JPA: " + ex.getLocalizedMessage());
			throw new RuntimeException("Error en consulta de asociaciones", ex);
		}
	}
	
	/**
	 * Insertar nuevo tipo de incidencia.
	 * 
	 * @param implService implementación del servicio
	 * @throws Exception error en test
	 */
	private static void insertarNuevoTipoIncidencia(Service implService) throws Exception {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			System.out.print("Insertar nuevo tipo de incidencia...\n");
			implService.insertarTipoIncidencia("Sin efecto", 0);

			con = pool.getConnection();
			// Comprobar si la incidencia se ha añadido
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM TIPOINCIDENCIA WHERE ID=5");
			int valor = -1;
			String descripcion = "";
			if (rs.next()) {		
				descripcion = rs.getString(2);
				valor = rs.getInt(3);
			}
			else {
				System.out.println("\tERROR no se encuentra fila con el identificador correcto.");
			}
			if (descripcion.equals("Sin efecto")) {
				System.out.println("\tOK valor correcto en descripcion ");
			} else {
				System.out.println("\tERROR valor mal inicializado en descripcion, tiene " + descripcion);
			}
			if (valor == 0) {
				System.out.println("\tOK valor correcto en valor");
			} else {
				System.out.println("\tERROR valor mal inicializado, tiene " + valor + " puntos");
			}
			con.commit();
		} catch (Exception ex) {
			logger.error("ERROR grave en test. " + ex.getLocalizedMessage());
			con.rollback();
			throw ex;
		} finally {
			cerrarRecursos(con, st, rs);
		}
	}
	
	/**
	 * Comprueba la consulta del número de conductores con incidencias.
	 * 
	 * @param implService servicio
	 * @throws Exception error en test 
	 */
	private static void consultarNumeroConductoresConIncidencias(Service implService) throws Exception {
		try {
			System.out.print("Consultar número de conductores con incidencias en asociación...\n");
			int contador = implService.consultarNumeroConductoresConIncidenciasEnAsoc("ABC");			
			if (contador == 4) {
				System.out.println("\tOK valor correcto de número de conductores con incidencias");
			}
			else {
				System.out.println("\tERROR contador devuelto incorrecto: " + contador);
			}
		} catch (Exception ex) {
			logger.error("ERROR grave en test. " + ex.getLocalizedMessage());
			throw ex;
		}
	}
	
	/**
	 * Consulta el número de conductores con incidencias en una asociación que NO existe.
	 * 
	 * @param implService servicio
	 * @throws Exception error en test
	 */
	private static void consultarNumeroConductoresConIncidenciasEnAsocQueNoExiste(Service implService) throws Exception {
		try {
			System.out.print("Consultar número de conductores con incidencias en asociación que no existe...\n");
			implService.consultarNumeroConductoresConIncidenciasEnAsoc("UUU");		
			System.out.println("\tERROR NO detecta que no existe la asociacion");
		} catch (IncidentException ex) {
			if (ex.getError() == IncidentError.NOT_EXIST_ASSOCIATION) {
				System.out.println("\tOK detectado que no existe la asociacion");		
			} else {
				System.out.println("\tERROR NO clasifica correctamente el tipo de error");
			}
		} catch (Exception ex) {
			System.out.println("\tERROR grave en transacción");
			logger.error("ERROR grave en test. " + ex.getLocalizedMessage());
			throw ex;
		}
	}
	

} // TestClient
