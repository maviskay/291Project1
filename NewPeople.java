import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;

public class NewPeople {
	// Registers new person
	public static void addPeople(Connection dbConn, String sin, int requestLicence) {
		int maxString = 1, varString = 0, noNum = 1, incNum = 0;
		String name, eyeColor, hairColor, address, gender;
		Double height, weight;
		java.util.Date birthday = new java.util.Date();
		java.util.Date currDate = new java.util.Date();
		java.sql.Date bDate;
		String queryNewPeople = "INSERT INTO people VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement addPerson;

		// Requests for name
		System.out.print("Please enter name of person: ");
		name = database.requestString(40, varString, noNum);
		
		// Requests for height
		System.out.print("Please enter height of person: ");
		height = database.requestInt(3, 2);
		
		// Requests for weight
		System.out.print("Please enter weight of person: ");
		weight = database.requestInt(3, 2);
		
		// Requests for eye color
		System.out.print("Please enter eye color of person: ");
		eyeColor = database.requestString(10, varString, noNum);
		
		// Requests for hair color
		System.out.print("Please enter hair color of person: ");
		hairColor = database.requestString(10, varString, noNum);
		
		// Requests for address
		System.out.print("Please enter address of person: ");
		address = database.requestString(50, varString, incNum);
		
		// Requests for gender
		while (true) {
			System.out.print("Please enter gender of person: ");
			gender = database.requestString(1, maxString, noNum);
			if ((gender.equalsIgnoreCase("f")) || (gender.equals("m")))
				break;
			else
				System.out.println("Gender invalid");
		}
		
		// Requests for birthday
		while (true) {
			System.out.print("Please enter birthday of person (YYYY-MM-DD): ");
			birthday = database.requestDate();
			if (birthday.after(currDate))
				System.out.println("Birthday cannot be in the future");
			else
				break;
		}
		bDate = new java.sql.Date(birthday.getTime());
		
		// Insert person to database
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
			addPerson.setDate(9, bDate);
			addPerson.executeUpdate();
			System.out.println("Person added to the database");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Checks if person has licence
		if (requestLicence == 1)
			NewLicence.ownsLicence(dbConn, sin);
	}
}



