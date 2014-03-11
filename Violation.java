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
		ResultSet ticketCount, offenderCount, serialCount;
		int ticketID = 0;
		String offenderID, serialNum, inputDate;
		Scanner keyboard;
		java.sql.Date violationDate;
		java.util.Date vDate = new java.util.Date();
		java.util.Date currentDate = new java.util.Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

		String queryTicketCount = "SELECT COUNT(*) FROM ticket";

		String queryOffenderCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";

		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";

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

		// TODO Obtain officer id

		// TODO Obtain violation type

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

		// TODO Obtain violation place

		// TODO Obtain descriptions

		// TODO Add violation record to database
	}

}
