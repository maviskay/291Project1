import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;

public class NewPeople{
	// Registers new person
	public static void addPeople(Connection dbConn, String sin) {
		PreparedStatement addPerson;
		Scanner keyboard;
		String name, heightString[], weightString[], eyeColor, hairColor, address, gender, birthday;
		Double height, weight;
		java.util.Date utilDate = new java.util.Date();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date bDate = new java.util.Date();;
		java.sql.Date bDay;
		String queryNewPeople = "INSERT INTO people VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// Requests for name
		while (true) {
			System.out.print("Please enter name of person: ");
		    keyboard = new Scanner(System.in);
			name = keyboard.nextLine();
			if (name.length() > 40)
				System.out.println("Name of person invalid");
			else
			    break;
		}
		// Requests for height
		while (true) {
			try {
				System.out.print("Please enter height of person: ");
				keyboard = new Scanner(System.in);
				height = keyboard.nextDouble();
				heightString = height.toString().split("\\.");
				if (heightString[0].length() > 3 || heightString[1].length() > 2)
					System.out.println("Height invalid");
				else
					break;
			} catch (InputMismatchException e) {
				System.out.println("Invalid option");
			}
		}
		// Requests for weight
		while (true) {
			try {
				System.out.print("Please enter weight of person: ");
				keyboard = new Scanner(System.in);
				weight = keyboard.nextDouble();
				weightString = weight.toString().split("\\.");
				if (weightString[0].length() > 3 || weightString[1].length() > 2)
					System.out.println("Weight invalid");
				else
					break;
			} catch (InputMismatchException e){
				System.out.println("Invalid option");
			}
		}
		// Requests for eye color
		while (true) {
			System.out.print("Please enter eye color of person: ");
		    keyboard = new Scanner(System.in);
			eyeColor = keyboard.nextLine();
			if (eyeColor.length() > 10)
				System.out.println("Eye color invalid");
			else
			    break;
		}
		// Requests for hair color
		while (true) {
			System.out.print("Please enter hair color of person: ");
		    keyboard = new Scanner(System.in);
			hairColor = keyboard.nextLine();
			if (hairColor.length() > 10)
				System.out.println("Hair color invalid");
			else
			    break;
		}
		// Requests for address
		while (true) {
			System.out.print("Please enter address of person: ");
		    keyboard = new Scanner(System.in);
			address = keyboard.nextLine();
			if (address.length() > 50)
				System.out.println("Address invalid");
			else
			    break;
		}
		// Requests for gender
		while (true) {
			System.out.print("Please enter gender of person: ");
		    keyboard = new Scanner(System.in);
			gender = keyboard.nextLine();
			if (gender.length() != 1)
				System.out.println("Gender invalid");
			// TODO: GET THIS TO WORK
			else if ((!gender.contains("f")) || (!gender.contains("m")))
				System.out.println("Gender invalid");
			else
			    break;
		}
		// Requests for birthday
		while (true) {
			System.out.print("Please enter birthday of person (YYYY-MM-DD): ");
		    keyboard = new Scanner(System.in);
			birthday = keyboard.nextLine();
			try{
				formatDate.setLenient(false);
				bDate = formatDate.parse(birthday);
			} catch (ParseException e) {
				System.out.println("Birthday not valid");
				continue;
			}			
			if (bDate.after(utilDate))
				System.out.println("Expiry date cannot be earlier than current date");
			else
				break;
		}
		bDay = new java.sql.Date(bDate.getTime());
		// Inserts person to database
		try {
			addPerson = dbConn.prepareStatement(queryNewPeople);
			addPerson.setString(1, sin);
			addPerson.setString(2, name);
			addPerson.setDouble(3, height);
			addPerson.setDouble(4, weight);
			addPerson.setString(5, eyeColor);
			addPerson.setString(6, hairColor);
			addPerson.setString(7, address);
			addPerson.setString(8, gender);
			addPerson.setDate(9, bDay);
			addPerson.executeUpdate();
			System.out.println("Person added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		//TODO: INSERT DRIVE LICENCE
	}
}
