import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Console;
import java.text.ParseException;

public class Violation {
	
	public static void violationRecord(Connection dbConn) {
		PreparedStatement addTicket;
		String queryTicketCount = "SELECT COUNT(*) FROM ticket";
		String queryPersonCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryHasLicence = "SELECT COUNT(sin) FROM drive_licence WHERE sin = ?";
		String queryIsPrimary = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ? AND vehicle_id = ?";
		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String queryTicketType = "SELECT COUNT(vtype) FROM ticket_type WHERE vtype = ?";
		String queryAddTicket = "INSERT INTO ticket VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		String driver, offenderID, serialNum, officerID, ticketType, place, comments;
		int maxString = 1, varString = 0, noNum = 1, incNum = 0, ticketID, exists;
		java.util.Date vDate = new java.util.Date();
		java.util.Date currentDate = new java.util.Date();
		java.sql.Date violationDate;

		// Obtain ticket number
		ticketID = database.getNextID(dbConn, queryTicketCount);
		
		// Obtain vehicle id
		while (true){
			System.out.print("Please enter the vehicle's serial number: ");
			serialNum = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, querySerialCount, serialNum);
			if (exists == 0){
				System.out.println("Vehicle not in database");
			} else {
				break;
			}
		}
		
		// Check who the ticket should be issued to
		System.out.print("Was the driver identified (y or n)? ");
		driver = database.requestYorN();
		
		// Obtain violator id
		while(true) {
			if (driver.equalsIgnoreCase("y")){
				System.out.print("Please enter driver's SIN: ");
			} else {
				System.out.print("Please enter primary owner's SIN: ");
			}
			offenderID = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryPersonCount, offenderID);
			if (exists == 0) {
				// Person not found in database
				System.out.println("No matching person on record");
			} else {
				if (driver.equalsIgnoreCase("y")) {
					// Check if person has licence
					exists = database.checkExistence(dbConn, queryHasLicence, offenderID);
					if (exists == 0){ 
						System.out.println("This person does not have a licence");
					} else {
						break;
					}
				} else {
					// Check if person is primary owner
					exists = database.checkExistence(dbConn, queryIsPrimary, offenderID, serialNum);
					if (exists == 0){ 
						System.out.println("This person is not the primary owner of the vehicle");
					} else {
						break;
					}
				}
			}
		}

		// Obtain officer id
		while(true) {
			System.out.print("Please enter the officer's SIN: ");
			officerID = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryPersonCount, officerID);
			if (exists == 0) {
				System.out.println("No matching officer on record");
			} else {
				break;
			}
		}

		// Obtain violation type
		while (true) {
			System.out.print("Please enter ticket type: ");
			ticketType = database.requestString(10, maxString, incNum);
			exists = database.checkExistence(dbConn, queryTicketType, ticketType);
			if (exists == 0){
				// Should not reach here
				System.out.println("Type not in database");
			} else {
				break;
			}
		}
		
		// Obtain violation date
		while (true) {
			System.out.print("Please enter the date of the violation in YYYY-MM-DD format: ");
			vDate = database.requestDate();
			if (currentDate.before(vDate)){
				System.out.println("Violation date cannot be later than current date");
			} else{
				break;
			}
		}
		violationDate = new java.sql.Date(vDate.getTime());
		

		// Obtain violation place
		System.out.print("Please enter the location where the violation took place: ");
		place = database.requestString(20, varString, incNum);

		// Obtain descriptions
		System.out.print("Please enter any further comments: ");
		comments = database.requestString(1024, varString, incNum);

		// Add violation record to database
		try {
			addTicket = dbConn.prepareStatement(queryAddTicket);
			addTicket.setInt(1, ticketID);
			addTicket.setString(2, offenderID);
			addTicket.setString(3, serialNum);
			addTicket.setString(4, officerID);
			addTicket.setString(5, ticketType);
			addTicket.setDate(6, violationDate);
			addTicket.setString(7, place);
			addTicket.setString(8, comments);
			addTicket.executeUpdate();
			System.out.println("Ticket has been added to database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

