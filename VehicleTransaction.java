import java.sql.*;
import java.util.Scanner;

public class VehicleTransaction {
	
	public static void autoTransaction(Connection dbConn){
		preparedStatement checkSerial, checkSeller, sellers;
		ResultSet = serialCount, sellerCount, allSellers;
		String serialNum, seller;
		int numBuyers;		
		double price;
		Scanner keyboard;

		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";

		String querySellerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ?";

		String querySellers = "SELECT owner_id FROM owner WHERE vehicle_id = ?";

		System.out.println("You have selected auto transaction");
		
		// Request vehicle serial number being sold
		while(true) {
			System.out.print("Please enter the vehicle's serial numberL ");

			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() != 15)
				System.out.println("Serial number invalid");
			else {
				try {
					checkSerial = dbConn.prepareStatement(querySerialCount);
					checkSerial.setString(1, serialNum);
					serialCount = checkSerial.executeQuery():
					serialCount.next();
					if (serialCount.getInt(1) == 0) {
						serialCount.close();	
						System.out.println("Vehicle not in database");
					} else 
						break;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		// Obtain selling price
		while(true) {
			System.out.print("Please enter the selling price: ");
			
			keyboard = new Scanner(System.in);
			price = keyboard.nextDouble();
			if (price.
		}

		// Find the current vehicle owner(s)
		while(true) {
			System.out.print("Please enter the seller's SIN: ");

			keyboard = new Scanner(System.in);
			seller = keyboard.nextLine();
			if (seller.length() != 15)
				System.out.println("SIN number invalid");
			else {
				try {
					checkSeller = dbConn.prepareStatement(querySellerCount);
					checkSeller.setString(1, seller);
					sellerCount = checkSerial.executeQuery():
					sellerCount.next();
					if (sellerCount.getInt(1) == 0) {
						sellerCount.close();	
						System.out.println("Seller not in database");
					} else {
						// Obtain all owners
						sellers = dbConn.prepareStatement(querySellers);
						allSellers = sellers.executeQuery();
						break;
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
					
		}
		
		// TODO Find or insert buyer information
		while(true) {
			System.out.print("Please enter the number of buyers: ");

			keyboard = new Scanner(System.in);
			numBuyers = keyboard.nextInt();
			if (numBuyers <= 0) {
				System.out.println("There must be at least one buyer");
			else {
				try {
					for (int i = 0; i < numBuyers; i++) {
						
					}
	}
}
