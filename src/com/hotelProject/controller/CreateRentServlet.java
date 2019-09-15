package com.hotelProject.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Amenity;
import com.hotelProject.Classes.Booking;
import com.hotelProject.Classes.Employee;
import com.hotelProject.Classes.Rent;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.HotelAndRoomManagement;
import com.hotelProject.model.SearchRooms;

/**
 * Servlet implementation class CreateRentServlet
 */
@WebServlet("/CreateRentServlet")
public class CreateRentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	


       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateRentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		String availRooms = request.getParameter("avail_rooms_button");
		String hotelID = request.getParameter("hotel_id");
		String ssn = request.getParameter("ssn");
		System.out.println("Employee SSN "+ssn);
		System.out.println("Hotel ID "+hotelID);
		String sDate = request.getParameter("start_date");
		String eDate = request.getParameter("end_date");
		request.setAttribute("start_date", sDate);
		request.setAttribute("end_date", eDate);
		//Get employee object
		Employee employee = ac.getEmployee(ssn);
		

		
		if (availRooms != null) {
			java.util.Date startDate = null; java.util.Date endDate = null; 
			
			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);
				if (endDate.before(startDate) || endDate.equals(startDate)) {
					throw new ParseException("Illegal date format",0);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.getStackTrace();

				String message = "";
				String failedMessage = "Illegal date format";
				response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);
				return;
			}
			//Get rooms available btwn start date and end date
			
			List<Room> roomsAvailable = null;
			try {
				Date s = new java.sql.Date(startDate.getTime());
				Date ed = new java.sql.Date(endDate.getTime());
				roomsAvailable = sr.searchRoomsByDate(s,ed );

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Get rooms from that that belong to this hotel 
			List<Room> roomsAvailableInThisHotel = new ArrayList<Room>();
			
			for (Room r: roomsAvailable) {
				if (r.getHotelID().contentEquals(hotelID)) {
					roomsAvailableInThisHotel.add(r);
				}
			}
		
			
			//Get employee address 
			Address address = employee.getAddress();
			
			//Get bookings for hotel employee works ag 
			List<Booking> bookings = ac.getBookingsForHotel(employee.getHotelID());
			
			//Get rents employee is taking care of 
			List<Rent> employeeRents = ac.getRentsForHotel(ssn);
			
			//Get rents to archive
			List<Rent> pastRents = ac.getPastRents(employee.getHotelID());
			
			//Get archived bookings for this hotel 
			List<Booking> archivedBookings = ac.getArchivedBookings(employee.getHotelID());

			//Get archived rents for this hotel 
			List<Rent> archivedRents = ac.getArchivedRents(employee.getHotelID());

			request.setAttribute("employee", employee);
			request.setAttribute("address", address);
			request.setAttribute("bookings", bookings);
			request.setAttribute("rents", employeeRents);
			request.setAttribute("past_rents", pastRents);
			request.setAttribute("archived_bookings", archivedBookings);
			request.setAttribute("archived_rents", archivedRents);
			request.setAttribute("archived_bookings", archivedBookings);
			
			
			//Add this list to the request 
			request.setAttribute("rooms_avail", roomsAvailableInThisHotel);
			
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("employee_profile.jsp");
			dispatcher.forward(request, response);
			
		} else { //one of the select buttons was hit
			//Step (a) : Find which select button was pushed 
			//get hotel rooms 
			List<Room> roomsInThisHotel = sr.getRoomsForHotel(hotelID);
			Room roomSelected = roomsInThisHotel.get(0);
			String temporaryButton;
			
			for(Room r: roomsInThisHotel) {
				temporaryButton = request.getParameter(r.getRoomNumber()+"-to-add");
				if (temporaryButton != null) {
					roomSelected = r;
					
					//Get amenities of this room: 
					HotelAndRoomManagement hr = new HotelAndRoomManagement();
					List<Amenity> roomAmenities = hr.getAmenitiesForRoom(hotelID, roomSelected.getRoomNumber());
					String amenities = "Room includes ";
					for (Amenity a : roomAmenities) {
						amenities += a.getType()+" for $"+a.getPrice();
					}
					amenities += " (price of amenities is already included)";
					
					response.sendRedirect("http://localhost:8080/HotelProject/add_rent.jsp?employee_ssn="+ssn+"&hotel_id="+hotelID+"&room_number="+roomSelected.getRoomNumber()+"&room_price="+roomSelected.getPrice()+"&room_capacity="+roomSelected.getCapacity()+"&view_type="+roomSelected.getViewType()+"&is_extended="+roomSelected.getIsExtended()+"&problem="+roomSelected.getProblem()+"&employee_password="+employee.getPassword()+"&start_date="+sDate+"&end_date="+eDate+"&amenities="+amenities);
				}
			}
			
		}
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
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
