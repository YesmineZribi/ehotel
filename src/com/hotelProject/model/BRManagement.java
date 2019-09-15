package com.hotelProject.model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Rent;

/**
 * This class deals with customer bookings, rates, their creation cancellation,
 * archive etc..
 * @author Yesmine Z
 *
 */
public class BRManagement {
	
	private String url ="jdbc:postgresql://192.168.0.38/postgres";
	private String user = "postgres";
	private String password = "postgres";
	private Connection connection; 
	private Statement stm; 
	
	public BRManagement() {
		//Connect to database
		try {
			
			connection = DriverManager.getConnection(url,user, password);
			System.out.println("Connected to the PostgreSQL server successfully");
			stm = connection.createStatement();
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} 		
	}
	
	public Connection getConnection() {
		return connection; 
	}
	
	/**
	 * Adds a booking to the db
	 * @param b
	 * @return true if the insertion was successful and false otherwise
	 */
	public boolean addBooking(Booking b) {
		try {
			String insertBooking = "INSERT INTO BOOKING(CustomerSSN, BookingID, HotelID, RoomNumber, StartDate, EndDate, PricePerNight) "+
							"VALUES ('"+b.getCustomerSSN()+"', '"+b.getBookingID()+"', '"+b.getHotelID()+"', '"+
							b.getRoomNumber()+"', '"+b.getStartDate()+"', '"+b.getEndDate()+"', '"+b.getPricePerNight()+"')";
			stm.executeUpdate(insertBooking);
			connection.close();
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Booking added successfully");
		return true;
	}	
	
	/**
	 * Cancels a booking provided the current date is before the start date
	 * @param bookingID
	 * @return true if the cancellation was successfull and false otherwise 
	 */
	public boolean cancelBooking(String bookingID) {
		try {
			//First select the start date 
			String bookingDate = "SELECT StartDate FROM Booking WHERE BookingID = '"+bookingID+"'";
			
			ResultSet rs = stm.executeQuery(bookingDate);
			rs.next(); //position in the first row
			Date startDate = rs.getDate("StartDate");
			Calendar calendar = Calendar.getInstance();
			
			Date currentDate = new java.sql.Date(calendar.getTime().getTime());
			
			//Compare the start date to the current date 
			if (startDate.after(currentDate)) {
				String deleteBooking = "DELETE FROM Booking WHERE BookingID = '"+bookingID+"'";
				stm.executeUpdate(deleteBooking);
			} else {
				System.out.println("Cannot cancel booking, too late...");
				return false;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Booking cancelled successfully!");
		return true;
	}
	
	/**
	 * Archives a booking
	 * @param b
	 * @return true if the insertion  was successful and false otherwise
	 */
	public boolean archiveBooking(Booking b) {
		try {
			//Delete the booking first
			stm.executeUpdate("DELETE FROM Booking WHERE CustomerSSN = '"+b.getCustomerSSN()+"' AND BookingID = '"+b.getBookingID()+"'");
			
			String insertArchivedBooking = "INSERT INTO ArchivedBooking(CustomerSSN, BookingID, HotelID, RoomNumber,"
											+ "StartDate, EndDate, PricePerNight)"
											+ "VALUES('"+b.getCustomerSSN()+"', '"+b.getBookingID()+"', '"+b.getHotelID()+"', '"+
											b.getRoomNumber()+"', '"+b.getStartDate()+"', '"+b.getEndDate()+"', '"+b.getPricePerNight()+"')";
			stm.executeUpdate(insertArchivedBooking);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Archived successfully!");
		return true; 
	}
	
	/**
	 * Adds a rent to the db
	 * @param r
	 * @return true if the insertion was successful and false otherwise 
	 */
	public boolean addRent(Rent r) {
		try {
			String insertRent = "INSERT INTO Rent(CustomerSSN, RentID, EmployeeSSN, HotelID, RoomNumber, StartDate,"+
								"EndDate, RentRate, CustomerBalance)"+
								"VALUES('"+r.getCustomerSSN()+"', '"+r.getRentID()+"', '"+r.getEmployeeSSN()+"', '"+
								r.getHotelID()+"', '"+r.getRoomNumber()+"', '"+r.getStartDate()+"', '"+r.getEndDate()+"', '"+
								r.getRentRate()+"', '"+r.getCustomerBalance()+"')";
			stm.executeUpdate(insertRent);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Rent added successfully!");
		return true;
	}
	
	/**
	 * Deletes a rent from the db
	 * @param rentID
	 * @return true if the deletion was successful and false otherwise 
	 */
	public boolean deleteRent(String rentID) {
		try {
			String deleteRent = "DELETE FROM RENT WHERE rentid = '"+rentID+"'";
			stm.executeUpdate(deleteRent);

			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Rent delete successfully!");
		return true;
	}	

	/**
	 * Archives a rent
	 * @param r 
	 * @return true if the insertion/deletion was successful and false otherwise
	 */
	public boolean archiveRent(String customerSSN, String rentID) {
		try {
			//Find the rent 
			ResultSet rs = stm.executeQuery("SELECT * FROM Rent WHERE CustomerSSN = '"+customerSSN+"' AND RentID = '"+rentID+"'");
			
			rs.next();
			String hotelID = rs.getString("HotelID");
			int roomNumber = rs.getInt("RoomNumber");
			Date startDate = rs.getDate("StartDate");
			Date endDate = rs.getDate("endDate");
			double rentRate = rs.getDouble("RentRate");
			String employeeSSN = rs.getString("EmployeeSSN");
			double customerBalance = rs.getDouble("customerBalance");
			
			Rent r = new Rent(customerSSN, rentID, employeeSSN,hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);
			//Add it to the archived table 
			String insertArchivedRent = "INSERT INTO ArchivedRent(CustomerSSN, RentID, HotelID, RoomNumber,"+
										"StartDate, EndDate, RentRate, EmployeeSSN, customerBalance)"
										+ "VALUES ('"+r.getCustomerSSN()+"', '"+r.getRentID()+"', '"+r.getHotelID()+"', '"+
										r.getRoomNumber()+"', '"+r.getStartDate()+"', '"+r.getEndDate()+"', '"+r.getRentRate()+"', '"
												+r.getEmployeeSSN()+"', '"+r.getCustomerBalance()+"')";
			stm.executeUpdate(insertArchivedRent);
			
			//Delete the rent from rent 
			stm.executeUpdate("DELETE FROM Rent WHERE CustomerSSN = '"+r.getCustomerSSN()+"' AND RentID = '"+r.getRentID()+"'");

			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Rent archived successfully");
		return true;
	}
	
	/**
	 * Converts a booking to a rent (only done by Employees)
	 * @param bookingID
	 * @return true  if the convertion was successful and false otherwise 
	 */
	public boolean convertBooking(String employeeSSN, String customerSSN,String bookingID) {
		//take the bookingID and use to become rentID 
		try { 
			//grab the booking
			String booking = "SELECT CustomerSSN,BookingID,HotelID,RoomNumber,StartDate, EndDate, PricePerNight "
					+ "FROM Booking WHERE CustomerSSN = '"+customerSSN+"' AND BookingID = '"+bookingID+"'";
			ResultSet rs = stm.executeQuery(booking);
			
			rs.next(); //grab the row 
			String hotelID = rs.getString("HotelID");
			int roomNum = rs.getInt("RoomNumber");
			Date startDate = rs.getDate("StartDate");
			Date endDate = rs.getDate("EndDate");
			double rentRate = rs.getDouble("PricePerNight");
			
			//archive the booking 
			Booking b = new Booking(customerSSN, bookingID, hotelID, roomNum, startDate, endDate, rentRate);
			archiveBooking(b);
			
			//Grab all the rents and take the max and increment to create rentID 
			AccountManagement ac = new AccountManagement();
			List<Rent> rents = ac.getAllRents();
			List<Integer> rentIDs = new ArrayList<Integer>();
			int rentID = 0;
			if (rents.size() != 0) {
				//Get rent ID; 
				for (Rent re: rents) {
					rentIDs.add(Integer.parseInt(re.getRentID()));
				}
				
				//Get biggest rentID 
				int maxRentID = rentIDs.get(0);
				for (int i = 0; i < rentIDs.size(); i++) {
					if (rentIDs.get(i) > maxRentID) {
						maxRentID = rentIDs.get(i);
					}
				}
				rentID = maxRentID+1;				
			}

			
			//create the rent
			Rent r = new Rent(customerSSN, String.valueOf(rentID), employeeSSN, hotelID, roomNum, startDate, endDate, rentRate, 0.0);
			addRent(r); //add the rent
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Booking converted into rent!");
		return true;
	}
	
	
	/**
	 * Updates the customer balance with amount
	 * @param customerSSN
	 * @param rentID
	 * @param amount
	 * @return true if the update was successful and false otherwise 
	 */
	public boolean payRent(String customerSSN, String rentID, double amount) {
		if (amount <= 0) {
			return false; 
		}
		try {
			String updateBalance = "UPDATE Rent SET CustomerBalance = CustomerBalance+'"+amount+"' WHERE CustomerSSN = '"+customerSSN+"' AND "
					+ "RentID = '"+rentID+"'";
			stm.executeUpdate(updateBalance);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Amount added to your balance");
		return true;
		
	}
	

	
	public static void main(String[] args) {
//		BRManagement br = new BRManagement();
		
	}
	
	
	
	
	
	
}
