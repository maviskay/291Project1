import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.Console;

// export CLASSPATH=$CLASSPATH\:.\:/oracle/jdbc/lib/classes12.zip
// TODO: make sure casing of string doesn't matter

public class database {
	static Connection conn = null;

	public static void main(String[] argv) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String database = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
		String username;
		String password;
		char[] pass;

		// Obtains database username and password
		if (argv.length != 1) {
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
		try {
			conn = DriverManager.getConnection(database, username, password);
		} catch (SQLException e) {
			System.out.println("Connection to Oracle failed");
			return;
		}
		if (conn != null){
			String caseInsensitive = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
			String caseInsensitive2 = "ALTER SESSION SET NLS_SORT=BINARY_CI";
			try {
				Statement statement = conn.createStatement();
				statement.executeQuery(caseInsensitive);
				statement.executeQuery(caseInsensitive2);
				requestAction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Requests selection from user & ensures valid request
	private static void requestAction() {
		Scanner keyboard;
		int selection = -1;

		System.out.println("\nWelcome to the auto registration system");
		while (true) {
			System.out.println("\nPlease select from the following:");
			System.out.println("\t1: New Vehicle Registration");
			System.out.println("\t2: Auto Transaction");
			System.out.println("\t3: Driver License Registration");
			System.out.println("\t4: Violation Record");
			System.out.println("\t5: Search Engine");
			System.out.println("\t6: Exit");

			// Requests selection from user
			keyboard = new Scanner(System.in);
			try {
				selection = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid option");
				continue;
			}
			// Searches for user's selection
			if (selection == 1) {
				System.out
						.println("You have selected new vehicle registration");
				NewVehicle.vehicleRegistration(conn);
			} else if (selection == 2) {
				System.out.println("You have selected auto transaction");
				VehicleTransaction.autoTransaction(conn);
			} else if (selection == 3) {
				System.out
						.println("You have selected driver licence registration");
				NewLicence.licenceRegistration(conn, "-1", "-1");
			} else if (selection == 4) {
				System.out.println("You have selected violation record");
				Violation.violationRecord(conn);
			} else if (selection == 5) {
				System.out.println("You have selected search engine");
				Search.searchEngine(conn);
			} else if (selection == 6) {
				System.out.println("Connection to database will now close");
				try {
					conn.close();
					System.exit(0);
				} catch (SQLException e) {
					System.out.println("Could not close database connection");
					return;
				}
			} else
				System.out.println("Selection not valid, please try again");
		}
	}

	// Request a yes or no answer from user
	public static String requestYorN() {
		Scanner keyboard;
		String input = null;

		while (true) {
			keyboard = new Scanner(System.in);
			input = keyboard.nextLine();
			if (input.length() != 1) {
				System.out.print("Input invalid, please try again: ");
			} else if (input.equalsIgnoreCase("y")
					|| input.equalsIgnoreCase("n")) {
				return input;
			} else {
				System.out.print("Input invalid, please try again: ");
			}
		}
	}

	// Requests string from user
	public static String requestString(int maxLength, int notVarChar, int nonNum) {
		Scanner keyboard;
		String input = null;
		int padding;

		while (true) {
			keyboard = new Scanner(System.in);
			input = keyboard.nextLine();
			if (input.length() > maxLength) {
				System.out.print("Input invalid, please try again: ");
			} else if (nonNum == 1 && input.matches(".*\\d+.*")) {
				// String contains numbers
				System.out.print("Input invalid, please try again: ");
			} else
				break;
		}
		if (notVarChar == 1) {
			// String must have a certain length
			padding = maxLength - input.length();
			for (int i = 0; i < padding; i++)
				input += " ";
			return input;
		} else {
			return input;
		}
	}

	// Requests int from user
	public static Double requestInt(int before, int after) {
		Scanner keyboard;
		String numberString[];
		Double number = null;

		while (true) {
			keyboard = new Scanner(System.in);
			try {
				number = keyboard.nextDouble();
				numberString = number.toString().split("\\.");
				if (numberString[0].length() > before || numberString[1].length() > after || numberString[0].length() <= 0) {
					System.out.print("Input invalid, please try again: ");
				} else
					return number;
			} catch (InputMismatchException e) {
				System.out.print("Input invalid, please try again: ");
			}
		}
	}

	public static java.util.Date requestDate() {
		String date;
		Scanner keyboard;
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date inputDate = new java.util.Date();

		while (true) {
			keyboard = new Scanner(System.in);
			date = keyboard.nextLine();
			try {
				formatDate.setLenient(false);
				inputDate = formatDate.parse(date);
				return inputDate;
			} catch (ParseException e) {
				System.out.print("Date not valid, please try again: ");
				continue;
			}
		}
	}

	// Checks if database already contains this entry
	public static int checkExistence(Connection dbConn, String query,
			String parameter) {
		PreparedStatement checkResult;
		ResultSet resultCount;

		try {
			checkResult = dbConn.prepareStatement(query);
			checkResult.setString(1, parameter);
			resultCount = checkResult.executeQuery();
			resultCount.next();
			if (resultCount.getInt(1) != 0) {
				// Database already contains this parameter
				resultCount.close();
				return 1;
			} else {
				resultCount.close();
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Should never reach here
		return -1;
	}

	// Checks if database already contains this entry
	public static int checkExistence(Connection dbConn, String query,
			int parameter) {
		PreparedStatement checkResult;
		ResultSet resultCount;

		try {
			checkResult = dbConn.prepareStatement(query);
			checkResult.setInt(1, parameter);
			resultCount = checkResult.executeQuery();
			resultCount.next();
			if (resultCount.getInt(1) != 0) {
				// Database already contains this parameter
				resultCount.close();
				return 1;
			} else {
				resultCount.close();
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Should never reach here
		return -1;
	}

	// Checks if database already contains this entry
	public static int checkExistence(Connection dbConn, String query,
			String parameter, String parameter2) {
		PreparedStatement checkResult;
		ResultSet resultCount;

		try {
			checkResult = dbConn.prepareStatement(query);
			checkResult.setString(1, parameter);
			checkResult.setString(2, parameter2);
			resultCount = checkResult.executeQuery();
			resultCount.next();
			if (resultCount.getInt(1) != 0) {
				// Database already contains this parameter
				resultCount.close();
				return 1;
			} else {
				resultCount.close();
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Should never reach here
		return -1;
	}

	// Remove person from owner table
	public static void removeOwner(Connection dbConn, String query,
			String parameter, String parameter2) {
		PreparedStatement deleteOwner;

		try {
			deleteOwner = dbConn.prepareStatement(query);
			deleteOwner.setString(1, parameter);
			deleteOwner.setString(2, parameter2);
			deleteOwner.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Finds the next id for that table
	public static int getNextID(Connection dbConn, String query) {
		PreparedStatement checkCount;
		ResultSet count;

		try {
			checkCount = dbConn.prepareStatement(query);
			count = checkCount.executeQuery();
			count.next();
			return count.getInt(1) + 1;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Should not reach here
		return -1;
	}
}


