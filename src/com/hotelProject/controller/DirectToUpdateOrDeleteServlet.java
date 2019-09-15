package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class DirectToUpdateOrDeleteServlet
 */
@WebServlet("/DirectToUpdateOrDeleteServlet")
public class DirectToUpdateOrDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectToUpdateOrDeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Check which button was pressed 
		String hotelID = request.getParameter("hotel_id");
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		//Get all rooms within this hotel 
		List<Room> hotelRooms = hr.getRoomsForHotel(hotelID);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Determine which update button was pressed if any
		Room roomToUpdate = null; 
		//Determine which delete button was pressed if any 
		Room roomToDelete = null;
		for (Room r : hotelRooms) {
			if (request.getParameter(r.getRoomNumber()+"-update") != null ) {
				roomToUpdate = r;
				break;
			}
			if (request.getParameter(r.getRoomNumber()+"-delete") != null) {
				roomToDelete = r;
				break;
			}
			
		}
		
		
		//if roomToUpdate is null no update button was pressed
		if (roomToUpdate != null) {
			//Call update method and pass it the room to update
			System.out.println("The room to update is "+roomToUpdate.getRoomNumber());
			updateRoom(hotelID, roomToUpdate,employeeSSN, password, request,response);
			
			//return
			return;
		}
		
		//if roomToDelete is null no delete button was pressed 
		if (roomToDelete != null) {
			//Call delete method and pass it the room to delete 
			System.out.println("The room to delete is "+roomToDelete.getRoomNumber());
			deleteRoom(hotelID, roomToDelete.getRoomNumber(),employeeSSN, password, request,response);
			
			//return 
			return;
		}
		
	}
	
	/**
	 * Redirect to page to update room / view amenities 
	 * @param hotelID
	 * @param room
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void updateRoom(String hotelID, Room room, String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Redirect to DisplayRoomServlet 
		response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+hotelID+"&room_number="+room.getRoomNumber()+"&capacity="+room.getCapacity()+"&is_extended="+room.getIsExtended()+"&price="+room.getPrice()+"&problem="+room.getProblem()+"&view_type="+room.getViewType()+"&employee_ssn="+employeeSSN+"&password="+password);
	}
	
	/**
	 * Delete room 
	 * @param hotelID
	 * @param room
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void deleteRoom(String hotelID, int roomNumber, String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Delete room from DB 
		boolean deleted = hr.deleteRoom(hotelID, roomNumber);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if(!deleted) {
			failedMessage = "Oh oh, something went wrong, could not delete room";
			
		} else {
			message = "Room successfully deleted!";
		}
		
		//Redirect to DisplayHandRServlet
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
