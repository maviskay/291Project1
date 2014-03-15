import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;

public class NewVehicle{
	// Registers new vehicle
	public static void vehicleRegistration(Connection dbConn) {
		PreparedStatement checkSerial, findPeople, addVehicle;
		ResultSet serialCount, peopleCount;
		Scanner keyboard;
		String serialNum, maker, model, color, ownerID = null, primary = " ";
		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String queryPeopleCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryNewVehicle = "INSERT INTO vehicle VALUES(?, ?, ?, ?, ?, ?)";
		int year, typeID, padding, ownerCount, primOwner = 0;
		int currYear = Calendar.getInstance().get(Calendar.YEAR);

		// Requests for serial number
		while (true) {
		    System.out.print("Please enter the vehicle's serial number: ");
			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() > 15)
				System.out.println("Serial number invalid");
			else{
				padding = 15 - serialNum.length();
				for (int i = 0; i < padding; i++)
					serialNum += " ";
				try{
					checkSerial = dbConn.prepareStatement(querySerialCount);
					checkSerial.setString(1, serialNum);
					serialCount = checkSerial.executeQuery();
					serialCount.next();
					if (serialCount.getInt(1) != 0){					
						serialCount.close();
						System.out.println("Vehicle already exists");
					} else
						break;
				} catch (SQLException e){
					System.out.println(e.getMessage());
				}
			}
		}
		// Requests for vehicle make
		while (true) {
			System.out.print("Please enter make of vehicle: ");
		    keyboard = new Scanner(System.in);
			maker = keyboard.nextLine();
			if (maker.length() > 20)
				System.out.println("Make of vehicle invalid");
			else
			    break;
		}
		// Requests for vehicle model
		while (true) {
		    System.out.print("Please enter model of vehicle: ");
		    keyboard = new Scanner(System.in);
		    model = keyboard.nextLine();
		    if (model.length() > 20)
				System.out.println("Model of vehicle invalid");
		    else
				break;
		}
		// Requests for vehicle year
		while (true) {
		    System.out.print("Please enter year of vehicle: ");
		    keyboard = new Scanner(System.in);
			try{
			    year = keyboard.nextInt();
				if (Integer.toString(year).length() != 4 || year > currYear)
					System.out.println("Year of vehicle invalid");
				else
					break;
			} catch (InputMismatchException e) {
			    System.out.println("Invalid year");
			    continue;
			}
		}
		// Requests for vehicle color
		while (true) {
			System.out.print("Please enter color of vehicle: ");
			keyboard = new Scanner(System.in);
		    color = keyboard.nextLine();
		    if (color.length() > 10)
				System.out.println("Color of vehicle invalid");
		    else
				break;
		}
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
			addVehicle.setInt(4, year);
			addVehicle.setString(5, color);
			addVehicle.setInt(6, typeID);
			addVehicle.executeUpdate();
			System.out.println("Vehicle added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// Asks for number of owners
		System.out.println("Please register owner of the vehicle");
		while (true) {
			try {
				System.out.print("How many people own this vehicle: ");
				keyboard = new Scanner(System.in);
				ownerCount = keyboard.nextInt();
				break;
			} catch (InputMismatchException e) {
			    System.out.println("Invalid number of owners");
			    continue;
			}
		}
		// Checks to see if owner exists
		while (true) {
			int j = 0;
			primOwner = 0;
			while (j < ownerCount) {
				System.out.print("Please enter the owner id: ");
				keyboard = new Scanner(System.in);
				ownerID = keyboard.nextLine();
				if (ownerID.length() > 15)
					System.out.println("Owner id invalid");
				else {
					padding = 15 - ownerID.length();
					for (int i = 0; i < padding; i++)
						ownerID += " ";
					try {
						findPeople = dbConn.prepareStatement(queryPeopleCount);
						findPeople.setString(1, ownerID);
						peopleCount = findPeople.executeQuery();
						peopleCount.next();
						if (peopleCount.getInt(1) != 0){					
							peopleCount.close();
							System.out.println("Person does not exist, please enter information of owner");
							// Person does not exist, request person info
							NewPeople.addPeople(dbConn, ownerID);
						}
					} catch (SQLException e){
						System.out.println(e.getMessage());
					}
					// Check if primary owner
					while (true) {
						if (primary.contains("y") && primary.length() == 1)
							break;
						System.out.print("Is this owner a primary owner: ");
						keyboard = new Scanner(System.in);
						primary = keyboard.nextLine();
						if (primary.length() != 1)
							System.out.println("Primary owner status invalid");
						else if (primary.contains("n"))
							break;
						else if (primary.contains("y")) {
							primOwner = 1;
							break;
						} else
							System.out.println("Primary owner status invalid");
					}
					// Inserts owner to database
					NewOwner.addOwner(dbConn, serialNum, ownerID, primary);
					j++;
				}
			}
			break;
		}
	}

	// Checks for vehicle type and returns type_id
	public static int checkVehicleType(Connection dbConn){
		PreparedStatement findType;
		ResultSet typeResult;
		Scanner keyboard;
		String type;
		String queryTypeID = "SELECT type_id FROM vehicle_type WHERE type = ?";
		int padding, typeID = -1;

		System.out.print("Please enter type of vehicle: ");
		keyboard = new Scanner(System.in);
		type = keyboard.nextLine();
		padding = 10 - type.length();
		if (type.length() > 10)
			return -1;
		else{
			// Padding for char(10) of type
			for (int i = 0; i < padding; i++)
				type += " ";
			try{
				// Assume all vehicle types are already in database
				findType = dbConn.prepareStatement(queryTypeID);
				findType.setString(1, type);
				typeResult = findType.executeQuery();
				while(typeResult.next())
					typeID = typeResult.getInt("type_id");
				typeResult.close();
			} catch (SQLException e){
				System.out.println(e.getMessage());
			}
		}
		return typeID;
	}
}
