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
		PreparedStatement checkTicketCount, checkOffender, checkSerial;
		PreparedStatement addTicket, checkOfficer, checkType;
		ResultSet ticketCount, offenderCount, serialCount;
		ResultSet officerCount, typeCount;
		int ticketID = 0;
		String offenderID, serialNum, inputDate, place, comments;
		String officerID, ticketType;
		Scanner keyboard;
		java.sql.Date violationDate;
		java.util.Date vDate = new java.util.Date();
		java.util.Date currentDate = new java.util.Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

		String queryTicketCount = "SELECT COUNT(*) FROM ticket";

		String queryOffenderCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";

		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";

		String queryOfficerCount = "SELECT COUNT(sin) FROM people WHERE sin = ? AND name LIKE %Officer%";

		String queryTicketType = "SELECT COUNT(vtype) FROM ticket_type WHERE vtype = ?";

		String queryAddTicket = "INSERT INTO ticket VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		System.out.println("You have selected violation record");

		// Obtain ticket number
		try {
			checkTicketCount = dbConn.prepareStatement(queryTicketCount);
			ticketCount = checkTicketCount.executeQuery();
			ticketCount.next();
			ticketID = ticketCount.getInt(1) + 1;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Obtain violator id
		while(true) {
			System.out.print("Please enter the violator's SIN: ");
			keyboard = new Scanner(System.in);
			offenderID = keyboard.nextLine();
			if (offenderID.length() > 15)
				System.out.println("SIN invalid");
			else {
				try {
					checkOffender = dbConn.prepareStatement(queryOffenderCount);
					checkOffender.setString(1, offenderID);
					offenderCount = checkOffender.executeQuery();
					offenderCount.next();
					if (offenderCount.getInt(1) == 0) {
						offenderCount.close();
						System.out.println("No matching person on record");
					} else 
						break;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Obtain vehicle id
		while(true) {
			System.out.print("Please enter the vehicle's serial number: ");

			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() > 15)
				System.out.println("Serial number invalid");
			else {
				try {
					checkSerial = dbConn.prepareStatement(querySerialCount);
					checkSerial.setString(1, serialNum);
					serialCount = checkSerial.executeQuery();
					serialCount.next();
					if (serialCount.getInt(1) == 0) {
						serialCount.close();	
						System.out.println("Vehicle not in database");
					} else 
						break;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Obtain officer id
		while(true) {
			System.out.print("Please enter the officer's SIN: ");
			keyboard = new Scanner(System.in);
			officerID = keyboard.nextLine();
			if (offenderID.length() > 15)
				System.out.println("SIN invalid");
			else {
				try {
					checkOfficer = dbConn.prepareStatement(queryOfficerCount);
					checkOfficer.setString(1, officerID);
					officerCount = checkOfficer.executeQuery();
					officerCount.next();
					if (officerCount.getInt(1) == 0) {
						officerCount.close();
						System.out.println("No matching officer on record");
					} else 
						break;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Obtain violation type
		while(true) {
			System.out.print("Please enter ticket type: ");

			keyboard = new Scanner(System.in);
			ticketType = keyboard.nextLine();
			if (serialNum.length() > 10)
				System.out.println("Invalid type");
			else {
				try {
					checkType = dbConn.prepareStatement(queryTicketType);
					checkType.setString(1, ticketType);
					typeCount = checkType.executeQuery();
					typeCount.next();
					if (typeCount.getInt(1) == 0) {
						typeCount.close();	
						System.out.println("Type not in database");
					} else 
						break;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Obtain violation date
		while(true) {
			System.out.print("Please enter the date of the violation in YYYY-MM-DD format: ");
			keyboard = new Scanner(System.in);
			inputDate = keyboard.nextLine();
			try {
				formatDate.setLenient(false);
				vDate = formatDate.parse(inputDate);
			} catch (ParseException e) {
				System.out.println("Invalid date format");
				continue;
			}
			if (currentDate.before(vDate))
				System.out.println("Violation date cannot be later than current date");
			else
				break;
		}
		violationDate = new java.sql.Date(vDate.getTime());

		// Obtain violation place
		while(true) {
			System.out.print("Please enter the location where the violation took place: ");
			keyboard = new Scanner(System.in);
			place = keyboard.nextLine();
			if (place.length() > 20)
				System.out.println("Place can be no more than 20 characters");
			else
				break;
		}

		// Obtain descriptions
		while(true) {
			System.out.print("Please enter any further comments: ");
			keyboard = new Scanner(System.in);
			comments = keyboard.nextLine();
			if (place.length() > 1024)
				System.out.println("Comments can be no more than 1024 characters");
			else
				break;
		}

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
