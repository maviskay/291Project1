import java.sql.*;
import java.util.Scanner;

public class NewOwner {
	
	// Checks how many people own a vehicle
	public static void numOwner(Connection dbConn, String serialNum) {
		String queryPeopleCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryOwnerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ? AND vehicle_id = ?";
		String ownerID;
		int maxString = 1, incNum = 0, ownerCount, exists, i = 0;
		
		System.out.print("How many people own this vehicle: ");
		ownerCount = database.requestInt(3, 1).intValue();
		
		// Adds owner
		while (i < ownerCount) {
			if (i == 0) {
				System.out.print("Please enter the primary owner id: ");
			} else {
				System.out.print("Please enter the owner id: ");
			}
			ownerID = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryPeopleCount, ownerID);
			if (exists == 0) {
				System.out
						.println("Person does not exist, please enter information of owner");
				// Person does not exist, request person info
				NewPeople.addPeople(dbConn, ownerID, 1);
			} else {
				exists = database.checkExistence(dbConn, queryOwnerCount, ownerID, serialNum);
				if (exists == 1){
					System.out.println("This person already owns the vehicle");
					continue;
				}
			}
			if (i == 0) {
				// Add primary owner
				NewOwner.addOwner(dbConn, serialNum, ownerID, "y");
				i++;
			} else {
				// Add regular owner
				NewOwner.addOwner(dbConn, serialNum, ownerID, "n");
				i++;
			}
		}
	}
	
	// Adds owner to the database
	public static void addOwner(Connection dbConn, String vehicleID,
			String ownerID, String primary) {
		PreparedStatement addOwner;
		String queryNewOwner = "INSERT INTO owner VALUES(?, ?, ?)";

		try {
			addOwner = dbConn.prepareStatement(queryNewOwner);
			addOwner.setString(1, ownerID);
			addOwner.setString(2, vehicleID);
			addOwner.setString(3, primary);
			addOwner.executeUpdate();
			System.out.println("Owner added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

