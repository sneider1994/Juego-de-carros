package pruebaEjercicio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
public static Connection getConnection() throws SQLException{
		
		String url = "jdbc:mysql://localhost:3306/carreracarros";
		String user = "root";
		String password = "";
		return DriverManager.getConnection(url, user, password);

	
	}
	

}
