package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Amenity;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class UpdateRoomServlet
 */
@WebServlet("/UpdateRoomServlet")
public class UpdateRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateRoomServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//CHECK IF UPDATE BUTTON WAS PRESSED 
		String updateRoomButton = request.getParameter("update-room");
		String hotelID = request.getParameter("hotel_id");
		String oldRoomNumberString = request.getParameter("old_room_number");
		int oldRoomNumber = Integer.parseInt(oldRoomNumberString);
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		System.out.println("Hotel id is :"+hotelID);
		System.out.println("Old room number is : "+oldRoomNumber);
		
		if (updateRoomButton != null) {
			//Call method to update room info 
			System.out.println("Update button was pressed");
			updateRoom(hotelID,oldRoomNumber,employeeSSN,password,request,response);
			
			//return 
			return;
		}
		
		//CHECK IF DELETE BUTTON WAS PRESSED 
		
		//Get the list of amenities for this room 
		List<Amenity> amenities = hr.getAmenitiesForRoom(hotelID, oldRoomNumber);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Amenity amenityToDelete = null; 
		
		for (Amenity a: amenities) {
			if (request.getParameter(a.getType()+"-delete") != null) {
				amenityToDelete = a;
			}
		}
		
		//If amenityToDelete is null no delete buttons were pressed
		if (amenityToDelete != null) {
			//Call method to delete that amenity and pass it the amenity
			System.out.println("Amenity to delete is "+amenityToDelete.getType());
			deleteAmenity(amenityToDelete, employeeSSN,password,request, response);
			
			//return 
			return;
		}
		
		//CHECK IF ADD BUTTON WAS PRESSED 
		String addAmenityButton = request.getParameter("add-amenity");
		if (addAmenityButton != null) {
			//Call method to add amenity 
			System.out.println("Add Amenity button was pressed");
			addAmenity(hotelID, oldRoomNumber, employeeSSN, password,request, response);
			//return 
			return;
		}
		
	}
	
	/**
	 * Update room
	 * @param hotelID
	 * @param oldRoomNumber
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void updateRoom(String hotelID, int oldRoomNumber, String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Get all the fields
		String newRoomNumberString = request.getParameter("new_room_number");
		int newRoomNumber = Integer.parseInt(newRoomNumberString);
		int capacity = Integer.parseInt(request.getParameter("capacity"));
		boolean isExtended = Boolean.parseBoolean(request.getParameter("is_extended"));
		String viewTypeString = request.getParameter("view_type");
		String viewType;

		if (viewTypeString.contentEquals("Sea View")) {
			viewType = "SeaView";
		} else {
			viewType = "MountainView";
		}
		
		double price = Double.parseDouble(request.getParameter("price"));
		String problem = request.getParameter("problem");
		
		String message, failedMessage; 
		message = failedMessage = "";
		//Update the fields in the DB
		boolean updatedRoomNum = hr.modifyRoomNumber(hotelID, oldRoomNumber, newRoomNumber);
		if (!updatedRoomNum) {
			failedMessage = "The room number you are trying to input already exists";
			response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+hotelID+"&room_number="+oldRoomNumber+"&capacity="+capacity+"&is_extended="+isExtended+"&price="+price+"&problem="+problem+"&view_type="+viewType+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return;
		}
		
		boolean updatedCapacity = hr.modifyRoomCapacity(hotelID, newRoomNumber, capacity);
		boolean updatedIsExtended = hr.modifyRoomExtend(hotelID, newRoomNumber, isExtended);
		boolean updatedViewType = hr.modifyRoomView(hotelID, newRoomNumber, viewType);
		boolean updatedPrice = hr.modifyRoomPrice(hotelID, newRoomNumber, price);
		boolean updatedProblem = hr.modifyRoomProblems(hotelID, newRoomNumber, problem);
		

		if (!updatedRoomNum || !updatedCapacity || !updatedIsExtended || !updatedViewType ||
				! updatedPrice || !updatedProblem) {
			failedMessage = "One or more inputted fields could not be updated successfully, please check your input and resubmit again!";
		} else {
			message = "Fields updated successfully!";
		}
		
		//Send back to RoomInfoDisplayServlet
		response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+hotelID+"&room_number="+oldRoomNumber+"&capacity="+capacity+"&is_extended="+isExtended+"&price="+price+"&problem="+problem+"&view_type="+viewType+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Delete Amenity
	 * @param amenity
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void deleteAmenity(Amenity amenity, String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Delete Amenity from DB 
		boolean deleted = hr.deleteAmenity(amenity.getHotelID(), amenity.getRoomNumber(), amenity.getType());
		
		String message, failedMessage; 
		message = failedMessage = "";
		if (!deleted) {
			failedMessage = "Oh oh something went wrong, could not delete amenity";
		} else {
			message = "Amenity deleted successfully!";
		}
		
		//Get room from room number of amenity 
		Room room = hr.getRoom(amenity.getHotelID(), amenity.getRoomNumber());
		
		//Redirect to DisplayRoomInfoServlet 
		response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+room.getHotelID()+"&room_number="+room.getRoomNumber()+"&capacity="+room.getCapacity()+"&is_extended="+room.getIsExtended()+"&price="+room.getPrice()+"&problem="+room.getProblem()+"&view_type="+room.getViewType()+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	protected void addAmenity(String hotelID, int roomNumber,String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Get the fields
		String type= request.getParameter("type");
		double price = Double.parseDouble(request.getParameter("amenity_price"));
		
		//Get room
		Room room = hr.getRoom(hotelID, roomNumber);
		
		//Check the type is a valid word 
		Pattern pattern = Pattern.compile("^[A-Za-z, ]++$");
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		
		boolean validWord = pattern.matcher(type).matches();
		if (!validWord) {
			failedMessage = "This is not a valid word, please input a valid amenity type";
			response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+room.getHotelID()+"&room_number="+room.getRoomNumber()+"&capacity="+room.getCapacity()+"&is_extended="+room.getIsExtended()+"&price="+room.getPrice()+"&problem="+room.getProblem()+"&view_type="+room.getViewType()+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return;
		}
		
		//Create the amenity object 
		Amenity amenityToAdd = new Amenity(type, hotelID, roomNumber, price);
		
		//Add amenity to the DB
		boolean added = hr.addAmenity(amenityToAdd);
		
		if (!added) {
			failedMessage = "Oh oh something went wrong, could not add amenity";
			
		} else {
			message = "Amenity added successfully!";
		}
		
		//redirect to RoomInfoDisplay 
		response.sendRedirect("http://localhost:8080/HotelProject/RoomInfoDisplayServlet?hotel_id="+room.getHotelID()+"&room_number="+room.getRoomNumber()+"&capacity="+room.getCapacity()+"&is_extended="+room.getIsExtended()+"&price="+room.getPrice()+"&problem="+room.getProblem()+"&view_type="+room.getViewType()+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
		
		
	}

}
