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
import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class RoomInfoDisplayServlet
 */
@WebServlet("/RoomInfoDisplayServlet")
public class RoomInfoDisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoomInfoDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Display Room Information: 
		String hotelID = request.getParameter("hotel_id");
		String roomNumberString = request.getParameter("room_number");
		String capacityString = request.getParameter("capacity");
		String isExtendedString = request.getParameter("is_extended");
		String priceString = request.getParameter("price");
		String problem = request.getParameter("problem");
		String viewType = request.getParameter("view_type");
		String message = request.getParameter("message");
		String failedMessage = request.getParameter("failed_message");
		
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		Room r = new Room(hotelID, Double.parseDouble(priceString), Integer.parseInt(capacityString), viewType, Boolean.parseBoolean(isExtendedString), problem, Integer.parseInt(roomNumberString));
		
		System.out.println("HOTEL ID: "+hotelID);
		System.out.println("ROOM NUMBER: "+roomNumberString);
		
		//Get Amenities for Room:
		List<Amenity> amenities = hr.getAmenitiesForRoom(hotelID, Integer.parseInt(roomNumberString));
		
		request.setAttribute("room", r);
		request.setAttribute("amenities", amenities);
		request.setAttribute("message", message);
		request.setAttribute("failed_message", failedMessage);
		request.setAttribute("employee_ssn", employeeSSN);
		request.setAttribute("password", password);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_info.jsp");
		
		dispatcher.forward(request, response);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
