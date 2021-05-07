package lsi.ubu;

import java.sql.SQLException;

public abstract class TableError {
	public static final int FK_VIOLATED = 1;
	public static final int PK_VIOLATED = 2;
	public static final int FK_VIOLATED_DELETE = 3;
	public static final int UNQ_VIOLATED = 3;
	//Seguir añadiendo...

	public abstract int translate(int errorSGBD);

	public abstract boolean checkExceptionToCode(SQLException ex, int exceptionCode);
} // enum