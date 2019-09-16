package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class AddRoomServlet
 */
@WebServlet("/AddRoomServlet")
public class AddRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ipAddress = "http://192.168.0.38";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRoomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Get the fields 
		String hotelID = request.getParameter("hotel_id");
		int roomNumber = Integer.parseInt(request.getParameter("room_number"));
		int capacity = Integer.parseInt(request.getParameter("capacity"));
		String viewTypeString = request.getParameter("view_type");
		
		String viewType; 
		if (viewTypeString.contentEquals("Mountain View")) {
			viewType = "MountainView";
		} else {
			viewType = "SeaView";
		}
		
		boolean isExtended = Boolean.parseBoolean(request.getParameter("is_extended"));
		String problem = request.getParameter("problem");
		double price = Double.parseDouble(request.getParameter("price"));
		
		//Create room object
		Room roomToAdd = new Room(hotelID,price,capacity,viewType,isExtended,problem,roomNumber);
		
		
		//Add room to the DB 
		boolean added = hr.addRoom(roomToAdd);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!added) {
			failedMessage = "Either the room alread exists or your input is invalid, please try again";
		} else {
			message = "Room added successfully!";
		}
		
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		//redirect to DisplayHandRServlet
		response.sendRedirect("http://"+ipAddress+":8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}
