import java.sql.*;
import java.util.Scanner;
import java.io.Console;
import java.util.InputMismatchException;

public class Search {

	public static void searchEngine(Connection dbConn) {
		Scanner keyboard;
		int selection;

		// Prompt the user to input which search they want
		while (true) {
			System.out
					.println("Please select from the following search options:");
			System.out.println("\t1: Driver info by name");
			System.out.println("\t2: Driver info by licence number");
			System.out.println("\t3: Violation info by driver sin");
			System.out.println("\t4: Violation into by licence number");
			System.out.println("\t5: Vehicle history by serial number");

			keyboard = new Scanner(System.in);
			try {
				selection = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid option");
				continue;
			}

			if (selection == 1) {
				System.out.println("You have selected driver info by name");
				selectDriver(dbConn, selection);
				break;
			} else if (selection == 2) {
				System.out
						.println("You have selected driver info by licence number");
				selectDriver(dbConn, selection);
				break;
			} else if (selection == 3) {
				System.out.println("You have selected violation info by sin");
				selectViolation(dbConn, selection);
				break;
			} else if (selection == 4) {
				System.out
						.println("You have selected violation info by licence number");
				selectViolation(dbConn, selection);
				break;
			} else if (selection == 5) {
				System.out
						.println("You have selected vehicle history by serial number");
				selectHistory(dbConn);
				break;
			} else {
				System.out.println("Invalid option");
			}
		}
	}

	public static ResultSet searchDatabase(Connection dbConn, String query,
			String parameter) {
		PreparedStatement searchDB;
		ResultSet results;

		try {
			searchDB = dbConn.prepareStatement(query);
			searchDB.setString(1, parameter);
			results = searchDB.executeQuery();
			return results;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Should not reach here
		return null;
	}

	// Search for driver
	public static void selectDriver(Connection dbConn, int selection) {
		ResultSet driver = null;
		String queryCheckName = "SELECT COUNT(name) FROM people WHERE name = ?";
		String queryCheckLicence = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String queryDriverByName = "SELECT p.name, l.licence_no, p.addr, p.birthday, l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND p.name = ?";
		String queryDriverByLNo = "SELECT p.name, l.licence_no, p.addr, p.birthday,l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND l.licence_no = ?";
		String name = null, licence = null;
		int maxString = 1, varString = 0, noNum = 1, incNum = 0, exists;

		while (true) {
			if (selection == 1) {
				// Search for driver by name
				System.out.print("Please enter name of person: ");
				name = database.requestString(40, varString, noNum);
				exists = database.checkExistence(dbConn, queryCheckName, name);
				if (exists == 0) {
					System.out.println("Person does not exist");
				} else {
					break;
				}
			} else if (selection == 2) {
				// Search for driver by licence number
				System.out.print("Please enter licence number of person: ");
				licence = database.requestString(15, maxString, incNum);
				exists = database.checkExistence(dbConn, queryCheckLicence,
						licence);
				if (exists == 0) {
					System.out.println("Licence does not exist");
				} else {
					break;
				}
			}
		}
		if (selection == 1) {
			driver = searchDatabase(dbConn, queryDriverByName, name);
		} else if (selection == 2) {
			driver = searchDatabase(dbConn, queryDriverByLNo, licence);
		}
		try {
			while (driver.next()) {
				System.out.println("Name: " + driver.getString("name"));
				System.out.println("Licence # : "
						+ driver.getString("licence_no"));
				System.out.println("Address: " + driver.getString("addr"));
				System.out.println("Birthday: " + driver.getString("birthday"));
				System.out.println("Driving class: "
						+ driver.getString("class"));
				System.out.println("Driving condition: "
						+ driver.getString("r_rid"));
				System.out.println("Licence expiring date: "
						+ driver.getString("l.expiring_date"));
				System.out.println("\n");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Search for violation
	public static void selectViolation(Connection dbConn, int selection) {
		ResultSet violation = null;
		String queryCheckSin = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryCheckLicence = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String queryTicketsBySin = "SLEECT * FROM ticket WHERE violator_no = ?";
		String queryTicketsByLNo = "SLEECT t.* FROM ticket t, drive_licence l WHERE l.sin = t.violator_no AND l.licence_no = ?";
		int maxString = 1, incNum = 0, exists;
		String sin = null, licence = null;

		while (true) {
			if (selection == 1) {
				// Search for violation by sin
				System.out.print("Please enter sin number of person: ");
				sin = database.requestString(15, maxString, incNum);
				exists = database.checkExistence(dbConn, queryCheckSin, sin);
				if (exists == 0) {
					System.out.println("Sin number does not exist");
				} else {
					break;
				}
			} else if (selection == 2) {
				// Search for violation by licence number
				System.out.print("Please enter licence number of person: ");
				licence = database.requestString(15, maxString, incNum);
				exists = database.checkExistence(dbConn, queryCheckLicence,
						licence);
				if (exists == 0) {
					System.out.println("Licence does not exist");
				} else {
					break;
				}
			}
		}
		if (selection == 1) {
			violation = searchDatabase(dbConn, queryTicketsBySin, sin);
		} else if (selection == 2) {
			violation = searchDatabase(dbConn, queryTicketsByLNo, licence);
		}
		try {
			while (violation.next()) {
				System.out.println("Ticket #: "
						+ violation.getString("ticket_no"));
				System.out.println("Violator: "
						+ violation.getString("violator_no"));
				System.out.println("Vehicle: "
						+ violation.getString("vehicle_id"));
				System.out.println("Officer: "
						+ violation.getString("office_no"));
				System.out.println("Violation type: "
						+ violation.getString("vtype"));
				System.out.println("Violation date: "
						+ violation.getString("vdate"));
				System.out.println("Location: " + violation.getString("place"));
				System.out.println("Comments: "
						+ violation.getString("descriptions"));
				System.out.println("\n");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Search for vehicle history
	public static void selectHistory(Connection dbConn) {
		PreparedStatement searchHistory;
		ResultSet history;
		String queryCheckVehicle = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String queryVehicleHist = "SELECT v.serial_no, COUNT(DISTINCT s.transaction_id) AS transCount, AVG(s.price) AS avgPrice, COUNT(DISTINCT t.ticket_no) AS ticCount FROM vehicle v, auto_sale s, ticket t WHERE s.vehicle_id (+)= ? AND t.vehicle_id (+)= ? GROUP BY ?";
		String serialNum;
		int maxString = 1, incNum = 0, exists;
		
		while (true) {
			System.out.print("Please enter serial number of vehicle: ");
			serialNum = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryCheckVehicle, serialNum);
			if (exists == 0) {
				System.out.println("Vehicle does not exist");
			} else {
				break;
			}
		}
		
		try {
			searchHistory = dbConn.prepareStatement(queryVehicleHist);
			searchHistory.setString(1, serialNum);
			searchHistory.setString(2, serialNum);
			searchHistory.setString(3, serialNum);
			history = searchHistory.executeQuery();
			while (history.next()) {
				System.out.println("Serial #: " + history.getString("v.serial_no"));
				System.out.println("Transactions: " + history.getString("transCount"));
				System.out.println("Average price: " + history.getString("avgPrice"));
				System.out.println("Tickets: " + history.getString("ticCount"));
				System.out.println("\n");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

