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
		PreparedStatement addSale;
		String queryTransCount = "SELECT COUNT(*) FROM auto_sale";
		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";
		String querySellerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ? AND vehicle_id = ?";
		String queryBuyerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ? AND vehicle_id = ?";
		String queryRemoveOwner = "DELETE FROM owner WHERE vehicle_id = ? AND owner_id = ?";
		String queryPersonCount = "SELECT COUNT(sin) FROM people WHERE sin = ?";
		String queryNewSale = "INSERT INTO auto_sale VALUES(?, ?, ?, ?, ?, ?)";
		
		//String querySellers = "SELECT owner_id FROM owner WHERE vehicle_id = ?";
		String serialNum, seller, primSeller = null, buyer, primBuyer = null;
		int transID = 0, maxString = 1, incNum = 0, sellerCount, buyerCount, exists, i = 0;
		Double price;
		java.util.Date sDate = new java.util.Date();
		java.util.Date currentDate = new java.util.Date();
		java.sql.Date saleDate;

		// Obtain transaction id
		transID = database.getNextID(dbConn, queryTransCount);
	
		// Request vehicle serial number being sold
		while (true) {
			System.out.print("Please enter the vehicle's serial number: ");
			serialNum = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, querySerialCount, serialNum);
			if (exists == 0) {
				System.out.println("Vehicle not in database");
			} else {
				break;
			}
		}

		// Obtain selling price
		System.out.print("Please enter the selling price: ");
		price = database.requestInt(7, 2);

		// Obtain sale date
		while(true) {
			System.out.print("Please enter sale date in form YYYY-MM-DD: "); 
			sDate = database.requestDate();
			if (currentDate.before(sDate))
				System.out.println("Sale date cannot be later than current date");
			else
				break;
				
		}
		saleDate = new java.sql.Date(sDate.getTime());

		// Asks for number of sellers
		System.out.println("How many people are selling this vehicle? ");
		sellerCount = database.requestInt(3, 0).intValue();
		while (i < sellerCount) {
			if (i == 0) {
				System.out.print("Please enter the primary seller's SIN: ");
			} else {
				System.out.print("Please enter the seller's SIN: ");
			}
			seller = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, querySellerCount, seller);
			if (exists == 0){
				System.out.println("This person does not own the vehicle");
			} else {
				if (i == 0)
					primSeller = seller;
				// Remove person from owning vehicle
				database.removeOwner(dbConn, queryRemoveOwner, serialNum, seller);
				i++;
			}
		}	
		
		// Asks for number of buyers
		System.out.println("How many people are buying this vehicle? ");
		buyerCount = database.requestInt(3, 0).intValue();
		i = 0;
		while (i < buyerCount) {
			if (i == 0) {
				System.out.print("Please enter the primary buyer's SIN: ");
			} else {
				System.out.print("Please enter the buyer's SIN: ");
			}
			buyer = database.requestString(15, maxString, incNum);
			exists = database.checkExistence(dbConn, queryBuyerCount, buyer);
			if (exists == 1){
				System.out.println("This person already owns the vehicle");
			} else {
				exists = database.checkExistence(dbConn, queryPersonCount, buyer);
				if (exists == 0){
					// Person does not exists, add person to database
					NewPeople.addPeople(dbConn, buyer, 1);
				}
				if (i == 0){
					primBuyer = buyer;
					// Add person to owning the vehicle as primary owner
					NewOwner.addOwner(dbConn, serialNum, buyer, "y");
				} else {
					// Add person to owning the vehicle
					NewOwner.addOwner(dbConn, serialNum, buyer, "n");
				}
				i++;
			}
		}

		// Input transaction in auto_sale table
		try {
			addSale = dbConn.prepareStatement(queryNewSale);
			addSale.setInt(1, transID);
			addSale.setString(2, primSeller);
			addSale.setString(3, primBuyer);
			addSale.setString(4, serialNum);
			addSale.setDate(5, saleDate);
			addSale.setDouble(6, price);
			addSale.executeUpdate();
			System.out.println("Transaction added to database");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
	}
}


