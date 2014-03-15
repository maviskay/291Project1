import java.sql.*;
import java.util.Scanner;

public class NewOwner {
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

