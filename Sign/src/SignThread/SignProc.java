package SignThread;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SignProc {
	
	public static int getResultSetSize(ResultSet resultSet) {
		int size = -1;

		try {
			resultSet.last();
			size = resultSet.getRow();
			resultSet.beforeFirst();
		} catch (SQLException e) {
			return size;
		}

		return size;
	}
}
