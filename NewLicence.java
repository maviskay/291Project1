import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Date;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;

public class NewLicence {
	// Registers new licence
	public static void licenceRegistration(Connection dbConn, String sin,
			String licence_no, int personAdded) {
		FileInputStream photoArray = null;
		Scanner keyboard;
		PreparedStatement checkLicence, checkSin, addLicence, checkCondition, addCondition, addRestriction;
		ResultSet licenceCount, sinCount, licenceExist, conditionCount;
		File photo;
		String classVal, photoFile, expireDate, hasCondition, condDes;
		String path = System.getProperty("user.dir");
		String queryLicenceCount = "SELECT COUNT(licence_no) FROM drive_licence WHERE licence_no = ?";
		String querySinLicence = "SELECT COUNT(licence_no) FROM drive_licence WHERE sin = ?";
		String querySinCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryNewLicence = "INSERT INTO drive_licence VALUES(?, ?, ?, ?, ?, ?)";
		String queryConditionCount = "SELECT COUNT(c_id) FROM driving_condition WHERE c_id = ?";
		String queryNewCondition = "INSERT INTO driving_condition VALUES(?, ?)";
		String queryNewRestriction = "INSERT INTO restriction VALUES(?, ?)";
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date issueDate = new java.sql.Date(utilDate.getTime());
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date exDate = null;
		java.sql.Date eDate;
		int padding, condition, conditionExists = 0;

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
			else {
				padding = 15 - sin.length();
				for (int i = 0; i < padding; i++)
					sin += " ";
				try {
					checkSin = dbConn.prepareStatement(querySinCount);
					checkSin.setString(1, sin);
					sinCount = checkSin.executeQuery();
					sinCount.next();
					if (sinCount.getInt(1) != 0) {
						sinCount.close();
						checkLicence = dbConn.prepareStatement(querySinLicence);
						checkLicence.setString(1, sin);
						licenceExist = checkLicence.executeQuery();
						licenceExist.next();
						if (licenceExist.getInt(1) != 0) {
							licenceExist.close();
							System.out
									.println("This person already has a licence");
							sin = "-1";
						} else
							break;
					} else {
						System.out.println("Person does not exist");
						NewPeople.addPeople(dbConn, sin, 0);
						break;
					}
				} catch (SQLException e) {
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
			licence_no = keyboard.nextLine();
			if (licence_no.length() > 15)
				System.out.println("Licence number invalid");
			else {
				padding = 15 - licence_no.length();
				for (int i = 0; i < padding; i++)
					licence_no += " ";
				try {
					checkLicence = dbConn.prepareStatement(queryLicenceCount);
					checkLicence.setString(1, licence_no);
					licenceCount = checkLicence.executeQuery();
					licenceCount.next();
					if (licenceCount.getInt(1) != 0) {
						licenceCount.close();
						System.out.println("Licence already exists");
						licence_no = "-1";
					} else
						break;
				} catch (SQLException e) {
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
			else {
				break;
			}
		}
		// Request photo
		while (true) {
			System.out
					.print("Please enter the drivers photo in current directory: ");
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
			try {
				formatDate.setLenient(false);
				exDate = formatDate.parse(expireDate);
			} catch (ParseException e) {
				System.out.println("Expiry date not valid");
				continue;
			}
			if (utilDate.after(exDate))
				System.out
						.println("Expiry date cannot be earlier than current date");
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

		// Check if person has conditions
		System.out.print("Does this person have any special conditions (y or n)? ");
		while (true) {
			keyboard = new Scanner(System.in);
			hasCondition = keyboard.nextLine();
			if (hasCondition.length() != 1) {
				System.out
						.print("Selection invalid\n Please enter 'y' or 'n': ");
			} else if (hasCondition.contains("y")) {
				break;
			} else
				return;
		}
		// Request drive condition
		while (true) {
			System.out.print("Please enter the condition id: ");
			keyboard = new Scanner(System.in);
			condition = keyboard.nextInt();
			try {
				checkCondition = dbConn.prepareStatement(queryConditionCount);
				checkCondition.setInt(1, condition);
				conditionCount = checkCondition.executeQuery();
				conditionCount.next();
				if (conditionCount.getInt(1) != 0) {
					conditionCount.close();
					conditionExists = 1;
					break;
				} else {
					conditionCount.close();
					conditionExists = 0;
					break;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// Request condition description
		while (true) {
			if (conditionExists == 0) {
				System.out.print("Please enter condition description: ");
				keyboard = new Scanner(System.in);
				condDes = keyboard.nextLine();
				if (condDes.length() > 1024)
					System.out.println("Description too long");
				else {
					try {
						addCondition = dbConn
								.prepareStatement(queryNewCondition);
						addCondition.setInt(1, condition);
						addCondition.setString(2, condDes);
						addCondition.executeUpdate();
						System.out.println("New driving conditions added");
						break;
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		// Insertion restriction
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

