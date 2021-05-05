package lsi.ubu;

public class OracleTableError extends TableError {
	
	@Override
	public int translate( int errorSGBD){
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