package lsi.ubu;

import java.sql.SQLException;

public class OracleTableError extends TableError {

	//Subir Nota
	/**
	 * M�todo que compara una excepci�n con su c�digo de error gen�rico
	 * @param ex Excepci�n SQL
	 * @param error C�digo de error de TableError
	 * @return true si son equivalentes
	 */
	@Override
	public boolean checkExceptionToCode(SQLException ex, int error) {
		return translate(ex.getErrorCode()) == error;
	}

	/**
	 * M�todo que permite traducir uan excepci�n de Oracle a una excepci�n
	 * gen�rica de TableError.
	 * @param errorSGBD: C�digo de error Oracle.
	 * @return Un valor ocrrespondiente al enumerado TableError.
	 */
	@Override
	public int translate(int errorSGBD) {
		switch (errorSGBD) {
			case 1: //ORA-00001: unique constraint (HR.SYS_C007861) violated
				return UNQ_VIOLATED;
			case 2291: //ORA-02291: integrity constraint (la que sea) violated - parent key not found
				return FK_VIOLATED;
			case 2292: //ORA-02292: integrity constraint (la que sea) violated - child record found
				return FK_VIOLATED_DELETE;
			case 2437:
				return PK_VIOLATED;
		}
		return -1;
	}

} 