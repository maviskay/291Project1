import java.sql.*;

public class Search {

	public static void searchEngine(Connection dbConn) {
	PreparedStatement DriverByName, DriverByLNo, TicketsBySin; 
	PreparedStatement TicketsByLNo, VehicleHist;
	ResutlSet driver, tickets, history;
	Scanner keyboard;

	String queryDriverByName = "SELECT p.name, l.licence_no, p.addr, p.birthday,l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND p.name = ?";

	String queryDriverByLNo = "SELECT p.name, l.licence_no, p.addr, p.birthday,l.class, r.r_id, l.expiring_date FROM people p, drive_licence l, restriction r WHERE p.sin = l.sin AND l.licence_no = r.licence_no AND l.licence_no = ?";

	String queryTicketsBySin = "SLEECT * FROM ticket WHERE violator_no = ?";

	String queryTicketsByLNo = "SLEECT t.* FROM ticket t, drive_licence l WHERE l.sin = t.violator_no AND l.licence_no = ?";

	String queryVehicleHist = "SELECT v.serial_no, COUNT(DISTINCT s.transaction_id), AVG(s.price), COUNT(DISTINCT t.ticket_no) FROM vehicle v, auto_sale s, ticket t WHERE s.vehicle_id (+)= ? AND t.vehicle_id (+)= ? GROUP BY v.serial_no ORDER BY v.serial_no";

	}
	
}
