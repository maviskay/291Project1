import java.sql.*;
import java.util.Calendar;

public class NewVehicle {
	// Registers new vehicle
	public static void vehicleRegistration(Connection dbConn) {
		PreparedStatement addVehicle;
		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String queryNewVehicle = "INSERT INTO vehicle VALUES(?, ?, ?, ?, ?, ?)";
		int maxString = 1, varString = 0, noNum = 1, incNum = 0, exists = 0, typeID;
		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		Double year;
		String serialNum, maker, model, color;

		// Requests for serial number
		while (true) {
			System.out.print("Please enter the vehicle's serial number: ");
			serialNum = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, querySerialCount,
					serialNum);
			if (exists == 1) {
				System.out.println("Vehicle already exists");
			} else {
				break;
			}
		}

		// Requests for vehicle make
		System.out.print("Please enter make of vehicle: ");
		maker = database.requestString(20, varString, incNum);

		// Requests for vehicle model
		System.out.print("Please enter model of vehicle: ");
		model = database.requestString(20, varString, incNum);
		
		// Requests for vehicle year
		while (true) {
			System.out.print("Please enter year of vehicle: ");
			year = database.requestInt(4, 1);
			if (year.intValue() > currYear) {
				System.out.println("Year of vehicle invalid");
			} else
				break;
		}
		
		// Requests for vehicle color
		System.out.print("Please enter color of vehicle: ");
		color = database.requestString(10, varString, noNum);
		
		// Requests for vehicle type
		while (true) {
			typeID = checkVehicleType(dbConn);
			// Should not reach here
			if (typeID == -1)
				System.out.println("Type of vehicle invalid");
			else
				break;
		}
		
		// Inserts vehicle to database
		try {
			addVehicle = dbConn.prepareStatement(queryNewVehicle);
			addVehicle.setString(1, serialNum);
			addVehicle.setString(2, maker);
			addVehicle.setString(3, model);
			addVehicle.setDouble(4, year);
			addVehicle.setString(5, color);
			addVehicle.setInt(6, typeID);
			addVehicle.executeUpdate();
			addVehicle.close();
			System.out.println("Vehicle added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		// Asks for number of owners
		System.out.println("Please register owners of the vehicle");
		NewOwner.numOwner(dbConn, serialNum);
	}

	// Checks for vehicle type and returns type_id
	public static int checkVehicleType(Connection dbConn) {
		PreparedStatement findType;
		ResultSet typeResult;
		String queryTypeID = "SELECT type_id FROM vehicle_type WHERE type = ?";
		String type;
		int maxString = 1, noNum = 1, typeID = -1;

		System.out.print("Please enter type of vehicle: ");
		type = database.requestString(10, maxString, noNum);
		try {
			// Assume all vehicle types are already in database
			findType = dbConn.prepareStatement(queryTypeID);
			findType.setString(1, type);
			typeResult = findType.executeQuery();
			while (typeResult.next())
				typeID = typeResult.getInt("type_id");
			typeResult.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return typeID;
	}
}



