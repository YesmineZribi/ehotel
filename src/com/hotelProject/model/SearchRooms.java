package com.hotelProject.model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.DateInterval;
import com.hotelProject.Classes.Rent;
import com.hotelProject.Classes.Room;

/**
 * This class hosts the methods used to search rooms with different criteria
 * @author Yesmine Z
 *
 */
public class SearchRooms {
	
	private String url ="jdbc:postgresql://192.168.0.38/postgres";
	private String user = "postgres";
	private String password = "postgres";
	private Connection connection; 
	private Statement stm; 

	public SearchRooms() {
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
	 * Get rooms by hotelID
	 * @param hotelID
	 * @return
	 */
	public List<Room> getRoomsForHotel(String hotelID) {
		List<Room> rooms = new ArrayList<Room>();
		try {
			String r = "SELECT * FROM Room WHERE HotelID = '"+hotelID+"'";
			ResultSet rs = stm.executeQuery(r);
			
			int roomNumber, capacity; double price; 
			String problem, viewType; boolean isExtended; 
			while(rs.next()) {
				roomNumber = rs.getInt("roomNumber");
				capacity = rs.getInt("capacity");
				price = rs.getDouble("price");
				problem = rs.getString("problem");
				viewType = rs.getString("viewtype");
				isExtended = rs.getBoolean("isExtended");
				Room roo = new Room(hotelID, price, capacity, viewType,isExtended, problem,roomNumber);
				rooms.add(roo);
				
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return rooms;
	}
	
	/**
	 * Returns bookings for a given room
	 * @param hotelID
	 * @param roomNum
	 * @return list of bookings for a given room with hotelID and roomNum
	 */
	public List<Booking> getBookingsForRoom(String hotelID, int roomNum) {
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			String bookingsForRoom = "SELECT * FROM Booking WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNum+"'";
			ResultSet rs = stm.executeQuery(bookingsForRoom);
			
			while(rs.next()) {
				Booking b = new Booking(rs.getString("CustomerSSN"), rs.getString("BookingID"), rs.getString("HotelID"), rs.getInt("RoomNumber"),
						rs.getDate("StartDate"), rs.getDate("EndDate"), rs.getDouble("PricePerNight"));
				bookings.add(b);
			}

		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		System.out.println("Bookings for hotel "+hotelID+" room "+roomNum+" returned!");
		return bookings; 
	}
	
	/**
	 * Returns rents for a given room
	 * @param hotelID
	 * @param roomNum
	 * @return list of rents for a given room with hotelID and roomNum
	 */
	public List<Rent> getRentsForRoom(String hotelID, int roomNum) {
		List<Rent> rents = new ArrayList<Rent>();
		try {
			String rentsForRooms = "SELECT * FROM Rent WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNum+"'";
			ResultSet rs = stm.executeQuery(rentsForRooms);
			
			while(rs.next()) {
				Rent r = new Rent(rs.getString("CustomerSSN"), rs.getString("RentID"), rs.getString("EmployeeSSN"), rs.getString("HotelID"), rs.getInt("RoomNumber"),
						rs.getDate("StartDate"), rs.getDate("EndDate"), rs.getDouble("RentRate"), rs.getDouble("CustomerBalance"));
				rents.add(r);
			}
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		System.out.println("Rents for hotel "+hotelID+" room "+roomNum+" returned!");
		return rents; 		
	}
	
	/**
	 * Grabs all rooms available within the interval of startDate and endDate
	 * @param startDate
	 * @param endDate
	 * @return all rooms available btw startDate and endDate 
	 */
	//Grab rooms that are free btw startDate and endDate
//	public List<Room> searchRoomsByDate(Date startDate, Date endDate) {
//		//look at all the bookings and all the rents and return the ones available on the start and end date interval mentioned
//		List<Room> availableRooms = new ArrayList<Room>();
//		List<Room> allRoomsList = new ArrayList<Room>();
//		try {
//			//1. Grab all the rooms for all hotels
//			String allRooms = "SELECT * FROM Room";
//			ResultSet rs = stm.executeQuery(allRooms);
//		 	
//			//put all rooms in a list 
//			while(rs.next()) {
//				Room room = new Room(rs.getString("HotelID"), rs.getDouble("Price"), rs.getInt("Capacity"), rs.getString("ViewType"), rs.getBoolean("isExtended"),
//						rs.getString("Problem"), rs.getInt("RoomNumber"));
//				
//				allRoomsList.add(room);
//			}
//			
//			String roomBooking;
//			Date sDate, eDate; 
//			ResultSet brs;
//			boolean add;
//			//2. Iterate over all the rooms: for each room grab all the bookings 
//			for (Room r : allRoomsList) {
//				add = true;
//				for (Booking b : getBookingsForRoom(r.getHotelID(), r.getRoomNumber())) {
//					sDate = b.getStartDate(); eDate = b.getEndDate();
//					if ((eDate.compareTo(startDate) > 0 && eDate.compareTo(endDate) <= 0) || (sDate.compareTo(endDate) < 0 && sDate.compareTo(startDate) >= 0) ||
//					startDate.compareTo(sDate) >= 0 && endDate.compareTo(eDate) <= 0) {
//						add = false;
//						break; //no need to check the other bookings no availability 					
//				
//					}
//				}
//				
//				if (add) { //if we did not find overlapping bookings run this loop 
//					for (Rent rent : getRentsForRoom(r.getHotelID(), r.getRoomNumber())) {
//						sDate = rent.getStartDate(); eDate = rent.getEndDate();
//						if ((eDate.compareTo(startDate) > 0 && eDate.compareTo(endDate) <= 0) || (sDate.compareTo(endDate) < 0 && sDate.compareTo(startDate) >= 0) ||
//						startDate.compareTo(sDate) >= 0 && endDate.compareTo(eDate) <= 0) {
//							add = false;
//							break; //no need to check the other bookings no availability 					
//					
//						}					
//					}		
//				}
//				//4. Create room object and add
//				if (add) { //if we did not find overlapping rents we are good to go 
//					availableRooms.add(r);
//				}
//			}
//			
//			System.out.println(Arrays.toString(availableRooms.toArray()));
//			System.out.println(availableRooms.size());
//
//			
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//			return null;
//		}
//		System.out.println("Rooms returned");
//		return availableRooms; 
//	}
	
	/**
	 * Get room given hotelID and roomNumber
	 */
	public Room getRoom(String hotelID, int roomNumber) {
		Room room; 
		try {
			String getRoom = "SELECT * from room where hotelID = '"+hotelID+"' and roomNumber = '"+roomNumber+"'";
			ResultSet rs = stm.executeQuery(getRoom);
			
			String problem, viewType;
			double price; int capacity ; boolean isExtended;
			
			rs.next();
			problem = rs.getString("Problem");
			price = rs.getDouble("price");
			capacity = rs.getInt("capacity");
			isExtended = rs.getBoolean("isExtended");
			viewType = rs.getString("ViewType");
			room = new Room(hotelID, price, capacity, viewType,isExtended, problem,roomNumber);
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return room;
	}
	/**
	 * Search rooms by date v2
	 * @throws ParseException 
	 */
	public List<Room> searchRoomsByDate(Date sDate, Date eDate) throws ParseException {
		List<Room> availableRooms = new ArrayList<Room>();
		List<Room> temp = new ArrayList<Room>();
		
		try {
			
			String availRooms = "select hotelID, roomNumber from room \r\n" + 
					"except\r\n" + 
					"((select booking.hotelID, booking.roomNumber from booking\r\n" + 
					"where (booking.startdate between '"+sDate+"' and '"+eDate+"') or (booking.enddate between '"+sDate+"' and '"+eDate+"'))\r\n" + 
					"union\r\n" + 
					"(select rent.hotelID, rent.roomNumber from rent\r\n" + 
					"where (rent.startdate between '"+sDate+"' and '"+eDate+"') or (rent.enddate between '"+sDate+"' and '"+eDate+"'))\r\n" + 
					");";
			
			ResultSet rs = stm.executeQuery(availRooms);
			
			
			String hotelID; int roomNumber; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				roomNumber = rs.getInt("RoomNumber");
				
				Room r = new Room(hotelID, roomNumber);
				temp.add(r);

			}
			
			for (Room ro: temp) {
				Room room = getRoom(ro.getHotelID(), ro.getRoomNumber());
				availableRooms.add(room);
			}
			
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return availableRooms;
	}
	
	/**
	 * Returns rooms with capacity range btw minCapacity and maxCapacity 
	 * @param minCapacity 
	 * @param maxCapacity
	 * @return list of rooms with capacity capacity 
	 */
	public List<Room> searchRoomsByCapacity(int minCapacity, int maxCapacity) {
		if (minCapacity < 0 || maxCapacity < 0 || minCapacity > maxCapacity ) {
			return null;
		}
		
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomsWithCapacity = "SELECT * FROM Room WHERE Capacity >= '"+minCapacity+"' AND Capacity <= '"+maxCapacity+"'";
			ResultSet rs = stm.executeQuery(roomsWithCapacity);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;
	}
	
	/**
	 * Returns rooms within price range minPrice and maxPrice
	 * @param minPrice
	 * @param maxPrice
	 * @return list of rooms within price range minPrice and maxPrice
	 */
	public List<Room> searchRoomsByPrice(double minPrice, double maxPrice) {
		if (minPrice > maxPrice) {
			throw new IllegalStateException("incorrect minPrice or maxPrice argument");

		}
		
		List<Room> resultRooms = new ArrayList<Room> (); 
		try {
			String roomsWithPrice = "SELECT * FROM Room WHERE Price >= '"+minPrice+"' AND Price <= '"+maxPrice+"'";
			ResultSet rs = stm.executeQuery(roomsWithPrice);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}
			
		} catch(SQLException e) {
			System.out.print(e.getMessage());
			return null;
		}
		return resultRooms;
	}
	
	/**
	 * Returns rooms with view type viewType
	 * @param viewType
	 * @return list of rooms with view type viewType
	 */
	public List<Room> searchRoomsByView(String type) {
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomWithView = "SELECT * FROM Room WHERE ViewType = '"+type+"'";
			ResultSet rs = stm.executeQuery(roomWithView);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;
	}
	
	public List<String> getAllRoomsAreas() {
		List<String> areas = new ArrayList<String>();
		try {
			String allAreas = "SELECT Province FROM Hotel";
			ResultSet rs = stm.executeQuery(allAreas);
			
			while(rs.next()) {
				String province = rs.getString("Province");
				areas.add(province);
			}

			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return areas; 
	}
	
	/**
	 * Returns rooms in hotels in area area
	 * @param area
	 * @return list of rooms in hotels in area rea 
	 */
	public List<Room> searchRoomsByArea(String area) {
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomInArea = "SELECT hotelID, price, capacity, viewType, isExtended, problem,"
					+ "roomNumber FROM room natural join hotel WHERE Province = '"+area+"'";
			ResultSet rs = stm.executeQuery(roomInArea);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;
	}
	
	/**
	 * Gives all rooms in hotelChainName's hotel hotelName
	 * @param hotelChainName
	 * @param hotelName
	 * @return a list of rooms in hotelChainName's hotel hotelName
	 */
	public List<Room> searchRoomsByHotel(String hotelChainName, String hotelName) {
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomsInHotel = "SELECT HotelID, Price, Capacity, ViewType, IsExtended, Problem, RoomNumber "
					+ "FROM Room natural join Hotel WHERE HotelName = '"+hotelName+"' AND ChainName = '"+hotelChainName+"'";
			ResultSet rs = stm.executeQuery(roomsInHotel);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;
		
	}
		
	/**
	 * Returns rooms in hotels that belong to the hotel chain hotelChainName
	 * @param hotelChainName
	 * @return list of rooms in hotels that belong to the hotel chain hotelChainName
	 */
	public List<Room> searchRoomsByHotelChain(String hotelChainName) {
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomsInHotel = "SELECT HotelID, Price, Capacity, ViewType, IsExtended, Problem, RoomNumber "
					+ "FROM Room natural join Hotel WHERE ChainName = '"+hotelChainName+"'";
			ResultSet rs = stm.executeQuery(roomsInHotel);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;		
	}
	
	/**
	 * Returns true if the room belongs to a hotel with that chain name
	 * @param r
	 * @param hotelChainName
	 * @return
	 */
	public boolean roomInChain(Room r, String hotelChainName) {
		try {
			String belongs = "SELECT * FROM Room natural join Hotel WHERE HotelID = '"+r.getHotelID()+"' "+
								"and RoomNumber = '"+r.getRoomNumber()+"' AND ChainName = '"+hotelChainName+"'";
			ResultSet rs = stm.executeQuery(belongs);
			
			if (rs.next() == false) {
				return false; 
			} 
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false; 
		}
		return true;
		
	}
	
	/**
	 * Return hotel chain of a room 
	 * @param r
	 * @return
	 */
	public String roomHotelChain(Room r) {
		String hc = null;
		try {
			String hotelChain = "SELECT ChainName FROM Room natural join hotel WHERE HotelID = '"+r.getHotelID()+"' "+
					"and roomNumber = '"+r.getRoomNumber()+"'";
			ResultSet rs = stm.executeQuery(hotelChain);
			
			rs.next();
			
			hc = rs.getString("ChainName");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hc; 
	}
	
	/**
	 * Return rooms that belong to hotel with category category
	 * @param category
	 * @return
	 */
	public List<Room> searchRoomsByCategory(int category) {
		List<Room> resultRooms = new ArrayList<Room>();
		
		try {
			String roomsInHotel="SELECT HotelID, Price, Capacity, ViewType, IsExtended, Problem, RoomNumber "
					+ "FROM Room natural join Hotel WHERE Category = '"+category+"'";
			ResultSet rs = stm.executeQuery(roomsInHotel);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;	
	}
	
	/**
	 * Return rooms that belong to hotels with numofRooms
	 * @param numOfRooms
	 * @return
	 */
	public List<Room> searchRoomsByNumOfRoomsInHotel(int numOfRooms) {
		List<Room> resultRooms = new ArrayList<Room>();
		try {
			String roomsInHotel = "SELECT HotelID, Price, Capacity, ViewType, IsExtended, Problem, RoomNumber "
					+ "FROM Room natural join Hotel WHERE numofrooms = '"+numOfRooms+"'";
			ResultSet rs = stm.executeQuery(roomsInHotel);
			
			String hotelID, viewType, problem;
			double price; int capacity, roomNumber; boolean isExtended; 
			Room r; 
			while(rs.next()) {
				hotelID = rs.getString("HotelID");
				price = rs.getDouble("Price");
				capacity = rs.getInt("Capacity");
				viewType = rs.getString("ViewType");
				problem = rs.getString("Problem");
				roomNumber = rs.getInt("RoomNumber");
				isExtended = rs.getBoolean("IsExtended");
				r = new Room(hotelID, price, capacity, viewType, isExtended, problem, roomNumber);
				resultRooms.add(r);
			}	
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return resultRooms;	
	}
	
	/**
	 * Return the number of rooms the room's hotel has
	 * @param r
	 * @return
	 */
	public int roomHotelNumOfRooms(Room r) {
		int numOfRooms = 0;
		try {
			String num = "SELECT numofrooms FROM Room natural join Hotel WHERE HotelID = '"+r.getHotelID()+"' "+
							"and RoomNumber = '"+r.getRoomNumber()+"'";
			ResultSet rs = stm.executeQuery(num);
			rs.next();
			
			numOfRooms = rs.getInt("NumofRooms");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
		return numOfRooms; 
		
	}
	
	public int getMaxNumOfRoom() {
		int maximum = 0;
		try {
			String maxNum = "select max(numofrooms) from hotel";
			ResultSet rs = stm.executeQuery(maxNum);
			rs.next();
			
			maximum = rs.getInt("max");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return maximum;
		}
		return maximum;
	}
	
	 
	/**
	 * Returns all date intervals unavailable for a given room - will be used to update calendar
	 * @param hotelID
	 * @param roomNumber
	 * @return a list of dateIntervals during which the room roomNumber in hotelID is unavailable 
	 */
	public List<DateInterval> roomUnavailableDates(String hotelID, int roomNumber) {
		List<DateInterval> unavailableDates = new ArrayList<DateInterval>();
		try {
			String roomBookingDates = "SELECT StartDate, EndDate FROM Room natural join Booking WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			String roomRentDates = "SELECT StartDate, EndDate FROM Room natural join Rent WHERE HotelID = '"+hotelID+"' AND RoomNumber = '"+roomNumber+"'";
			ResultSet rs1 = stm.executeQuery(roomBookingDates);
			
			DateInterval dateInterval; 
			while(rs1.next()) {
				dateInterval = new DateInterval(rs1.getDate("StartDate"), rs1.getDate("EndDate"));
				unavailableDates.add(dateInterval);
			}
			
			ResultSet rs2 = stm.executeQuery(roomRentDates);
			while(rs2.next()) {
				dateInterval = new DateInterval(rs2.getDate("StartDate"), rs2.getDate("EndDate"));
				unavailableDates.add(dateInterval);				
			}
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return unavailableDates;
	}
	
	/**
	 * Get all hotel chain names
	 * @param args
	 * @throws ParseException
	 */
	
	public List<String> getHotelChainNames() {
		List<String> hotelChains = new ArrayList<String>();
		try {
			String getHotelChains = "select chainname from hotelchain";
			ResultSet rs = stm.executeQuery(getHotelChains);
			
			while(rs.next()) {
				String chainName = rs.getString("chainname");
				hotelChains.add(chainName);
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotelChains;
	}
	
	/**
	 * Return province of the hotel of room r
	 * @param r
	 * @return
	 */
	public String roomHotelProvince(Room r) {
		String province = null;
		try {
			String roomProve = "SELECT Province FROM Room NATURAL JOIN Hotel WHERE HotelID = '"+r.getHotelID()+"' "+
							"AND RoomNumber = '"+r.getRoomNumber()+"'";
			ResultSet rs = stm.executeQuery(roomProve);
			rs.next();
			
			province = rs.getString("province");
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return province; 
	}
	
	/**
	 * Return hotel names of room r
	 * @param r
	 * @return
	 */
	public String roomHotelName(Room r) {
		String hotelName = null;
		
		try {
			String roomHotName = "select hotelName from room natural join hotel where hotelID = '"+r.getHotelID()+"' and "
					+ "roomNumber = '"+r.getRoomNumber()+"'";
			ResultSet rs = stm.executeQuery(roomHotName);
			
			rs.next();
			hotelName = rs.getString("hotelName");
					
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return hotelName;
	}
	

	public static void main(String[] args) throws ParseException {
		SearchRooms sr = new SearchRooms();
		
		Room r = new Room("206388",2);
		
		System.out.println(sr.roomHotelName(r));
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
