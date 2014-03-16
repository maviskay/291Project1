import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Date;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;

public class NewLicence {
	
	public static void ownsLicence(Connection dbConn, String sin){
		String queryLicenceCount = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String hasLicence, licenceNum;
		int maxString = 1, noNum = 1, incNum = 0, exists;
		
		while (true){
			System.out.print("Does this person have a licence (y or n): ");
			hasLicence = database.requestYorN();
			if(hasLicence.equalsIgnoreCase("n")){
				// Person does not have licence
				return;
			} else {
				break;
			}
		}
		
		while (true){
			System.out.print("Please enter licence number: ");
			licenceNum = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryLicenceCount, licenceNum);
			if (exists == 1){
				System.out.println("Licence already exists");
			} else {
				NewLicence.licenceRegistration(dbConn, sin, licenceNum);
				break;
			}	
		}
	}
	
	// Registers new licence
	public static void licenceRegistration(Connection dbConn, String sin,
			String licence_no) {
		PreparedStatement addLicence, addCondition, addRestriction;
		File photo;
		FileInputStream photoArray;
		Date expiry;
		java.sql.Date expireDate;
		java.util.Date currDate = new java.util.Date();
		java.sql.Date issueDate = new java.sql.Date(currDate.getTime());
		String querySinCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String querySinLicence = "SELECT COUNT(licence_no) FROM drive_licence WHERE sin = ?";
		String queryLicenceCount = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String queryNewLicence = "INSERT INTO drive_licence VALUES(?, ?, ?, ?, ?, ?)";
		String queryConditionCount = "SELECT COUNT(c_id) FROM driving_condition WHERE c_id = ?";
		String queryNewCondition = "INSERT INTO driving_condition VALUES(?, ?)";
		String queryNewRestriction = "INSERT INTO restriction VALUES(?, ?)";
		int maxString = 1, varString = 0, noNum = 1, incNum = 0, exists, condition;
		String classVal, photoFile, hasCondition, condDes;

		// Request for sin number
		if (sin.equalsIgnoreCase("-1")){
			while (true) {
				System.out.print("Please enter the sin number: ");
				sin = database.requestString(15, maxString, incNum);
				exists = database.checkExistence(dbConn, querySinCount, sin);
				if (exists == 1){
					// Person exists, check if they have a licence
					exists = database.checkExistence(dbConn, querySinLicence, sin);
					if (exists == 1){
						// Person has licence, request new sin
						System.out.println("Person already owns a licence");
					} else {
						// Person does not have licence
						break;
					}
				} else {
					// Person does not exist, add person to database
					NewPeople.addPeople(dbConn, sin, 0);
					break;
				}
			}
		}
			
		// Requests for licence number
		if (licence_no.equalsIgnoreCase("-1")){
			while (true) {
				System.out.print("Please enter the licence number: ");
				licence_no = database.requestString(15, maxString, incNum);
				exists = database.checkExistence(dbConn, queryLicenceCount, licence_no);
				if (exists == 1) {
					System.out.println("Licence already exists");
				} else {
					break;
				}
			}
		}
		
		// Requests for class
		System.out.print("Please enter the class: ");
		classVal = database.requestString(10, varString, incNum);
		
		// Request photo
		while (true) {
			System.out.print("Please enter drivers photo in current directory: ");
			photoFile = database.requestString(50, varString, incNum);
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
			expiry = database.requestDate();
			if (expiry.before(currDate))
				System.out
						.println("Expiry date cannot be earlier than current date");
			else
				break;
		}
		expireDate = new java.sql.Date(expiry.getTime());

		// Inserts licence to database
		try {
			addLicence = dbConn.prepareStatement(queryNewLicence);
			addLicence.setString(1, licence_no);
			addLicence.setString(2, sin);
			addLicence.setString(3, classVal);
			addLicence.setBinaryStream(4, photoArray, photoArray.available());
			addLicence.setDate(5, issueDate);
			addLicence.setDate(6, expireDate);
			addLicence.executeUpdate();
			System.out.println("Licence added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}

		// Check if person has conditions
		while (true) {
			System.out.print("Does this person have any special conditions (y or n)? ");
			hasCondition = database.requestYorN();
			if (hasCondition.equalsIgnoreCase("n")){
				// Person does not have special conditions
				return;
			} else {
				break;
			}
		}
		
		// Request drive condition
		System.out.print("Please enter the condition id: ");
		condition = database.requestInt(2, 1).intValue();
		exists = database.checkExistence(dbConn, queryConditionCount, condition);
		
		// Request condition description
		if (exists == 0) {
			System.out.print("Please enter condition description: ");
			condDes = database.requestString(1024, varString, incNum);
			// Add condition
			try {
				addCondition = dbConn.prepareStatement(queryNewCondition);
				addCondition.setInt(1, condition);
				addCondition.setString(2, condDes);
				addCondition.executeUpdate();
				System.out.println("New driving conditions added");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		// Add restriction
		try {
			addRestriction = dbConn.prepareStatement(queryNewRestriction);
			addRestriction.setString(1, licence_no);
			addRestriction.setInt(2, condition);
			addRestriction.executeUpdate();
			System.out.println("Restriction added");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
