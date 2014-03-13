import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewLicence {
	// Registers new licence
    public static void licenceRegistration(Connection dbConn, String sin, String licence_no, int personAdded) {
		Scanner keyboard;
		PreparedStatement checkLicence;
		ResultSet licenceCount;
		File photo;
		String licenceNum, classVal, photoFile, expireDate;
		String path = System.getProperty("user.dir");
		String queryLicenceCount = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date issueDate = new java.sql.Date(utilDate.getTime());
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date exDate = null;

		// TODO: request/check sin number
		// Requests for licence number
		while (true) {
			// Licence number already given
			if (licence_no.contains("-1"))
				break;
		    System.out.print("Please enter the licence number: ");
			keyboard = new Scanner(System.in);
			licenceNum = keyboard.nextLine();
			if (licenceNum.length() != 15)
				System.out.println("Licence number invalid");
			else{
				try{
					checkLicence = dbConn.prepareStatement(queryLicenceCount);
					checkLicence.setString(1, licenceNum);
					licenceCount = checkLicence.executeQuery();
					licenceCount.next();
					if (licenceCount.getInt(1) != 0){					
						licenceCount.close();
						System.out.println("Licence already exists");
					} else
						break;
				} catch (SQLException e){
					System.out.println(e.getMessage());
				}
			}
		}
		// Requests for class
		while (true) {
		    System.out.print("Please enter the class: ");
			keyboard = new Scanner(System.in);
			classVal = keyboard.nextLine();
			if (classVal.length() > 10)
				System.out.println("Class entered is too long");
			else{
				break;
			}
		}
		// Request photo
		while (true) {
		    System.out.print("Please enter the drivers photo in current directory: ");
			keyboard = new Scanner(System.in);
			photoFile = keyboard.nextLine();
			path += "/" + photoFile;
			photo = new File(photoFile);
			break;
		}
		// Request expiring date
		while (true) {
		    System.out.print("Please enter the expiry date (YYYY-MM-DD): ");
			keyboard = new Scanner(System.in);
			expireDate = keyboard.nextLine();
			try{
				formatDate.setLenient(false);
				exDate = formatDate.parse(expireDate);
			} catch (ParseException e) {
				System.out.println("Expiry date not valid");
				continue;
			}			
			if (utilDate.after(exDate))
				System.out.println("Expiry date cannot be earlier than current date");
			else
				break;
		}
	}
	
}
