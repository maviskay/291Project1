import java.util.InputMismatchException;
import java.util.Scanner;


public class NewVehicle {

	public static void vehicleRegistration() {
		Scanner keyboard;
		String serialNum, maker, model, color;
		int year, type;
		System.out.println("You have selected new vehicle registration");
		while (true) {
		    System.out.print("Please enter the vehicle's serial number: ");
			keyboard = new Scanner(System.in);
			serialNum = keyboard.nextLine();
			if (serialNum.length() != 15)
				System.out.println("Serial number invalid");
			else
			    // TODO: Check serial num before next input request
				break;
		}
		while (true) {
			System.out.print("Please enter make of vehicle: ");
		        keyboard = new Scanner(System.in);
			maker = keyboard.nextLine();
			if (maker.length() > 20)
				System.out.println("Make of vehicle invalid");
			else
			    break;
		}
		while (true) {
		    System.out.print("Please enter model of vehicle: ");
		    keyboard = new Scanner(System.in);
		    model = keyboard.nextLine();
		    if (model.length() > 20)
				System.out.println("Model of vehicle invalid");
		    else
				break;
		}
		while (true) {
		    System.out.print("Please enter year of vehicle: ");
		    keyboard = new Scanner(System.in);
			try{
			    year = keyboard.nextInt();
				if (Integer.toString(year).length() != 4)
					System.out.println("Year of vehicle invalid");
				else
					break;
			} catch (InputMismatchException e) {
			    System.out.println("Invalid year");
			    continue;
			}
		}
		while (true) {
			System.out.print("Please enter color of vehicle: ");
			keyboard = new Scanner(System.in);
		    color = keyboard.nextLine();
		    if (color.length() > 10)
				System.out.println("Color of vehicle invalid");
		    else
				break;
		}
		// Assume all vehicle types are already in database

	}

}
