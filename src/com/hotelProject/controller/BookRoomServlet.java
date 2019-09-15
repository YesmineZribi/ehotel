package com.hotelProject.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Amenity;
import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.DateInterval;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.BRManagement;
import com.hotelProject.model.HotelAndRoomManagement;
import com.hotelProject.model.SearchRooms;

/**
 * Servlet implementation class BookRoomServlet
 */
@WebServlet("/BookRoomServlet")
public class BookRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookRoomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		BRManagement br = new BRManagement();

		
		//Get all the attributes 
		String customerSSN = request.getParameter("customer_ssn");
		String hotelName = request.getParameter("hotel_name");
		String hotelID = request.getParameter("hotel_id");
		String hotelChain = request.getParameter("chain_name");
		String roomNumberString = request.getParameter("room_num");
		int roomNumber = Integer.parseInt(roomNumberString);
		
		String startDateString = request.getParameter("start_date");
		String endDateString = request.getParameter("end_date");
		
		//Prepare message attributes 
		String message, failedMessage; 
		message = failedMessage = "";
		
		//Get customer 
		Customer c = ac.getCustomer(customerSSN);
		
		//Get room selected 
		Room roomSelected = hr.getRoom(hotelID, roomNumber);

		Date start; Date end;
		start = end  = null; 
		//Convert the dates to sql format: 
		java.util.Date startDate = null; java.util.Date endDate = null; 
		
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
			
			if (endDate.before(startDate) || endDate.equals(startDate) || endDate.before(new java.util.Date())) {
				throw new ParseException("Illegal date format",0);
			}
			
			//Convert dates in SQL
			start = new java.sql.Date(startDate.getTime());
			end = new java.sql.Date(endDate.getTime());
			
			//Get all rooms availble btw these two dates
			List<Room> roomsAvail = null;

			roomsAvail = sr.searchRoomsByDate(start, end);

			
			boolean foundRoom = false;
			for (Room r: roomsAvail) {
				if (r.getHotelID().contentEquals(roomSelected.getHotelID()) &&
						r.getRoomNumber() == roomSelected.getRoomNumber()) {
					foundRoom = true; 
				}
			}
			
			if (!foundRoom) {
				throw new ParseException("Illegal date format",0);
			}
			
			
		} catch(ParseException e) {
			e.getStackTrace();
			
			//if illegal date format go back 

			failedMessage = "Illegal or unavailable date please pick different dates";
			
			//Get amenities of the room 
			List<Amenity> amenities = hr.getAmenitiesForRoom(hotelID, roomNumber);
			
			//Get unavailable dates for this room: 
			List<DateInterval> unavailableDates = sr.roomUnavailableDates(hotelID, roomSelected.getRoomNumber());
			
			request.setAttribute("room", roomSelected);
			request.setAttribute("amenities", amenities);
			request.setAttribute("chain_name", hotelChain);
			request.setAttribute("hotel_name", hotelName);
			request.setAttribute("unavailable_dates", unavailableDates);
			request.setAttribute("failed_message", failedMessage);
			request.setAttribute("message", message);
			request.setAttribute("customer_ssn", customerSSN);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("book_room_mirror.jsp");
			dispatcher.forward(request, response);
			
			
			try {
				sr.getConnection().close();
				hr.getConnection().close();
				ac.getConnection().close();
				br.getConnection().close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		} //end of catch 
		
		
		//Create booking ID: -get all bookings and increment max
		List<Booking> allBookings = ac.getAllBookings();
		List<Integer> bookingIDs = new ArrayList<Integer>();
		int bookingID = 0;
		if (allBookings.size() != 0) {
			//Get booking ID
			for (Booking b: allBookings) {
				bookingIDs.add(Integer.parseInt(b.getBookingID()));
			}
			
			//Get biggest booking ID
			int maxBookingID = bookingIDs.get(0);
			for (int i = 0; i < bookingIDs.size(); i++) {
				if (bookingIDs.get(i) > maxBookingID) {
					maxBookingID = bookingIDs.get(i);
				}
			}
			bookingID = maxBookingID + 1;
		}
		
		//Create bookinng 

		Booking newBooking = new Booking(customerSSN, String.valueOf(bookingID), hotelID, roomNumber,start, end, roomSelected.getPrice());
		
		boolean added = br.addBooking(newBooking);
		
		if (!added) {
			failedMessage = "Oh oh something went wrong, could not add booking";
		} else {
			message = "Booking successfully added, we look forward to seeing you!";
			
		}
		
		//redirect back to main page 
		response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

	
		
		try {
			hr.getConnection().close();
			sr.getConnection().close();
			ac.getConnection().close();
			br.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}
