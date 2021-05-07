package lsi.ubu;

import java.sql.SQLException;

public class OracleTableError extends TableError {
	@Override
	public boolean checkExceptionToCode(SQLException ex, int error) {
		// return new
		// OracleTableError().translate(e.getErrorCode())==TableError.FK_VIOLATED
		return translate(ex.getErrorCode()) == error;
	}

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