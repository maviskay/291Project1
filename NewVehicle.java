import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;

public class NewVehicle{
	// Registers new vehicle
	public static void vehicleRegistration(Connection dbConn) {
		PreparedStatement checkSerial, findOwner, addVehicle;
		ResultSet serialCount, ownerCount;
		Scanner keyboard;
		String serialNum, maker, model, color, ownerID;
		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String queryOwnerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_no = ?";
		String queryNewVehicle = "INSERT INTO vehicle VALUES(?, ?, ?, ?, ?, ?)";
		int year, typeID;
		int currYear = Calendar.getInstance().get(Calendar.YEAR);

		System.out.println("You have selected new vehicle registration");
		// Requests for serial number
		while (true) {
		    System.out.print("Please enter the vehicle's serial number: ");
			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() != 15)
				System.out.println("Serial number invalid");
			else{
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
		// TODO: Insert new vehicle to database
		
/*		//Checks to see if owner exists
		while (true) {
			System.out.print("Please enter the owner id: ");
			keyboard = new Scanner(System.in);
			ownerID = keyboard.nextLine();
			if (ownerID.length() != 15)
				System.out.println("Owner id invalid");
			else{
				findOwner = dbConn.prepareStatement(queryOwnerCount);
				findOwner.setString(1, owner);
				ownerCount = findType.executeQuery();
				ownerCount.next();
				if (ownerCount.getInt(1) != 0){					
					ownerCounts.close();
					System.out.println("Owner does not exist, please enter information of owner");
					addOwner(dbConn, ownerID);
				}
				// owner exists, add vehicle
				break;
					
			}
		}
		if (ownerID.equals(NULL))
			addOwner(dbConn);
		else
			// owner exists, only add vehicle 
*/
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
	
	/*public static void addOwner(Connection dbConn, String ownerID){
		
	}*/
}
