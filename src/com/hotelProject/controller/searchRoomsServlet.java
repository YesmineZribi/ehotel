package com.hotelProject.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Room;
import com.hotelProject.model.AccountManagement;
import com.hotelProject.model.HotelAndRoomManagement;
import com.hotelProject.model.SearchRooms;

/**
 * Servlet implementation class searchRoomsServlet
 */
@WebServlet("/searchRoomsServlet")
public class searchRoomsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String customerSSN;
	String startDateString;
	String endDateString; 
	String minPriceString;
	String maxPriceString; 
	String[] preferredHotelChains; 
	String numOfRoomsString; 
	String minCapacityString; 
	String maxCapacityString; 
	String viewTypeString; 
	String viewType; 
	String[] preferredAreas;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchRoomsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//1. Get the customer ssn
		 customerSSN = request.getParameter("customer_ssn");
		
		
		//2. Get all the search fields 
		 startDateString = request.getParameter("start_date");
		 endDateString = request.getParameter("end_date");
		
		 
		 minPriceString = request.getParameter("min_price");
		 maxPriceString = request.getParameter("max_price");
		
		
		preferredHotelChains = request.getParameterValues("preferred_chain");
				
		
		numOfRoomsString = request.getParameter("num_of_rooms");
		
		
		minCapacityString = request.getParameter("min_capacity");
		maxCapacityString = request.getParameter("max_capacity");
				
		viewTypeString = request.getParameter("view_type");
		
		if (viewTypeString.contentEquals("Sea View")) {
			viewType = "SeaView";
		} else if (viewTypeString.contentEquals("Mountain View")) {
			viewType = "MountainView";
		}
				
		preferredAreas = request.getParameterValues("preferred_area");
		
		//3. First non-null field is the one to fetch from the DB
		if (startDateString != "" && endDateString != "") {
			//Call method that will check all fields for possible
			//combs with date search 
			System.out.println("Search by date combo");
			
			searchByDateCombo(request,response);
			
			//return;
			return;
		}
		
		else if (startDateString != "" || endDateString != "") {
			//Error message that both need to be filled out 
			System.out.println("Error both date fields need to be full");
			
		}
		else if (minPriceString != "" && maxPriceString != "") {
			//Call method that will check all fields starting
			//from prices for possible combos with price search
			System.out.println("Seach by price combo");
			
			searchByPriceCombo(request,response);
			
			//return
			return;
		}
		else if (minPriceString != "" || maxPriceString != "") {
			//Make the one that isn't null equal the other
			System.out.println("Send error message for price");
			
			//Call search by price combo
			
			
			//return
			
		}
		
		else if (preferredHotelChains != null) {
			//Call method that will check all fields starting
			//from hotel chains for possible combos with chain search
			System.out.println("Search by chain combo");
			
			searchByChain(request,response);
			
			//return
			return;
		}
		
		else if (numOfRoomsString != "") {
			//Call method that will check all fields starting 
			//from numOfRooms for possible combos with numOfRooms search
			System.out.println("Search by num of rooms combo");
			
			searchByNumOfRooms(request, response);
			
			//return 
			return;
			
		}
		
		else if (minCapacityString != "" && maxCapacityString != "") {
			//Call method that will check all fields starting from 
			//Capacity for possible combos with Capacity search 
			System.out.println("Seach by capacity combo");
			
			searchByCapacity(request,response);
			
			//return 
			return;
		}
		else if (minCapacityString != "" || maxCapacityString != "") {
			//Make the null one equal the non-null one
			if (minCapacityString != "") {
				maxCapacityString = minCapacityString;
			} else {
				minCapacityString = maxCapacityString; 
			}
			
			//Call search by capacity commbo method
			
			
		} else if (viewTypeString != "") {
			//Call method to search by view and by area 
			System.out.println("Search by view only");
			searchByView(request,response);
			
			//return
			return;
			
		} 
	
		else if (preferredAreas != null) {
			//Call method that will check all fields starting from 
			//preferred areas and that's it (last one) 
			System.out.println("search by area combo ");
			
			searchByArea(request,response);
			
			//return 
			return;
			
		} else {
			//Everything is empty display all rooms 
			searchAllRooms(request,response);
			return; 
		}
	
	}
	
	protected void searchByDateCombo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		//Get customer object to send in redirect 
		Customer customer = ac.getCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		//Convert the date strings to util.Date 
		java.util.Date startDate = null; java.util.Date endDate = null;
		
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
			
			if (endDate.before(startDate) || endDate.equals(startDate)) {
				throw new ParseException("Illegal date format",0);
			}
			
		} catch (ParseException e) { //if date entered is funky
			e.getStackTrace();
			
			failedMessage = "Illegal date format";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);
			
			//close connection
			try {
				sr.getConnection().close();
				ac.getConnection().close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return;
			
		}
		
		//Convert string dates to SQL dates
		Date start = new java.sql.Date(startDate.getTime());
		Date end = new java.sql.Date(endDate.getTime());
		
		List<Room> roomsByDate = null; 
		//1. Get rooms available by date from DB
		try {
			roomsByDate = sr.searchRoomsByDate(start, end);
		} catch (ParseException e1) {
			//close connection
			try {
				sr.getConnection().close();
				ac.getConnection().close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		//2. If price not null add price search 
		if (minPriceString != "" && maxPriceString != "") {
			//remove the rooms which prices do not fall in btw the min and max
			Double minPrice, maxPrice; 
			minPrice = Double.parseDouble(minPriceString);
			maxPrice = Double.parseDouble(maxPriceString);
			
			List<Room> roomsToDelete = new ArrayList<Room>();
			
			for (Room r: roomsByDate) {
				if (r.getPrice() < minPrice || r.getPrice() > maxPrice) {
					roomsToDelete.add(r);
				}
			}
			
			//delete the rooms contained in roomsToDelete
			List<Room> tempRoomsByDate = new ArrayList<Room>();
			for (Room r: roomsByDate) {
				if (!roomsToDelete.contains(r)) {
					tempRoomsByDate.add(r);
				}
			}
			
			roomsByDate = tempRoomsByDate; 
			
		} 
		else if ((minPriceString == "" && maxPriceString != "" )|| (maxPriceString == "" && minPriceString != "")) {
			failedMessage = "Please enter input for both minn and max price fields or neither";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		}
		
		//3. if chains is not null add chain search 
		if (preferredHotelChains != null) {
			//Get all the rooms based on hotel chains
			List<String> chains = Arrays.asList(preferredHotelChains);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByDate) {
				String roomChain = sr.roomHotelChain(r);
				if (chains.contains(roomChain)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByDate = roomsToKeep;
		}
		
		//4. if numofrooms is add numofrooms search
		if (numOfRoomsString != "") {
			//Get the number of rooms 
			int numOfRooms = Integer.parseInt(numOfRoomsString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			//Only keep those who return number of rooms equal
			for (Room r: roomsByDate) {
				if (sr.roomHotelNumOfRooms(r) == numOfRooms) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByDate = roomsToKeep;
			
		}
		
		
		//5. if capacities add capacity search 
		if (minCapacityString != "" && maxCapacityString != "") {
			//Get the room capacities and make sure they are within the range
			int minCapacity = Integer.parseInt(minCapacityString);
			int maxCapacity = Integer.parseInt(maxCapacityString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByDate) {
				if (r.getCapacity() >= minCapacity && r.getCapacity() <= maxCapacity) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByDate = roomsToKeep; 
		} else if ((minCapacityString == "" && maxCapacityString != "" )|| (maxCapacityString == "" && minCapacityString != "")) {
			failedMessage = "Please enter input for both minn and max capacity fields or neither";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		}
		
		
		//6. if view is not empty add search by view option
		if (viewTypeString != "") {
			
			//Keep only the ones with the view in question 
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByDate) {
				if (r.getViewType().contentEquals(viewType)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByDate = roomsToKeep;
			
		}
		
		//7. if preferredAreas array is not null look up
		//rooms whose hotel provinces are included there 
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByDate) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByDate = roomsToKeep;
		}
		
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByDate);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);
		
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	
	protected void searchByPriceCombo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		//Get customer object to send in redirect 
		Customer customer = ac.getCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		//Convert prices to doubles 
		double minPrice = Double.parseDouble(minPriceString);
		double maxPrice = Double.parseDouble(maxPriceString);
		
		//Get rooms by price from DB
		List<Room> roomsByPrice = sr.searchRoomsByPrice(minPrice, maxPrice);
		
		System.out.println("Rooms available in this price range");
		System.out.println(roomsByPrice);
		
		//1. if preferredHotelChains not null add chain search 
		if (preferredHotelChains != null) {
			//Get all the rooms based on hotel chains
			List<String> chains = Arrays.asList(preferredHotelChains);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByPrice) {
				String roomChain = sr.roomHotelChain(r);
				if (chains.contains(roomChain)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByPrice = roomsToKeep;
		}
		
		//2 if roomNum was mentioned
		if (numOfRoomsString != "") {
			//Get the number of rooms 
			int numOfRooms = Integer.parseInt(numOfRoomsString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			//Only keep those who return number of rooms equal
			for (Room r: roomsByPrice) {
				if (sr.roomHotelNumOfRooms(r) == numOfRooms) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByPrice = roomsToKeep;
			
		}
		
		//3. If capacities are not null
		if (minCapacityString != "" && maxCapacityString != "") {
			//Get the room capacities and make sure they are within the range
			int minCapacity = Integer.parseInt(minCapacityString);
			int maxCapacity = Integer.parseInt(maxCapacityString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByPrice) {
				if (r.getCapacity() >= minCapacity && r.getCapacity() <= maxCapacity) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByPrice = roomsToKeep; 
		} else if ((minCapacityString == "" && maxCapacityString != "" )|| (maxCapacityString == "" && minCapacityString != "")) {
			failedMessage = "Please enter input for both minn and max capacity fields or neither";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		}
		
		//4. If view is not empty
		if (viewTypeString != "") {
			
			//Keep only the ones with the view in question 
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByPrice) {
				if (r.getViewType().contentEquals(viewType)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByPrice = roomsToKeep;
			
		}
		
		//5. If preferred areas array is not null
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByPrice) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByPrice = roomsToKeep;
		}
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByPrice);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	protected void searchByChain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
	
		//Get customer object to send in redirect 
		Customer customer = ac.getCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		//Get all rooms in the hotel chains selected
		List<Room> roomsByChain = new ArrayList<Room>();
		
		for(String chain: preferredHotelChains) {
			roomsByChain.addAll(sr.searchRoomsByHotelChain(chain));
		}
		
		//1. If numofrooms is full
		if (numOfRoomsString != "") {
			//Get the number of rooms 
			int numOfRooms = Integer.parseInt(numOfRoomsString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			//Only keep those who return number of rooms equal
			for (Room r: roomsByChain) {
				if (sr.roomHotelNumOfRooms(r) == numOfRooms) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByChain = roomsToKeep;
			
		}
		
		
		//2. if capacities are not null 
		if (minCapacityString != "" && maxCapacityString != "") {
			//Get the room capacities and make sure they are within the range
			int minCapacity = Integer.parseInt(minCapacityString);
			int maxCapacity = Integer.parseInt(maxCapacityString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByChain) {
				if (r.getCapacity() >= minCapacity && r.getCapacity() <= maxCapacity) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByChain = roomsToKeep; 
		} else if ((minCapacityString == "" && maxCapacityString != "" )|| (maxCapacityString == "" && minCapacityString != "")) {
			failedMessage = "Please enter input for both minn and max capacity fields or neither";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		}
		
		
	
		//3. If view is not empty
		if (viewTypeString != "") {
			
			//Keep only the ones with the view in question 
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByChain) {
				if (r.getViewType().contentEquals(viewType)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByChain = roomsToKeep;
			
		}
		
		//4. If preferred areas array is not null
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByChain) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByChain = roomsToKeep;
		}
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByChain);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);
		
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void searchByNumOfRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
	
		//Get customer object to send in redirect 
		Customer customer = ac.getCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		//Get all the rooms whose hotels have the numofrooms specified
		int numOfRooms = Integer.parseInt(numOfRoomsString);
		
		List<Room> roomsByNum = sr.searchRoomsByNumOfRoomsInHotel(numOfRooms);
		
		
		//1. If capacities are not null
		if (minCapacityString != "" && maxCapacityString != "") {
			//Get the room capacities and make sure they are within the range
			int minCapacity = Integer.parseInt(minCapacityString);
			int maxCapacity = Integer.parseInt(maxCapacityString);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByNum) {
				if (r.getCapacity() >= minCapacity && r.getCapacity() <= maxCapacity) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByNum = roomsToKeep; 
		} else if ((minCapacityString == "" && maxCapacityString != "" )|| (maxCapacityString == "" && minCapacityString != "")) {
			failedMessage = "Please enter input for both min and max capacity fields or neither";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+customerSSN+"&password="+customer.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		}
		
		//4. If view is not empty
		if (viewTypeString != "") {
			
			//Keep only the ones with the view in question 
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByNum) {
				if (r.getViewType().contentEquals(viewType)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByNum = roomsToKeep;
			
		}
		
		//5. If preferred areas array is not null
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByNum) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByNum = roomsToKeep;
		}
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByNum);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);		
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void searchByCapacity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		int minCapacity = Integer.parseInt(minCapacityString);
		int maxCapacity = Integer.parseInt(maxCapacityString);
		
		//Get all rooms by capacity: 
		List<Room> roomsByCapacity = sr.searchRoomsByCapacity(minCapacity, maxCapacity);
		
		//4. If view is not empty
		if (viewTypeString != "") {
			
			//Keep only the ones with the view in question 
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByCapacity) {
				if (r.getViewType().contentEquals(viewType)) {
					roomsToKeep.add(r);
				}
			}
			
			roomsByCapacity = roomsToKeep;
			
		}
		
		//5. If preferred areas array is not null
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByCapacity) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByCapacity = roomsToKeep;
		}
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByCapacity);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);	
		
		
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	protected void searchByView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		//Get all rooms by the view specified 
		List<Room> roomsByView = sr.searchRoomsByView(viewType);
		
		if (preferredAreas != null) {
			List<String> areas = Arrays.asList(preferredAreas);
			
			List<Room> roomsToKeep = new ArrayList<Room>();
			
			for (Room r: roomsByView) {
				String roomProvince = sr.roomHotelProvince(r);
				if (areas.contains(roomProvince)) {
					roomsToKeep.add(r);
				}
				
			}
			
			roomsByView = roomsToKeep;
		}
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByView);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);	
		
		
		
		
		
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	protected void searchByArea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SearchRooms sr = new SearchRooms();
		AccountManagement ac = new AccountManagement();
		
		//Get all rooms by area 
		List<Room> roomsByArea = new ArrayList<Room>();
		
		for(String area: preferredAreas) {
			roomsByArea.addAll(sr.searchRoomsByArea(area));
		}
		
		System.out.println(roomsByArea);
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", roomsByArea);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);	
		
		try {
			sr.getConnection().close();
			ac.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	protected void searchAllRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Get all rooms
		List<Room> allRooms = hr.getAllRooms();
		
		request.setAttribute("customer_ssn", customerSSN);
		request.setAttribute("search_result", allRooms);

		
		RequestDispatcher dispatcher = request.getRequestDispatcher("display_room_search.jsp");
		
		dispatcher.forward(request, response);	
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	

}
