import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.util.Calendar;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;

public class NewLicence {
	// Registers new licence
    public static void licenceRegistration(Connection dbConn, String sin, String licence_no, int personAdded) {
		FileInputStream photoArray = null;
		Scanner keyboard;
		PreparedStatement checkLicence, checkSin, addLicence;
		ResultSet licenceCount, sinCount;
		File photo;
		String licenceNum, classVal, photoFile, expireDate;
		String path = System.getProperty("user.dir");
		String queryLicenceCount = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String querySinCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryNewLicence = "INSERT INTO drive_licence VALUES(?, ?, ?, ?, ?, ?)";
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date issueDate = new java.sql.Date(utilDate.getTime());
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date exDate = null;
		java.sql.Date eDate;
		int padding;

		// Request for sin number
		while (true) {
			// Sin number already given
			if (!sin.equalsIgnoreCase("-1"))
				break;
			System.out.print("Please enter the sin number: ");
			keyboard = new Scanner(System.in);
			sin = keyboard.nextLine();
			if (sin.length() > 15)
				System.out.println("Sin number invalid");
			else{
				padding = 15 - sin.length();
				for (int i = 0; i < padding; i++)
					sin += " ";
				try{
					checkSin = dbConn.prepareStatement(querySinCount);
					checkSin.setString(1, sin);
					sinCount = checkSin.executeQuery();
					sinCount.next();
					if (sinCount.getInt(1) != 0){					
						sinCount.close();
						System.out.println("Sin already exists");
						sin = "-1";
					} else
						break;
				} catch (SQLException e){
					System.out.println(e.getMessage());
				}
			}
		}
		// Requests for licence number
		while (true) {
			// Licence number already given
			if (!licence_no.equalsIgnoreCase("-1"))
				break;
		    System.out.print("Please enter the licence number: ");
			keyboard = new Scanner(System.in);
			licenceNum = keyboard.nextLine();
			if (licenceNum.length() > 15)
				System.out.println("Licence number invalid");
			else{
				padding = 15 - licenceNum.length();
				for (int i = 0; i < padding; i++)
					licenceNum += " ";
				try{
					checkLicence = dbConn.prepareStatement(queryLicenceCount);
					checkLicence.setString(1, licenceNum);
					licenceCount = checkLicence.executeQuery();
					licenceCount.next();
					if (licenceCount.getInt(1) != 0){					
						licenceCount.close();
						System.out.println("Licence already exists");
						licenceNum = "-1";
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
			try {
				photoArray = new FileInputStream(photo);
				break;
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
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
		eDate = new java.sql.Date(exDate.getTime());
		// Inserts licence to database
		try {
			addLicence = dbConn.prepareStatement(queryNewLicence);
			addLicence.setString(1, licence_no);
			addLicence.setString(2, sin);
			addLicence.setString(3, classVal);
			addLicence.setBinaryStream(4, photoArray, photoArray.available());
			addLicence.setDate(5, issueDate);
			addLicence.setDate(6, eDate);
			addLicence.executeUpdate();
			System.out.println("Licence added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}
	
}

