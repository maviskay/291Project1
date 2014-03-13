import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.Console;

// export CLASSPATH=$CLASSPATH\:.\:/oracle/jdbc/lib/classes12.zip
// TODO: make sure casing of string doesn't matter
// TODO: check that certain strings only contain alphabets eg: eye color

public class database {
	static Connection conn = null;

	public static void main(String[] argv) {
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
		if (conn != null)
			requestAction();
	}

	// Requests selection from user & ensures valid request
	private static void requestAction() {
		Scanner keyboard;
		int selection = -1;

		System.out.println("\nWelcome to the auto registration system");
		while(true){
			System.out.println("\nPlease select from the following:");
			System.out.println("\t1: New Vehicle Registration");
			System.out.println("\t2: Auto Transaction");
			System.out.println("\t3: Driver License Registration");
			System.out.println("\t4: Violation Record");
			System.out.println("\t5: Search Engine");
			System.out.println("\t6: Exit");
			
			// Requests selection from user
			keyboard = new Scanner(System.in);
			try{
			    selection = keyboard.nextInt();
			} catch (InputMismatchException e) {
			    System.out.println("Invalid option");
			    continue;
			}
			// Searches for user's selection
			if (selection == 1){
				System.out.println("You have selected new vehicle registration");
				NewVehicle.vehicleRegistration(conn);
			} else if (selection == 2){
				System.out.println("You have selected auto transaction");
				VehicleTransaction.autoTransaction(conn);
			} else if (selection == 3){
				System.out.println("You have selected driver licence registration");
				NewLicence.licenceRegistration(conn);
			} else if (selection == 4){
				System.out.println("You have selected violation record");
				Violation.violationRecord(conn);
			} else if (selection == 5){
				System.out.println("You have selected search engine");
				Search.searchEngine(conn);
			} else if (selection == 6){
				System.out.println("Connection to database will now close");
				try {
					conn.close();
					System.exit(0);
				} catch (SQLException e) {
					System.out.println("Could not close database connection");
					return;
				}
			}
			else
				System.out.println("Selection not valid, please try again");
		}
	}
}
