package es.ubu.lsi.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.model.invoice.Factura;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.invoice.InvoiceError;
import es.ubu.lsi.service.invoice.InvoiceException;
import es.ubu.lsi.service.invoice.Service;
import es.ubu.lsi.service.invoice.ServiceImpl;
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
public class TestClient2 {

	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(TestClient.class);

	/** Connection pool. */
	private static PoolDeConexiones pool;

	/** Path. */
	private static final String SCRIPT_PATH = "sql/";

	/**
	 * Main.
	 * 
	 * @param args
	 *            arguments.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Iniciando...");
			init();
			System.out.println("Probando el servicio...");
			testService();
			System.out.println("FIN.............");
		}
		catch(Exception ex) {
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

		Connection connectionTest = null;
		ResultSet rs = null;
		Statement st = null;

		Service implService = null;

		try {
			// JPA Service
			implService = new ServiceImpl();
			System.out.println("Framework y servicio iniciado");
			
			implService.borrarLinea(2, 1); // borrar linea 2 de la factura 1
			System.out.println("Transacción de borrado realizada");

			// Begin first test...
			
			connectionTest = pool.getConnection();

			// Comprobar si el join de facturas con lineas tiene todos los datos
			// menos la linea borrada, y ademas el importe esta restado
			st = connectionTest.createStatement();
			rs = st.executeQuery("SELECT f.nro||cliente||fecha||total||linea||descripcion||unidades||importe "
					+ "FROM facturas f INNER JOIN lineasFactura lf ON f.nro=lf.nro");

			StringBuilder resultado = new StringBuilder();
			while (rs.next()) {
				resultado.append(rs.getString(1));
			}

			logger.debug(resultado.toString());
			String cadenaEsperada = "1Pepe03/04/13101Articulo15102Ana03/02/13251Articulo15102Ana03/02/13252Articulo21515";
			logger.debug(cadenaEsperada);

			if (cadenaEsperada.equals(resultado.toString())) {
				System.out.println("OK Borrado");
			} else {
				System.out.println("ERROR Borrado");
			}

			// Comprobamos lanza excepcion

			// Begin second test...
			try {
				implService.borrarLinea(2, 1); // borrar linea 2 de la factura 1
												// otra vez
				System.out.println("ERROR NO se da cuenta de que no existe la linea");
			} catch (InvoiceException e) {
				if (e.getError() == InvoiceError.NOT_EXIST_INVOICE_LINE) {
					System.out.println("OK Se da cuenta de que no existe la linea de factura");
				} else {
					System.err.println("ERROR No ha clasificado correctamente el error, no existe la linea de factura");
				}
			} // catch

			// Third test ... empty set of unbalanced invoices
			try {
				List<Factura> facturasDesequilibradas = implService.consultarFacturasDesequilibradas();
				if (facturasDesequilibradas.isEmpty()) {
					System.out.println("OK No hay facturas desequilibradas");
				}
				else {
					System.out.println("ERROR Detecta " + facturasDesequilibradas.size() + " facturas desequilibradas");
				}
			} catch (Exception e) {
				System.err.println("ERROR Se ha generado alguna excepción en el recuento");
			} // catch
			connectionTest.commit();
		} catch (SQLException /*| PersistenceException*/ e) { // for testing code...
			connectionTest.rollback();
			logger.error(e.getMessage());		
		} finally {
			if (rs!=null && !rs.isClosed()) rs.close();
			if (st!=null && !st.isClosed()) st.close();
			if (connectionTest!=null && !connectionTest.isClosed()) connectionTest.close();			
			pool = null;
		} // finally
	} // testClient

} // TestClient
