import java.sql.*;

public class database {

	public static void main(String[] argv) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String database ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
		String username;
		String password;
		Connection conn = null;

		System.out.println("CMPUT 291 Project 1");
		// Obtains database username and password
		if (argv.length != 2){
			System.out.println("Enter oracle username followed by password");
			System.exit(0);
		}
		username = argv[0];
		password = argv[1];
		
		// Attempts to connect to Oracle database
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC Driver not found");
			return;
		}
		try{
			conn = DriverManager.getConnection(database, username, password);
		} catch (SQLException e) {
			System.out.println("Connection to Oracle failed");
			return;
		}
		if (conn != null)
			System.out.println("Connection established");
	}
}
