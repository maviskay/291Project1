import java.sql.*;
import java.util.Scanner;
import java.io.Console;
import java.util.InputMismatchException;

public class Search {

	public static void searchEngine(Connection dbConn) {
		PreparedStatement DriverByName, DriverByLNo, TicketsBySin; 
		PreparedStatement TicketsByLNo, VehicleHist;
		ResultSet driver, tickets, history;
		Scanner keyboard;
		int selection;

		String queryDriverByName = "SELECT p.name, l.licence_no, p.addr, p.birthday,l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND p.name = ?";

		String queryDriverByLNo = "SELECT p.name, l.licence_no, p.addr, p.birthday,l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND l.licence_no = ?";

		String queryTicketsBySin = "SLEECT * FROM ticket WHERE violator_no = ?";

		String queryTicketsByLNo = "SLEECT t.* FROM ticket t, drive_licence l WHERE l.sin = t.violator_no AND l.licence_no = ?";

		String queryVehicleHist = "SELECT v.serial_no, COUNT(DISTINCT s.transaction_id), AVG(s.price), COUNT(DISTINCT t.ticket_no) FROM vehicle v, auto_sale s, ticket t WHERE s.vehicle_id (+)= ? AND t.vehicle_id (+)= ? GROUP BY v.serial_no ORDER BY v.serial_no";

		// Prompt the user to input which search they want
		while(true) {
			System.out.println("Please select from the following search options:");
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
				System.out.println("");
	
			} else if (selection == 2) {
				System.out.println("");	
				
			} else if (selection == 3) {
				System.out.println("");	
				
			} else if (selection == 4) {
				System.out.println("");	
				
			} else if (selection == 5) {
				System.out.println("");	
				
			} else {
				System.out.println("Invalid option");
			}
		}
	}	
}
