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

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Hotel;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class DisplayHandRServlet
 */
@WebServlet("/DisplayHandRServlet")
public class DisplayHandRServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayHandRServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		String hotelID = request.getParameter("hotel_id");
		String message = request.getParameter("message");
		String failedMessage = request.getParameter("failed_message");
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		//Grab hotel information 
		Hotel hotel = hr.getHotel(hotelID);

		
		//Grab hotel address
		Address hotelAddress = hr.getHotelAddress(hotelID);
		
		
		//Grab all the rooms of that hotel 
		List<Room> hotelRooms = hr.getRoomsForHotel(hotelID);
		
		hotel.setNumOfRooms(hotelRooms.size());

			
		//Send all that info to hotel_rooms_info.jsp
		request.setAttribute("message", message);
		request.setAttribute("failed_message", failedMessage);
		request.setAttribute("hotel", hotel);
		request.setAttribute("hotel_rooms", hotelRooms);
		request.setAttribute("address", hotelAddress);
		request.setAttribute("employee_ssn", employeeSSN);
		request.setAttribute("password", password);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("hotel_rooms_info.jsp");
		dispatcher.forward(request, response);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
