package com.hotelProject.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Hotel;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class DeleteOrAddHotelsServlet
 */
@WebServlet("/DeleteOrAddHotelsServlet")
public class DeleteOrAddHotelsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteOrAddHotelsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Figure out if a add or delete happened
		String addHotelButton = request.getParameter("add-hotel");
		
		if (addHotelButton != null) { //Add a hotel
			addHotel(request,response);
			return;
			
		} else { //figure out which delete button was pressed 
			//Get all the hotels of the hotelChain
			String hotelChain = request.getParameter("hotel_chain");
			
			HotelAndRoomManagement hr = new HotelAndRoomManagement();
			
			//Get all the hotels that belong to this chain
			List<Hotel> chainHotels = hr.getAllChainHotels(hotelChain);
			
			Hotel hotelSelected = null;
			String tempButton; 
			for (Hotel h : chainHotels) {
				tempButton = request.getParameter(h.getHotelID()+"_delete");
				if (tempButton != null) {
					hotelSelected = h; 
				}
			}
			
			deleteHotel(hotelSelected,request,response);
			
			
			
			
			try {
				hr.getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}
		
		
		
	}
	
	
	protected void deleteHotel(Hotel hotelToDelete, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Delete hotel from the DB
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		boolean deleted = hr.deleteHotel(hotelToDelete.getHotelID());
		
		String message, failedMessage; 
		message = failedMessage = "";
		if (!deleted) {
			failedMessage = "Oh oh something went wrong, could not delete the hotel, try again";
		} else {
			message = "Hotel deleted successfully!";
		}
		
		
		String encodedHotelChain = URLEncoder.encode(hotelToDelete.getHotelChainName(), "UTF-8");
		
		//redirect back 
		response.sendRedirect("DisplayHotelChainInfoServlet?hotel_chain="+encodedHotelChain+"&message="+message+"&failed_message="+failedMessage);

		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	protected void addHotel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the parameters involved 
		String hotelID = request.getParameter("hotel_id");
		String hotelName = request.getParameter("hotel_name");
		String hotelChain = request.getParameter("hotel_chain");
		String categoryString = request.getParameter("category");
		String streetNumberString = request.getParameter("street_num");
		String streetName = request.getParameter("street_name");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip_code");
		
		
		//create hotel: 
		Hotel newHotel = new Hotel(hotelChain, hotelID, hotelName, new Address(Integer.parseInt(streetNumberString),streetName, province, country, zipCode),null,null,null,null,0,Integer.parseInt(categoryString));
		
		//Add hotel to database 
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		boolean valid = hr.addHotel(newHotel);
		
		//Check if added successfully
		String message, failedMessage; 
		message = failedMessage = "";
		if (!valid) {
			failedMessage = "Oh oh something went wrong, could not add the hotel, either hotel already exists or one of your inputs is invalid!";
		} else {
			message = "Hotel added successfully!";
		}
		
		String encodedHotelChain = URLEncoder.encode(hotelChain, "UTF-8");
		
		response.sendRedirect("DisplayHotelChainInfoServlet?hotel_chain="+encodedHotelChain+"&message="+message+"&failed_message="+failedMessage);
		
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
