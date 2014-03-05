import java.sql.*;
import java.util.Scanner;
import java.io.Console;

// export CLASSPATH=$CLASSPATH\:.\:/oracle/jdbc/lib/classes12.zip

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
		int selection;
		
		//Requests selection from user
		System.out.println("\nWelcome to the auto registration system");
		while(true){
			System.out.println("\nPlease select from the following:");
			System.out.println("\t1: New Vehicle Registration");
			System.out.println("\t2: Auto Transaction");
			System.out.println("\t3: Driver License Registration");
			System.out.println("\t4: Violation Record");
			System.out.println("\t5: Search Engine");
			System.out.println("\t6: Exit");
			keyboard = new Scanner(System.in);
			// TODO: check if non numeric value is entered
			selection = keyboard.nextInt();
			if (selection == 1)
				vehicleRegistration();
			else if (selection == 2)
				autoTransaction();
			else if (selection == 3)
				licenseRegistration();
			else if (selection == 4)
				violationRecord();
			else if (selection == 5)
				searchEngine();
			else if (selection == 6){
				System.out.println("Connection to database will now close.");
				try {
					conn.close();
					System.exit(0);
				} catch (SQLException e) {
					System.out.println("Could not close database connection");
				}
			}
			else
				System.out.println("Selection not valid, please try again");
		}
		
	}

	// Registers new vehicle to database
	private static void vehicleRegistration() {
		Scanner keyboard;
		String serialNum, maker, model, color;
		int year, type;
		System.out.println("You have selected new vehicle registration");
		while (true) {
		    System.out.print("Please enter the vehicle's serial number: ");
			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() != 15)
				System.out.println("Serial number invalid");
			else
			    // TODO: Check serial num before next input request
				break;
		}
		while (true) {
			System.out.print("Please enter make of vehicle: ");
		        keyboard = new Scanner(System.in);
			maker = keyboard.nextLine();
			if (maker.length() > 20)
				System.out.println("Make of vehicle invalid");
			else
			    break;
		}
		while (true) {
		    System.out.print("Please enter model of vehicle: ");
		    keyboard = new Scanner(System.in);
		    model = keyboard.nextLine();
		    if (model.length() > 20)
			System.out.println("Model of vehicle invalid");
		    else
			break;
		}
		while (true) {
		    System.out.print("Please enter year of vehicle: ");
		    keyboard = new Scanner(System.in);
		    year = keyboard.nextInt();
		}
	}
	
	// Complete an auto transaction in database
	private static void autoTransaction() {
		System.out.println("You have selected auto transaction");
	}
	
	// Registers new driving license to database
	private static void licenseRegistration() {
		System.out.println("You have selected driver license registration");
	}
	
	// Issue ticket & record violation in database
	private static void violationRecord() {
		System.out.println("You have selected violation record");
	}
	
	// Searches the database
	private static void searchEngine() {
		System.out.println("You have selected search engine");
	}
}

