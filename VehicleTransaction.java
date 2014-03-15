import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Console;
import java.text.ParseException;

public class VehicleTransaction {
	
	public static void autoTransaction(Connection dbConn){
		PreparedStatement checkSerial, checkSeller, sellers;
		PreparedStatement checkSaleCount, checkPersonCount, addSale;
		PreparedStatement findPrim, addOwner, removeOwner;
		ResultSet serialCount, sellerCount, allSellers, saleCount;
		ResultSet personCount, sellerPrim;
		Scanner keyboard;
		int numBuyers = 0, transID = 0, i = 0;
 		double price;
		String serialNum, seller, inputDate, buyer, isPrimary;
		String primSeller = null;
		String[] buyers;
		java.util.Date sDate = new java.util.Date();
		java.sql.Date saleDate;
		java.util.Date currentDate = new java.util.Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";

		String querySellerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ?";

		String queryPersonCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";

		String querySellers = "SELECT owner_id FROM owner WHERE vehicle_id = ?";

		String queryTransCount = "SELECT COUNT(*) FROM auto_sale";

		String queryNewSale = "INSERT INTO auto_sale VALUES(?, ?, ?, ?, ?, ?)";

		String querySellerPrim = "SELECT owner_id FROM owner WHERE vehicle_id = ? AND is_primary_owner = 'y'";

		String queryRemoveOwner = "DELETE FROM owner WHERE vehicle_id = ?";
		
		String queryAddOwner ="INSERT INTO owner VALUES(?, ?, ?)";

		// Obtain transaction id
		try {
			checkSaleCount = dbConn.prepareStatement(queryTransCount);
			saleCount = checkSaleCount.executeQuery();
			saleCount.next();
			transID = saleCount.getInt(1) + 1;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
	
		// Request vehicle serial number being sold
		while(true) {
			System.out.print("Please enter the vehicle's serial number: ");

			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() > 15)
				System.out.println("Serial number invalid");
			else {
				try {
					checkSerial = dbConn.prepareStatement(querySerialCount);
					checkSerial.setString(1, serialNum);
					serialCount = checkSerial.executeQuery();
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

			String[] split = String.valueOf(price).split("\\."); 
			if ((split[1].length() != 2) || (split[0].length() > 7) || (split[0].length() < 1)) 
				System.out.println("Not a valid price");
			else {
				break;
			}	
		}

		// Obtain sale date
		while(true) {
			System.out.print("Please enter sale date in form YYYY-MM-DD: "); 
			keyboard = new Scanner(System.in);
			inputDate = keyboard.nextLine();
			try {
				formatDate.setLenient(false);
				sDate = formatDate.parse(inputDate);
			} catch (ParseException e) {
				System.out.println("Invalid sale date");
				continue;
			}
			if (currentDate.before(sDate))
				System.out.println("Sale date cannot be later than current date");
			else
				break;
				
		}
		saleDate = new java.sql.Date(sDate.getTime());

		// Find the current vehicle owner(s)
		while(true) {
			System.out.print("Please enter the seller's SIN: ");

			keyboard = new Scanner(System.in);
			seller = keyboard.nextLine();
			if (seller.length() > 15)
				System.out.println("SIN number invalid");
			else {
				try {
					checkSeller = dbConn.prepareStatement(querySellerCount);
					checkSeller.setString(1, seller);
					sellerCount = checkSeller.executeQuery();
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
		
		// Find or insert buyer information
		while(true) {
			System.out.print("Please enter the number of buyers: ");

			keyboard = new Scanner(System.in);
			numBuyers = keyboard.nextInt();
			buyers = new String[numBuyers];
			if (numBuyers <= 0) 
				System.out.println("There must be at least one buyer");
			else {
				while(i < numBuyers){
					// First entry is primary owner
					if (i == 0) {
						System.out.println("Please enter primary buyer's SIN: ");
					} else {
						System.out.println("Please enter buyer's SIN: ");
					}

					// Check if buyer is a person
					keyboard = new Scanner(System.in);
					buyer = keyboard.nextLine();
					if (buyer.length() > 15)
						System.out.println("SIN number invalid");
					else {
						try {
							checkPersonCount = dbConn.prepareStatement(queryPersonCount);
							checkSeller.setString(1, buyer);
							personCount = checkPersonCount.executeQuery();
							personCount.next();
							if (personCount.getInt(1) == 0) {
								// If buyer not a person, add them
								NewPeople.addPeople(dbConn, buyer, 1);
							}
							// Add buyer to array of buyers
							buyers[i] = buyer;
							i++;

						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
					}			
				}
				break;
			}
		}

		// Find primary owner selling vehicle
		try {
			findPrim = dbConn.prepareStatement(querySellerPrim);
			findPrim.setString(1, serialNum);
			sellerPrim = checkSeller.executeQuery();
			sellerPrim.next();
			primSeller = sellerPrim.getString(1);
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}

		// Update database
		// Input transation in auto_sale table
		try {
			addSale = dbConn.prepareStatement(queryNewSale);
			addSale.setInt(1, transID);
			addSale.setString(2, primSeller);
			addSale.setString(3, buyers[0]);
			addSale.setString(4, serialNum);
			addSale.setDate(5, saleDate);
			addSale.setDouble(6, price);
			addSale.executeUpdate();
			System.out.println("Transaction added to database");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}


		// Remove then add owners from/to owner table
		try {	
			// Remove
			removeOwner = dbConn.prepareStatement(queryRemoveOwner);
			removeOwner.setString(1, serialNum);
			removeOwner.executeUpdate();		

			// Add	
			for (int j = 0; j < numBuyers; j++) {
				if (j == 0) 
					isPrimary = "Y";
				else 
					isPrimary = "N";
				addOwner = dbConn.prepareStatement(queryAddOwner);
				addOwner.setString(1, buyers[j]);
				addOwner.setString(2, serialNum);
				addOwner.setString(3, isPrimary);
				addOwner.executeUpdate();
			}
			System.out.println("Owner table updated to reflect sale");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}

	}
}

