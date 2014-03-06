import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.Console;

public class CreatePopulate {
	static Connection conn = null;

	public static void main(String[] argv) {
	/*TODO only in for testing purposes */
		String driver = "oracle.jdbc.driver.OracleDriver";
		String database ="jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
		String username;
		String password;
		char[] pass;

		// Obtains database username and password
		if (argv.length != 1){
			System.out.println("Enter oracle username");
			System.exit(0);
		}
		username = argv[0];
		Console console = System.console();
		pass = console.readPassword("Enter your password: ");
		password = new String(pass);

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

	/*TODO end of testing insert*/

	//String createSQL; 
	//String popSQL;

	// Drop previous tables
	try {
		Statement dropTables = conn.createStatement();
		String dropSQL = "DROP TABLE auto_sale";
	    	//"DROP TABLE owner;" +
	    	//"DROP TABLE restriction;" +
	    	//"DROP TABLE driving_condition;" +
	    	//"DROP TABLE ticket;" +
	    	//"DROP TABLE ticket_type;" +
	    	//"DROP TABLE vehicle;" + 
	    	//"DROP TABLE vehicle_type;" + 
	    	//"DROP TABLE drive_licence;" +
	    	//"DROP TABLE people;";

	dropTables.executeUpdate(dropSQL);
	} catch (SQLException e) {
		System.out.println(e.getMessage());
		return;
	}

	// Create tables
	//Statement createTables = con.createStatement();

	// Populate tables
	//Statement populateTables = con.createStatement();
	}	
}
