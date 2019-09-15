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
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.hotelProject.Classes.*;

/**
 * This class handles addition/update/delete of hotels and rooms 
 * @author Jasmine Z
 *
 */
public class HotelAndRoomManagement {

	private String url ="jdbc:postgresql://192.168.0.38/postgres";
	private String user = "postgres";
	private String password = "postgres";
	private Connection connection; 
	private Statement stm; 
	
	public HotelAndRoomManagement( ) {
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
	
				// --HOTEL CHAIN METHODS -- //
	public List<HotelChain> getAllHotelChains() {
		List<HotelChain> chains = new ArrayList<HotelChain>();
		
		try {
			String getAllChains = "select * from hotelChain";
			ResultSet rs = stm.executeQuery(getAllChains);
			
			
			while(rs.next()) {
				HotelChain chain = new HotelChain(rs.getString("chainName"),rs.getInt("numofhotels"));
				chains.add(chain);
				
			}
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return chains; 
	}
	
	public List<Hotel> getAllChainHotels(String chainName) {
		List<Hotel> hotels = new ArrayList<Hotel>();
		
		try {
			String hots = "select * from hotel where chainName = '"+chainName+"'";
			ResultSet rs = stm.executeQuery(hots);
			
			String hotelID, hotelName; 
			int streetNum;
			String streetName, province, country, zipCode, managerSSN;
			Date startDate; 
			int numOfRooms, category; 
			
			while(rs.next()) {
				hotelID = rs.getString("hotelID");
				hotelName = rs.getString("hotelName");
				streetNum = rs.getInt("streetNum");
				streetName = rs.getString("streetName");
				province = rs.getString("province");
				country = rs.getString("country");
				zipCode = rs.getString("zipCode");
				managerSSN = rs.getString("managerSSN");
				startDate = rs.getDate("startDate");
				numOfRooms = rs.getInt("numOfRooms");
				category = rs.getInt("category");
				
				Hotel hotel = new Hotel(chainName, hotelID, hotelName, new Address(streetNum, streetName, province, country, zipCode),null,null,managerSSN,startDate, numOfRooms,category);
				hotels.add(hotel);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotels; 
		
	}
	
	public List<String> chainEmails(String chainName) {
		List<String> emails = new ArrayList<String>();
		
		try {
			String chainEmails = "select email from chainEmail where chainName = '"+chainName+"'";
			ResultSet rs = stm.executeQuery(chainEmails);
			
			String email;
			while(rs.next()) {
				email = rs.getString("email");
				
				emails.add(email);
			}
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return emails;
		
	}
	
	public List<String> chainPhones(String chainName) {
		List<String> phones = new ArrayList<String>();
		
		try {
			String chainPhones = "select phoneNumber from chainPhone where chainName = '"+chainName+"'";
			ResultSet rs = stm.executeQuery(chainPhones);
			
			String phone; 
			while(rs.next()) {
				phone = rs.getString("phoneNumber");
				
				phones.add(phone);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return phones; 
	}
	
	public HotelChain getHotelChain(String chainName) {
		HotelChain hotelChain = null;
		try {
			String chain = "select * from hotelChain where ChainName = '"+chainName+"'";
			ResultSet rs = stm.executeQuery(chain);
			
			rs.next();
			
			int numOfHotels = rs.getInt("numOfHotels");
			
			hotelChain = new HotelChain(chainName, numOfHotels);
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return hotelChain;
	}
	
	public List<CentralOffice> getOffices(String chainName) {
		List<CentralOffice> offices = new ArrayList<CentralOffice>();
		
		try {
			String chainOffices = "SELECT * FROM CentralOfficeAddress WHERE ChainName = '"+chainName+"'";
			ResultSet rs = stm.executeQuery(chainOffices);
			
			String  streetName, province, country, zipCode;
			int streetNum;
			CentralOffice o;
			while(rs.next()) {
				streetName= rs.getString("streetName");
				province = rs.getString("province");
				country = rs.getString("country");
				zipCode = rs.getString("zipCode");
				streetNum = rs.getInt("streetNum");
				o = new CentralOffice(new Address(streetNum, streetName, province, country, zipCode), chainName);
				offices.add(o);
			}
			
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return offices; 
	}
	
				// -- HOTEL METHODS --//
	/**
	 * Adds a hotel to the database
	 * @param hotel 
	 * @return true if the insertion was successful and false otherwise 
	 */
	public boolean addHotel(Hotel hotel) {
		try {
			//Insert the hotel first 
			String insertHotel = "INSERT INTO Hotel(HotelID, ChainName, HotelName, StreetNum, StreetName, "
													+ "Province, Country, ManagerSSN, StartDate, NumOfRooms, ZipCode, Category) "+
								  "VALUES ('"+hotel.getHotelID()+"', '"+hotel.getHotelChainName()+"', '"+
										hotel.getHotelName()+"', '"+hotel.getAddress().getStreetNum()+"', '"+
										hotel.getAddress().getStreetName()+"', '"+hotel.getAddress().getProvince()+"', '"+
										hotel.getAddress().getCountry()+"', ";
			
			if (hotel.getManagerSSN() == null && hotel.getStartDate() == null) {
				insertHotel += "NULL, NULL, '";
			} else {
				insertHotel += "'"+hotel.getManagerSSN()+"', '"+hotel.getStartDate()+"', '";
			}
			insertHotel += hotel.getNumOfRooms()+"', '"+hotel.getAddress().getZipCode()+"', '"+hotel.getCategory()+"')";
										
			stm.executeUpdate(insertHotel);
			
			if (hotel.getEmails() != null) {
				//insert the emails 
				String addEmail;
				for (String email: hotel.getEmails()) {
					addEmail = "INSERT INTO HotelEmail(HotelID, Email) "+
								"VALUES ('"+hotel.getHotelID()+"', '"+email+"')";
					stm.executeUpdate(addEmail);
				}				
			}

			
			if (hotel.getPhones() != null) {
				//insert the phone numbers 
				String addPhone; 
				for (String phone : hotel.getPhones()) {
					addPhone = "INSERT INTO HotelPhone(HotelID, PhoneNumber) "+
								"VALUES ('"+hotel.getHotelID()+"', '"+phone+"')";
					stm.executeUpdate(addPhone);
				}
			}
											
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel added successfully!");
		return true;
		
	}	
	
	/**
	 * Get Hotel given hotelID
	 */
	public Hotel getHotel(String hotelID) {
		Hotel hotel = null; 
		try {
			String getHotel = "SELECT * FROM Hotel  WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(getHotel);
			
			String hotelName, hotelChainName, managerSSN, streetName, province, country, zipCode; int category, numOfRooms; Date managerStartDate; 
			int streetNum;
			rs.next();
			hotelName = rs.getString("hotelName");
			hotelChainName = rs.getString("ChainName");
			managerSSN = rs.getString("managerSSN");
			category = rs.getInt("category");
			numOfRooms = rs.getInt("numOfRooms");
			managerStartDate = rs.getDate("startDate");
			streetNum = rs.getInt("streetNum");
			streetName = rs.getString("streetName");
			province = rs.getString("province");
			country = rs.getString("country");
			zipCode = rs.getString("zipCode");

			
			//Get email info
			String getHotelEmails = "SELECT * FROM HotelEmail WHERE HotelID = '"+hotelID+"'";
			ResultSet rs1 = stm.executeQuery(getHotelEmails);
			List<String> emails  = new ArrayList<String>(); 
			
			while(rs1.next()) {
				String email = rs1.getString("email");
				emails.add(email);
			}

			
			//Get phone info 
			String getHotelPhones = "SELECT * FROM HotelPhone WHERE HotelID = '"+hotelID+"'";
			ResultSet rs2 = stm.executeQuery(getHotelPhones);
			List<String> phones = new ArrayList<String>();
			
			while(rs2.next()) {
				String phone = rs2.getString("phonenumber");
				phones.add(phone);
			}

			
			 hotel = new Hotel(hotelChainName, hotelID, hotelName, new Address(streetNum, streetName, province, country, zipCode),phones, emails, managerSSN, managerStartDate, numOfRooms, category);
			 
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotel; 
	}
	
	/**
	 * Deletes a hotel from the database
	 * @param chainName
	 * @param hotelID
	 * @return true if the deletion was successful and false otherwise
	 */
	public boolean deleteHotel(String hotelID) {
		try {
			String deleteHotel = "DELETE FROM Hotel WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(deleteHotel);
			
			
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			return false;
		}
		System.out.println("Hotel deleted successfully");
		return true;
	}
	
	/**
	 * Get all Hotel IDs
	 */
	public List<String> getAllHotelIDs() {
		List<String> hotelIDs = new ArrayList<String>();
		
		try {
			String hotels = "select hotelID from hotel";
			ResultSet rs = stm.executeQuery(hotels);
			
			while(rs.next()) {
				String hotelID = rs.getString("hotelID");
				hotelIDs.add(hotelID);
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			return null;
		}
		return hotelIDs;
	}
	
	/**
	 * Modify hotel chain for a given hotel
	 */
	public boolean modifyHotelChain(String hotelID, String hotelChain) {
		try {
			String updateHotelChain = "UPDATE Hotel SET ChainName = '"+hotelChain+"' WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateHotelChain);
			
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel chain updated successfully");
		return true;
	}
	
	/**
	 * Modify hotel name for a given hotel 
	 */
	public boolean modifyHotelName(String hotelID, String hotelName) {
		try {
			String updateHotelName = "UPDATE Hotel SET HotelName = '"+hotelName+"' WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateHotelName);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel name updated successfully");
		return true;		
	}
	
	/**
	 * Modify hotel address 
	 */
	
	public Address getHotelAddress(String hotelID) {
		Address hotelAddress = null;
		try {
			String hotelAdd = "SELECT StreetNum, StreetName, Province, Country, ZipCode from Hotel where HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(hotelAdd);
			
			int streetNum; String streetName, province, country, zipCode;
			rs.next();
			streetNum = rs.getInt("streetNum");
			streetName = rs.getString("streetName");
			province = rs.getString("province");
			country = rs.getString("country");
			zipCode = rs.getString("zipCode");
			
			hotelAddress = new Address(streetNum, streetName, province, country, zipCode);

			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotelAddress; 
	}
	/**
	 * Modify hotel name for a given hotel 
	 */
	public boolean modifyHotelAddress(String hotelID, Address address) {
		try {
			String updateHotelAddress = "UPDATE Hotel SET StreetNum = '"+address.getStreetNum()+"', StreetName = '"+address.getStreetName()+"',"
					+ "Province = '"+address.getProvince()+"', Country = '"+address.getCountry()+"', ZipCode = '"+address.getZipCode()+"'"
					+ " WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateHotelAddress);
			

			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel address updated successfully");
		return true;		
	}
	
	
	/**
	 * Modify hotel manager for a given hotel
	 */
	
	public boolean modifyHotelManager(String hotelID, String managerSSN) {
		try {
			String updateHotelManager = "UPDATE Hotel SET ManagerSSN = '"+managerSSN+"'"
					+ " WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateHotelManager);
			

			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel manager updated successfully");
		return true;		
	}
	
	/**
	 * Modify hotel manager start date
	 */
	public boolean modifyHotelManagerStartDate(String hotelID, java.sql.Date startDate) {
		try {
			String updateStartDate = "UPDATE Hotel SET StartDate = '"+startDate+"'"+
		" WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateStartDate);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel start date updated successfully");
		return true;	
	}
	
	/**
	 * Modify room Number
	 */
	public boolean modifyRoomNumber(String hotelID, int oldRoomNumber, int newRoomNumber) {
		try {
			String modifyRoomNum = "UPDATE Room Set roomNumber = '"+newRoomNumber+"' WHERE HotelID = '"+hotelID+"' AND roomNumber = '"+oldRoomNumber+"'";
			stm.executeUpdate(modifyRoomNum);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel Room Number updated successfully");
		return true;	
	}
	
	/**
	 * Modify hotel category 
	 */
	public boolean modifyHotelCategory(String hotelID, int category) {
		try {
			String updateHotelCategory = "UPDATE Hotel SET category = '"+category+"'"
					+ " WHERE HotelID = '"+hotelID+"'";
			stm.executeUpdate(updateHotelCategory);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel category updated successfully");
		return true;			
	}
	
	/**
	 * Add Hotel Email
	 */
	public boolean addHotelEmail(String hotelID, String email) {
		try {
			String addEmail = "INSERT INTO HotelEmail Values('"+hotelID+"', '"+email+"')";
			stm.executeUpdate(addEmail);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel Email added successfully");
		return true;	
	}
	
	/**
	 * Delete Hotel Email
	 */
	public boolean deleteHotelEmail(String hotelID, String email) {
		try {
			String deleteEmail = "DELETE FROM HotelEmail WHERE HotelID = '"+hotelID+"' AND Email = '"+email+"'";
			stm.executeUpdate(deleteEmail);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel Email deleted successfully");
		return true;
	}
	
	/**
	 * Add phone number
	 */
	public boolean addHotelPhone(String hotelID, String phone) {
		try {
			String addEmail = "INSERT INTO HotelPhone Values('"+hotelID+"', '"+phone+"')";
			stm.executeUpdate(addEmail);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel phone added successfully");
		return true;		
	}
	
	/**
	 * Delete phone number
	 */
	public boolean deleteHotelPhone(String hotelID, String phone) {
		try {
			String deleteEmail = "DELETE FROM HotelPhone WHERE HotelID = '"+hotelID+"' AND phoneNumber = '"+phone+"'";
			stm.executeUpdate(deleteEmail);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Hotel Phone deleted successfully");
		return true;		
	}
	
	/**
	 * Returns the number of rooms of each hotel 
	 * @param hotelID
	 * @return
	 */
	public int getHotelNumberOfRooms(String hotelID) {
		int numOfRooms = 0; 
		try {
			String hotelNumberOfRooms = "SELECT count(*) as num_of_rooms from room where hotelID ='"+hotelID+"'";
			ResultSet rs = stm.executeQuery(hotelNumberOfRooms);
			rs.next();
			numOfRooms = rs.getInt("num_of_rooms");
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return numOfRooms;
		}
		return numOfRooms; 
	}
	
	/**
	 * Get rooms of hotel 
	 */
	public List<Room> getRoomsForHotel(String hotelID) {
		List<Room> roomsForHotel = new ArrayList<Room>();
		try {
			String rooms = "SELECT * FROM Room WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(rooms);
			
			int capacity, roomNumber; boolean isExtended; double price; 
			String viewType, problem;
			
			while(rs.next()) {
				capacity = rs.getInt("capacity");
				roomNumber = rs.getInt("roomNumber");
				isExtended = rs.getBoolean("isExtended");
				price = rs.getDouble("price");
				viewType = rs.getString("viewType");
				problem = rs.getString("problem");
				
				Room r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				roomsForHotel.add(r);
			}
			
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return roomsForHotel;
	}
	
			// -- ROOM METHODS --//
	
	/**
	 * Adds a room to the db
	 * @param r
	 * @return true if the insertion was successful and false otherwise
	 */
	public boolean addRoom(Room r) {
		try {
			
			String insertRoom = "INSERT INTO ROOM (HotelID, Price, Capacity, ViewType, isExtended, Problem, RoomNumber)"
					+ "VALUES ('"+r.getHotelID()+"', '"+r.getPrice()+"', '"+r.getCapacity()+"', '"+r.getViewType()+"', '"+r.getIsExtended()+"', '"+
					r.getProblem()+"', '"+r.getRoomNumber()+"')";
			stm.executeUpdate(insertRoom);
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room added");
		return true;
	}
	
	
	
	/**
	 * Delete hotel room 
	 */
	public boolean deleteRoom(String hotelID, int roomNumber) {
		try {
			String deleteRoom = "DELETE FROM Room WHERE HotelID = '"+hotelID+"' AND roomNumber = '"+roomNumber+"'";
			stm.executeUpdate(deleteRoom);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room deleted succesfully");
		return true; 
	}
	
	/**
	 * Get room 
	 */
	public Room getRoom(String hotelID, int roomNumber) {
		Room room = null; 
		try {
			String getRoom = "SELECT * FROM Room WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			ResultSet rs = stm.executeQuery(getRoom);
			
			int capacity; boolean isExtended; double price; String problem, viewType; 
			rs.next();
			capacity = rs.getInt("capacity");
			isExtended = rs.getBoolean("isExtended");
			price = rs.getDouble("price");
			problem = rs.getString("problem");
			viewType = rs.getString("viewType");
			
			room = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return room; 
	}
	
	/**
	 * Modify price
	 */
	
	public boolean modifyRoomPrice(String hotelID, int roomNumber, double price) {
		try {
			String updateRoomPrice = "UPDATE Room SET Price = '"+price+"' WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			stm.executeUpdate(updateRoomPrice);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room price updated successfully");
		return true;
	}
	
	/**
	 * Modify capacity 
	 */
	public boolean modifyRoomCapacity(String hotelID, int roomNumber, int capacity) {
		try {
			String updateRoomCapacity = "UPDATE Room SET Capacity = '"+capacity+"' WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			stm.executeUpdate(updateRoomCapacity);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room capacity updated successfully");
		return true;
	}
	
	
	/**
	 * Modify view type
	 */
	public boolean modifyRoomView(String hotelID, int roomNumber, String view) {
		try {
			String updateRoomView = "UPDATE Room SET ViewType = '"+view+"' WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			stm.executeUpdate(updateRoomView);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room view updated successfully");
		return true; 
	}
	
	/**
	 * Modify whether it can be extended or not
	 */
	public boolean modifyRoomExtend(String hotelID, int roomNumber, boolean isExtended) {
		try {
			String updateRoomExtend = "UPDATE Room SET IsExtended = '"+isExtended+"' WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			stm.executeUpdate(updateRoomExtend);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room extension updated successfully");
		return true;		
	}
	
	/**
	 * Modify problems
	 */
	public boolean modifyRoomProblems(String hotelID, int roomNumber, String problems) {
		try {
			String updateRoomProb = "UPDATE Room SET Problem = '"+problems+"' WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			stm.executeUpdate(updateRoomProb);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Room problems updated successfully");
		return true;			
	}
	
	/**
	 * Get Amenities for a room
	 */
	public List<Amenity> getAmenitiesForRoom(String hotelID, int roomNumber) {
		List<Amenity> amenities = new ArrayList<Amenity>();
		try {
			String getAmenities = "SELECT type, price from Amenity where hotelID = '"+hotelID+"' and roomNumber = '"+roomNumber+"'";
			ResultSet rs = stm.executeQuery(getAmenities);
			
			String type; double price; 
			while(rs.next()) {
				type = rs.getString("type");
				price = rs.getDouble("price");
				Amenity a = new Amenity(type,hotelID, roomNumber, price);
				amenities.add(a);
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		return amenities; 
	}
	
	
	/**
	 * Add amenity 
	 */
	public boolean addAmenity(Amenity amenity) {
		try {
			String addAmenity = "INSERT INTO Amenity (Type, HotelID, RoomNumber, Price)"
					+ "VALUES ('"+amenity.getType()+"', '"+amenity.getHotelID()+"', '"+amenity.getRoomNumber()+"', '"+
					amenity.getPrice()+"')";
			
			stm.executeUpdate(addAmenity);
			

			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Amenity added succesfully");
		return true;
	}
	
	
	/**
	 * Remove amenity 
	 */
	public boolean deleteAmenity(String hotelID, int roomNumber, String type) {
		try {
			String deleteAmenity = "DELETE FROM Amenity WHERE HotelID = '"+hotelID+"' AND "
					+ "RoomNumber = '"+roomNumber+"' AND Type = '"+type+"'";
			
			stm.executeUpdate(deleteAmenity);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.out.println("Amenity deleted succesfully");
		return true;		
	}
	
	public List<Room> getAllRooms() {
		List<Room> allRooms = new ArrayList<Room>();
		
		try {
			String rooms = "select * from room";
			ResultSet rs = stm.executeQuery(rooms);
			
			String hotelID, problem, viewType; 
			int capacity, roomNumber; double price; 
			boolean isExtended; 
			
			while(rs.next()) {
				hotelID = rs.getString("hotelID");
				problem = rs.getString("problem");
				viewType = rs.getString("viewType");
				capacity = rs.getInt("capacity");
				roomNumber = rs.getInt("roomNumber");
				price = rs.getDouble("price");
				isExtended = rs.getBoolean("isExtended");
				
				Room r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				allRooms.add(r);
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return allRooms; 
	}
	
	
	public List<NumOfRoomsPerArea> getView1() {
		List<NumOfRoomsPerArea> view1 = new ArrayList<NumOfRoomsPerArea>();
		
		try {
			String getView1 = "select * from numofroomsperarea";
			
			ResultSet rs = stm.executeQuery(getView1);
			
			String province; int numOfRooms; 
			NumOfRoomsPerArea numPerArea; 
			while(rs.next()) {
				province = rs.getString("province");
				numOfRooms = rs.getInt("numofroomsperarea");
				numPerArea = new NumOfRoomsPerArea(province,numOfRooms);
				view1.add(numPerArea);
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return view1;
		
	}
	
	public List<RoomCapacity> getView2() {
		List<RoomCapacity> roomCap = new ArrayList<RoomCapacity>();
		
		try {
			String getView2 = "select * from roomcapacity";
			ResultSet rs = stm.executeQuery(getView2);
			
			
			String hotelID; int roomNumber, capacity; 
			RoomCapacity rc;
			while(rs.next()) {
				hotelID = rs.getString("hotelID");
				roomNumber = rs.getInt("roomNumber");
				capacity = rs.getInt("capacity");
				
				rc = new RoomCapacity(hotelID,roomNumber,capacity);
				roomCap.add(rc);
				
			}
			
		
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return roomCap; 
	}
	
	
	public static void main(String[] args) {
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		System.out.println(hr.getAllHotelIDs());

	}
	
	
	
	
	
	
	
	
	
	
	
	
}
