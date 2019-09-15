package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Amenity;
import com.hotelProject.Classes.DateInterval;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;
import com.hotelProject.model.SearchRooms;

/**
 * Servlet implementation class DisplayRoomSearchServlet
 */
@WebServlet("/DisplayRoomSearchServlet")
public class DisplayRoomSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayRoomSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String customerSSN = request.getParameter("customer_ssn");
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		SearchRooms sr = new SearchRooms();
		//Figure out which button was pressed 
		
		//1. Get all rooms 
		List<Room> allRooms = hr.getAllRooms();
		
		//find the one which hotelID-roomNumber-view is not null
		Room roomSelected = null;
		String buttonClicked; 
		for (Room r: allRooms) {
			buttonClicked = request.getParameter(r.getHotelID()+"-"+r.getRoomNumber()+"-view");
			if (buttonClicked != null) {
				roomSelected = r; 
			}
		}
		
		
		//Get the amenities of the room 
		List<Amenity> roomAmenities = hr.getAmenitiesForRoom(roomSelected.getHotelID(), roomSelected.getRoomNumber());
		
		//Get hotel chain name of the room 
		String hotelChainName = sr.roomHotelChain(roomSelected);
		
		//Get hotel name of the room 
		String hotelName = sr.roomHotelName(roomSelected);
		
		//Get all the dates this room is unavailable in 
		List<DateInterval> unavailableDates = sr.roomUnavailableDates(roomSelected.getHotelID(), roomSelected.getRoomNumber());
		
		request.setAttribute("room", roomSelected);
		request.setAttribute("amenities", roomAmenities);
		request.setAttribute("chain_name", hotelChainName);
		request.setAttribute("hotel_name", hotelName);
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("unavailable_dates", unavailableDates);
		
		//dispatch to jsp 
		RequestDispatcher dispatcher = request.getRequestDispatcher("book_room.jsp");
		
		dispatcher.forward(request, response);
		
		//close connection 
		try {
			hr.getConnection().close();
			sr.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}



}
