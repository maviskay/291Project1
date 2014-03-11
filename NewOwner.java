import java.sql.*;

public class NewOwner{
	public static void addOwner(Connection dbConn, String ownerID, String vehicleID, String primary) {
		// Requests for primary owner
		// TODO: check if vehicle already has primary owner
		while (true) {
			System.out.print("Please enter name of person: ");
		    keyboard = new Scanner(System.in);
			name = keyboard.nextLine();
			if (name.length() > 40)
				System.out.println("Name of person invalid");
			else
			    break;
		}
	}
}
