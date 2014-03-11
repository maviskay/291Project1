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
		PreparedStatement checkSerial, checkSeller, sellers, checkSaleCount;
		ResultSet serialCount, sellerCount, allSellers, saleCount;
		String serialNum, seller, inputDate;
		int numBuyers, transID;
		double price;
		Date saleDate = null;
		Scanner keyboard;
		java.util.Date currentDate = new java.util.Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

		String querySerialCount = "SELECT COUNT(serial_no) FROM vehicle WHERE serial_no = ?";

		String querySellerCount = "SELECT COUNT(owner_id) FROM owner WHERE owner_id = ?";

		String querySellers = "SELECT owner_id FROM owner WHERE vehicle_id = ?";
		String queryTransCount = "SELECT COUNT(*) FROM auto_sale";

		System.out.println("You have selected auto transaction");
	
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
			System.out.print("Please enter the vehicle's serial numberL ");

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
				saleDate = formatDate.parse(inputDate);
			} catch (ParseException e) {
				System.out.println("Invalid sale date");
				continue;
			}
			if (currentDate.before(saleDate))
				System.out.println("Sale date cannot be later than current date");
			else
				break;
				
		}

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
	/*	
		// TODO Find or insert buyer information
		while(true) {
			System.out.print("Please enter the number of buyers: ");

			keyboard = new Scanner(System.in);
			numBuyers = keyboard.nextInt();
			if (numBuyers <= 0) 
				System.out.println("There must be at least one buyer");
			else {
				try {
					for (int i = 0; i < numBuyers; i++) {
						
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}			
		}*/
	}
}
