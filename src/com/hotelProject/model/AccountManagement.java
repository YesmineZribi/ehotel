package com.hotelProject.model;
/**
 * This classes handles Customer/Employee sign up, sing in, as well as profile modification 
 * @author Jasmine Z
 *
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Employee;
import com.hotelProject.Classes.Rent; 

public class AccountManagement {
	
	private String url ="jdbc:postgresql://192.168.0.38/postgres";
	private String user = "postgres";
	private String password = "postgres";
	private Connection connection; 
	private Statement stm; 
	
	public AccountManagement() {
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
	
								// -- SIGN UP METHODS  -- //
	/**
	 * Adds an employee to the database
	 * @param em
	 * @return true if the insertion was successful and false otherwise 
	 */	
	public boolean addEmployee(Employee em) {
		//verify password is valid 
		if (em.getPassword().length() < 6) {
			return false;
		}
		
		try {
			//insert the employee first
			String insertEmployee = "INSERT INTO Employee(SSN, FirstName, MiddleName, LastName, StreetNum,"
									+ "StreetName, Province, Country, ZipCode, HotelID, Password) "+
									"VALUES ('"+em.getSsn()+"', '"+em.getFirstName()+"', ";
			
			if (em.getMiddleName() == null) {
				insertEmployee += "NULL, '";
			} else {
				insertEmployee += "'"+em.getMiddleName()+"', '";
			}
			insertEmployee += em.getLastName()+"', '"+em.getAddress().getStreetNum()+"', '"+em.getAddress().getStreetName()+"', '"+
					em.getAddress().getProvince()+"', '"+em.getAddress().getCountry()+"', '"+em.getAddress().getZipCode()+"', '"+
					em.getHotelID()+"', '"+em.getPassword()+"')";
			stm.executeUpdate(insertEmployee);
			
			//insert the employee roles 
			String addRole; 
			for (String role: em.getRoles()) {
				addRole = "INSERT INTO EmployeeRole(SSN, Role) "+
						  "VALUES ('"+em.getSsn()+"', '"+role+"')";
				stm.executeUpdate(addRole);
			}
			
			
						
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Employee added successfully!");
		return true;
	}
	
	
	/**
	 * Adds a customer to the database
	 * @param cs
	 * @return true if the insertion was successful and false otherwise
	 */
	public boolean addCustomer(Customer cs) {
		//verify password is valid 
		if (cs.getPassword().length() < 6) {
			return false;
		}
		
		try {
			String insertCustomer = "INSERT INTO Customer(SSN, FirstName, MiddleName, LastName, StreetNum,"+
									"StreetName, Province, Country, ZipCode, RegistrationDate, Password)"+
									"VALUES ('"+cs.getSsn()+"', '"+cs.getFirstName()+"', ";
			
			if (cs.getMiddleName() == null) {
				insertCustomer += "NULL, '";
			} else {
				insertCustomer += "'"+cs.getMiddleName()+"', '";
			}
			insertCustomer += cs.getLastName()+"', '"+cs.getAddress().getStreetNum()+"', '"+cs.getAddress().getStreetName()+"', '"+
			cs.getAddress().getProvince()+"', '"+cs.getAddress().getCountry()+"', '"+cs.getAddress().getZipCode()+"', '"+
					cs.getRegistrationDate()+"', '"+cs.getPassword()+"')";
			stm.executeUpdate(insertCustomer);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Customer added successfully!");
		return true;
	}
	
							// -- END OF SIGN UP METHODS -- //
	
							// -- LOGIN METHODS -- //
	
	//Have a dropdown list to choose to login as customer or employee
	/**
	 * Verifies the credentials entered by employee are valid
	 * @param ssn
	 * @param password
	 * @return true if the credentials entered by employee are valid and false otherwise 
	 */
	public boolean verifyEmployee(String ssn, String password) {
		try {
			String getCustomer = "SELECT SSN, Password FROM EMPLOYEE WHERE SSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(getCustomer);
			
			String employeeSSN = "";
			String employeePassword = "";
			while(rs.next()) {
				employeeSSN = rs.getString("SSN");
				employeePassword = rs.getString("Password");
			}
			if (employeeSSN.equals("")) {
				System.out.println("Employee does not exist");
				return false;
			}
			if (!employeePassword.equals(password)) {
				System.out.println("Wrong password, please try again!");
				return false; 
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Employee is valid!");
		return true; 		
	}
	
	/**
	 * Return customer object given the ssn 
	 */
	public Employee getEmployee(String ssn) {
		Employee employee; 
		try {
			List<String> roles = new ArrayList<String>();
			//grab employee roles
			String emRoles = "SELECT * FROM EmployeeRole WHERE SSN = '"+ssn+"'";
			ResultSet res = stm.executeQuery(emRoles);
			
			while(res.next()) {
				roles.add(res.getString("Role"));
			}
			
			//grab employee
			String empl = "SELECT * FROM Employee WHERE SSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(empl);
			
			rs.next();
			
			String password = rs.getString("Password");
			String firstName = rs.getString("FirstName");
			String middleName = rs.getString("MiddleName");
			String lastName = rs.getString("LastName");
			int streetNum = rs.getInt("StreetNum");
			String streetName = rs.getString("StreetName");
			String province = rs.getString("Province");
			String country = rs.getString("Country");
			String zipCode = rs.getString("ZipCode");
			String hotelID = rs.getString("HotelID");
			
			employee = new Employee(ssn,firstName,middleName,lastName,new Address(streetNum,streetName,province,country,zipCode),roles,hotelID,password);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return employee; 
	}
	

	//Have a dropdown list to choose to login as customer or employee
	/**
	 * Verifies the credentials entered by customer are valid
	 * @param ssn
	 * @param password
	 * @return true if the credentials entered by customer are valid and false otherwise 
	 */
	public boolean verifyCustomer(String ssn, String password) {
		try {
			String getCustomer = "SELECT SSN, Password FROM Customer WHERE SSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(getCustomer);
			
			String customerSSN = "";
			String customerPassword = "";
			while(rs.next()) {
				customerSSN = rs.getString("SSN");
				customerPassword = rs.getString("Password");
			}
			if (customerSSN.equals("")) {
				System.out.println("Customer does not exist");
				return false;
			}
			if (!customerPassword.equals(password)) {
				System.out.println("Wrong password, please try again!");
				return false; 
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Customer is valid!");
		return true; 
	}
	
	/**
	 * Return customer object given the ssn 
	 */
	public Customer getCustomer(String ssn) {
		Customer customer; 
		try {
			String cus = "SELECT * FROM Customer WHERE SSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(cus);
			rs.next();
			
			String customerPassword = rs.getString("Password");
			String customerFirstName = rs.getString("FirstName");
			String customerMiddleName = rs.getString("MiddleName");
			String customerLastName = rs.getString("LastName");
			int streetNum = rs.getInt("StreetNum");
			String streetName = rs.getString("StreetName");
			String province = rs.getString("Province");
			String country = rs.getString("Country");
			String zipCode = rs.getString("ZipCode");
			Date registrationDate = rs.getDate("RegistrationDate");
			
			customer = new Customer(ssn,customerFirstName, customerMiddleName, customerLastName, new Address(streetNum, streetName,province, country, zipCode),registrationDate, customerPassword);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return customer; 
	}
	
	
							// -- END OF LOGIN METHODS -- //
	
							// -- ACCOUNT DELETION METHODS -- //
	
	/**
	 * Deletes an employee from the database
	 * @param ssn
	 * @return true if the insertion was successful and false otherwise
	 */
	public boolean deleteEmployee(String ssn) {
		try {
			String deleteEmployee = "DELETE FROM Employee WHERE ssn = '"+ssn+"'";
			stm.executeUpdate(deleteEmployee);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Employee deleted successfully");
		return true; 
	}
	
	/**
	 * Deletes a customer from the database
	 * @param ssn
	 * @return true if the deletion was successful and false otherwise
	 */
	public boolean deleteCustomer(String ssn) {
		try {
			String deleteCustomer = "DELETE FROM Customer WHERE ssn = '"+ssn+"'";
			stm.executeUpdate(deleteCustomer);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Customer deleted successfully");
		return true;
	}
						// -- END OF ACCOUNT DELETION METHODS -- //
	
						// -- CUSTOMER PROFILE MODIFICATION METHODS-- //
	/**
	 * Modifies the first name of a customer or employee
	 * @param ssn
	 * @param newName
	 * @return true if the modification was successful and false otherwise
	 */
	public boolean modifyFirstName(String ssn, String newName) {
		//fetch in customer or employer
		try {
			String updateFirstNameCustomer = "UPDATE Customer SET FirstName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameCustomer);
			String updateFirstNameEmployee = "UPDATE Employee SET FirstName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameEmployee);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("First name changed");
		return true;
	}
	
	/**
	 * Modifies the middle name of a customer or employee
	 * @param ssn
	 * @param newName
	 * @return true if the modification was successful and false otherwise
	 */
	public boolean modifyMiddleName(String ssn, String newName) {
		//fetch in customer or employer
		try {
			String updateFirstNameCustomer = "UPDATE Customer SET MiddleName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameCustomer);
			String updateFirstNameEmployee = "UPDATE Employee SET MiddleName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameEmployee);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("First name changed");
		return true;
	}
	
	/**
	 * Modifies the last name of a customer or employee
	 * @param ssn
	 * @param newName
	 * @return true if the modification was successful and false otherwise
	 */
	public boolean modifyLastName(String ssn, String newName) {
		//fetch in customer or employer 
		try {
			String updateFirstNameCustomer = "UPDATE Customer SET LastName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameCustomer);
			String updateFirstNameEmployee = "UPDATE Employee SET LastName = '"+newName+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateFirstNameEmployee);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("First name changed");
		return true;
	}
	
	/**
	 * Modifies the address of a customer 
	 * @param ssn
	 * @param newName
	 * @return true if the modification was successful and false otherwise
	 */
	public boolean modifyAddress(String ssn, Address add) {
		try {
			String updateAddressCustomer = "UPDATE Customer SET StreetNum = '"+add.getStreetNum()+"', StreetName = '"+add.getStreetName()+"', "
					+ "Province = '"+add.getProvince()+"', Country = '"+add.getCountry()+"', ZipCode = '"+add.getZipCode()+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateAddressCustomer);
			String updateAddressEmployee = "UPDATE Employee SET StreetNum = '"+add.getStreetNum()+"', StreetName = '"+add.getStreetName()+"', "
					+ "Province = '"+add.getProvince()+"', Country = '"+add.getCountry()+"', ZipCode = '"+add.getZipCode()+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updateAddressEmployee);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Address changed successfully");
		return true; 
	}
	
	/**
	 * Modifies the password of a customer 
	 * @param ssn
	 * @param newName
	 * @return true if the modification was successful and false otherwise
	 */
	public boolean modifyPassword(String ssn, String newPassword) {
		try {
			String updatePasswordCustomer = "UPDATE Customer SET Password = '"+newPassword+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updatePasswordCustomer);
			String updatePasswordEmployee = "UPDATE Employee SET Password = '"+newPassword+"' WHERE SSN = '"+ssn+"'";
			stm.executeUpdate(updatePasswordEmployee);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false; 
		}
		System.out.println("Password changed successfully");
		return true;
	}
	

						// -- END OF CUSTOMER/EMPLOYEE PROFILE MODIFICATION METHODS -- //
	
						// -- METHODS FOR CUSTOMER PROFILE --//
	//Be able to view their bookings and rents 
	/**
	 * Gets all bookings for a given customer with ssn ssn 
	 * @param ssn
	 * @return the list of bookings for a given customer 
	 */
	public List<Booking> getBookingsForCustomer(String ssn) {
		List<Booking> customerBookings = new ArrayList<Booking>();
		try {
			String customerBkgs = "SELECT * FROM Booking WHERE CustomerSSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(customerBkgs);
			
			String bookingID, hotelID; int roomNumber; Date startDate, endDate; double pricePerNight; 
			Booking b;
			while(rs.next()) {
				bookingID = rs.getString("BookingID");
				hotelID = rs.getString("HotelID");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				pricePerNight = rs.getDouble("PricePerNight");
				b = new Booking(ssn, bookingID, hotelID, roomNumber, startDate, endDate, pricePerNight);
				customerBookings.add(b);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return customerBookings;
	}
	
	/**
	 * Get all the rents for a given customer with ssn ssn
	 * @param ssn
	 * @return the list rents for a given customer 
	 */
	public List<Rent> getRentsForCustomer(String ssn) {
		List<Rent> customerRents = new ArrayList<Rent>();
		try {
			String customerRnt = "SELECT * FROM Rent WHERE CustomerSSN = '"+ssn+"'";
			ResultSet rs = stm.executeQuery(customerRnt);
			
			String bookingID, hotelID, employeeSSN; int roomNumber; Date startDate, endDate; double rentRate, customerBalance; 
			Rent r;
			while(rs.next()) {
				bookingID = rs.getString("rentID");
				hotelID = rs.getString("HotelID");
				employeeSSN = rs.getString("EmployeeSSN");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				rentRate = rs.getDouble("RentRate");
				customerBalance = rs.getDouble("CustomerBalance");
				r = new Rent(ssn, bookingID, employeeSSN, hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);
				customerRents.add(r);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return customerRents;
	}
	
	//Be able to cancel bookings before the start date 
	public boolean cancelBookingForCustomer(String ssn, String bookingID) {
		BRManagement br = new BRManagement();
		boolean cancelled = br.cancelBooking(bookingID);
		return cancelled; 
	}
	
	
						// -- END OF METHODS FOR CUSTOMER PROFILE -- //
	
						// -- METHODS FOR EMPLOYER PROFILE -- //
	/**
	 * Add roles
	 * @param hotelID
	 * @return
	 */
	public boolean addRole(String ssn, String role) {
		try {
			String addRole = "INSERT INTO EmployeeRole (SSN,Role) VALUES ('"+ssn+"', '"+role+"')";
			stm.executeUpdate(addRole);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;		
		} 
		System.out.println("Role successfully added");
		return true;
	}
	
	/**
	 * Delete roles
	 * @param hotelID
	 * @return
	 */
	public boolean removeRole(String ssn, String role) {
		try {
			String removeRole = "DELETE FROM EmployeeRole WHERE SSN = '"+ssn+"' AND Role = '"+role+"'";
			stm.executeUpdate(removeRole);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;		
		} 
		System.out.println("Role successfully deleted");
		return true;
	}
	
	
	//Be able to view all bookings and rents for their hotel 
	public List<Booking> getBookingsForHotel(String hotelID) {
		List<Booking> bookingsForHotel = new ArrayList<Booking>();
		try {
			String bookingsForH = "SELECT * FROM Booking WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(bookingsForH);
			
			String customerSSN,
			bookingID; int roomNumber; Date startDate, endDate; double pricePerNight; 
			Booking b; 
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				bookingID = rs.getString("BookingID");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				pricePerNight = rs.getDouble("PricePerNight");
				
				b = new Booking(customerSSN,bookingID,hotelID,roomNumber,startDate,endDate,pricePerNight);
				bookingsForHotel.add(b);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return bookingsForHotel;
	}
	
	/**
	 * Gets rents for a specific hotel
	 * @param hotelID
	 * @param employeeSSN
	 * @return list of rents for hotel with hotelID 
	 */
	public List<Rent> getRentsForHotel(String employeeSSN) {
		List<Rent> rentsForHotel = new ArrayList<Rent>();
		try {
			String rentsForH = "SELECT * FROM Rent WHERE EmployeeSSN = '"+employeeSSN+"'";
			ResultSet rs = stm.executeQuery(rentsForH);
			
			String customerSSN, rentID, hotelID; int roomNumber; Date startDate, endDate; double rentRate, customerBalance; 
			Rent r; 
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				rentID = rs.getString("rentID");
				hotelID = rs.getString("hotelID");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				rentRate = rs.getDouble("rentRate");
				customerBalance = rs.getDouble("customerBalance");
				r = new Rent(customerSSN, rentID, employeeSSN, hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);
				rentsForHotel.add(r);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return rentsForHotel;
	}
	
	
	/**
	 * Get all bookings - so we have unique ID
	 */
	public List<Booking> getAllBookings() {
		List<Booking> allBookings = new ArrayList<Booking>();
		
		try {
			String bookings = "select * from booking";
			ResultSet rs = stm.executeQuery(bookings);
			
			String customerSSN, bookingID, hotelID; int roomNumber; Date startDate, endDate; 
			double pricePerNight; 
			Booking b;
			
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				bookingID = rs.getString("bookingID");
				hotelID = rs.getString("hotelID");
				roomNumber = rs.getInt("roomNumber");
				startDate = rs.getDate("startDate");
				endDate = rs.getDate("endDate");
				pricePerNight = rs.getDouble("pricePerNight");
				
				b = new Booking(customerSSN, bookingID, hotelID, roomNumber, startDate,endDate, pricePerNight);
				allBookings.add(b);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return allBookings;
	}
	
	/**
	 * Get all rents - so we can have unique ID
	 */
	public List<Rent> getAllRents() {
		List<Rent> allRents = new ArrayList<Rent>();
		try {
			String rents = "SELECT * FROM Rent";
			ResultSet rs = stm.executeQuery(rents);
			
			String customerSSN, employeeSSN, rentID, hotelID; int roomNumber; Date startDate, endDate; double rentRate, customerBalance;
			Rent r; 
			
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				employeeSSN = rs.getString("employeeSSN");
				rentID = rs.getString("rentID");
				hotelID = rs.getString("hotelID");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("endDate");
				rentRate = rs.getDouble("rentRate");
				customerBalance = rs.getDouble("customerBalance");
				
				r = new Rent(customerSSN, rentID, employeeSSN, hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);

				allRents.add(r);
				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return allRents;
	}

	/**
	 * Get all past rents for a specific hotel 
	 * @param hotelID
	 * @return list of all past rends for a specific hotel
	 */
	public List<Rent> getPastRents(String hotelID) { 	//Be able to archive rents with endDate today or passed
		//get rents with end date before today 
		Calendar calendar = Calendar.getInstance();
		
		Date currentDate = new java.sql.Date(calendar.getTime().getTime());
		
		
		List<Rent> pastRents = new ArrayList<Rent>();
		
		try {
			String pastR = "SELECT *  FROM RENT WHERE HotelID = '"+hotelID+"' AND EndDate <= '"+currentDate+"' AND RentRate = CustomerBalance";
			ResultSet rs = stm.executeQuery(pastR);
			
			String customerSSN, rentID, employeeSSN; int roomNumber; Date startDate, endDate; double rentRate, customerBalance; 
			Rent r; 
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				rentID = rs.getString("rentID");
				employeeSSN = rs.getString("EmployeeSSN");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				rentRate = rs.getDouble("rentRate");
				customerBalance = rs.getDouble("customerBalance");
				r = new Rent(customerSSN, rentID, employeeSSN, hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);
				pastRents.add(r);
			}
			
			
		} catch(SQLException e) {
			System.out.print(e.getMessage());
			return null;
			
		}
		return pastRents;
	}
	
	/**
	 * Get all archived bookings for a specific hotel 
	 * @param hotelID
	 * @return list of archived bookings for a specific hotel 
	 */
	public List<Booking> getArchivedBookings(String hotelID) {
		List<Booking> archivedBookings = new ArrayList<Booking>();
		try {
			String archivedB = "SELECT * FROM ArchivedBooking WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(archivedB);
			
			String customerSSN, bookingID; int roomNumber; Date startDate, endDate; double pricePerNight; 
			Booking b; 
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				bookingID = rs.getString("BookingID");
				roomNumber = rs.getInt("RoomNumber");
				startDate = rs.getDate("StartDate");
				endDate = rs.getDate("EndDate");
				pricePerNight = rs.getDouble("PricePerNight");
				b = new Booking(customerSSN, bookingID, hotelID, roomNumber, startDate, endDate, pricePerNight);
				archivedBookings.add(b);
			}			
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return archivedBookings;
	}
	
	/**
	 * Get archived rents for hotel
	 * @param hotelID
	 * @return
	 */
	public List<Rent> getArchivedRents(String hotelID) {
		List<Rent> archivedRents = new ArrayList<Rent>();
		try {
			String archivedR = "SELECT * FROM ArchivedRent WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(archivedR);
			
			String customerSSN, employeeSSN, rentID; int roomNumber; Date startDate, endDate; double rentRate, customerBalance; 
			while(rs.next()) {
				customerSSN = rs.getString("customerSSN");
				employeeSSN = rs.getString("employeeSSN");
				rentID = rs.getString("rentID");
				roomNumber = rs.getInt("roomNumber");
				startDate = rs.getDate("startDate");
				endDate = rs.getDate("endDate");
				rentRate = rs.getDouble("rentRate");
				customerBalance = rs.getDouble("customerBalance");
				Rent archivedRent = new Rent(customerSSN, rentID, employeeSSN, hotelID, roomNumber, startDate, endDate, rentRate, customerBalance);
				archivedRents.add(archivedRent);
			}
 
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return archivedRents; 
	}
	
						// -- END OF METHODS FOR EMPLOYER PROFILE -- //
	
						// -- Utility Methods -- //
	
	
	/**
	 * Get hotel name given ID
	 * @param args
	 */
	public String getHotelName(String hotelID) {
		String hotelName;
		try {
			String hotelN = "SELECT HotelName FROM Hotel WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(hotelN);
			rs.next();
			hotelName = rs.getString("HotelName");
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotelName;
		
	}
	
	public static void main(String[] args) {
//		AccountManagement ac = new AccountManagement();

		
	}
	
	
}
