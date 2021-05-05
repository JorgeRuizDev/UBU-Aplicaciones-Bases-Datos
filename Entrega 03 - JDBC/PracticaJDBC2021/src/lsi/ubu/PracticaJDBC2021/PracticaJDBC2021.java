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

	private static PoolDeConexiones p = null;
	private static Logger l = null;

	private static final String script_path = "sql/";
    
    public static void main(String [] argv) {
    	inicializaciones();
		pruebaAltaAutorizado();

		System.out.println("FIN.............");
	}
    
    static void pruebaAltaAutorizado() {
		
		creaTablas();
		
		//Pruebo a dar de alta un autorizado que no existe
		//Doy de alta en la cuenta 1 al cliente con DNI 44444 que no existe
	    try{        	
	    	altaAutorizado(1, 44444);
	    	System.out.println("Cliente inexistente MAL");
	     } catch (SQLException_PracticaJDBC2021 e) {
			if (e.getErrorCode() == SQLException_PracticaJDBC2021.NO_EXISTE_CLIENTE)
				System.out.println("Cliente inexistente OK");
			else
				System.out.println("Cliente inexistente MAL");
		}
	    System.out.println("--------------------------------");  
	    
	  //Pruebo a dar de alta un autorizado en una cuenta que no existe
	  //cuenta=2 y el dni=12345678  
	    try{        	
	    	altaAutorizado(2, 12345678);
	    	System.out.println("Cuenta inexistente MAL");
	     } catch (SQLException_PracticaJDBC2021 e) {
			if (e.getErrorCode() == SQLException_PracticaJDBC2021.NO_EXISTE_CTA)
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
			
			if (rs.next()){
				System.out.println("Caso todo correcto OK");
			}
			else{
				System.out.println("Caso todo correcto  MAL");
			}
					
		} catch (SQLException e) {
			l.error(e.getMessage());
		} finally {
			
			
		}
		System.out.println("--------------------------------");
		//Pruebo que ese cliente ya exist�a con esa cuenta
		//volviendo a insertar altaAutorizado(1, 12345678);
		try{        	
	    	altaAutorizado(1, 12345678);
	    	System.out.println("Autorizaci�n repetida MAL");
	     } catch (SQLException_PracticaJDBC2021 e) {
			if (e.getErrorCode() == SQLException_PracticaJDBC2021.AUTORIZACION_REPETIDA)
				System.out.println("Autorizaci�n repetida OK");			
			else
				System.out.println("Autorizaci�n repetida MAL");
		}
	    
	}

	public static void altaAutorizado( int cta, int dni) throws SQLException_PracticaJDBC2021{
    	
		
		
    	//A completar por el alumno
    	
		
		
    } 
	public static boolean checkExceptionToCode(SQLException ex, int error) {
		// return new
		// OracleTableError().translate(e.getErrorCode())==TableError.FK_VIOLATED
		return new OracleTableError().translate(ex.getErrorCode()) == error;
	}
    
    static public void creaTablas() {
		ExecuteScript.run(script_path+ "script_PracticaJDBC2021.sql");
	}
        
	static public void inicializaciones() {
		try {
			//Acuerdate de q la primera vez tienes que crear el .bindings con:
			// PoolDeConexiones.reconfigurarPool();
			
			// Inicializacion de Pool
			p = PoolDeConexiones.getInstance();
			
			// Inicializacion del Logger
			l =	LoggerFactory.getLogger(PracticaJDBC2021.class);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e.getStackTrace());
		}
	}
}  


