package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Hotel;
import com.hotelProject.model.HotelAndRoomManagement;

/**
 * Servlet implementation class UpdateHotelServlet
 */
@WebServlet("/UpdateHotelServlet")
public class UpdateHotelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ipAddress = "24.202.253.67";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateHotelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Determine which button was pressed
		String updateHotelButton = request.getParameter("update-hotel-info");
		String hotelID = request.getParameter("hotel_id");
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		System.out.println("The employee SSN is :"+employeeSSN);
		System.out.println("The password is: "+password);
		
		//Get the hotel object
		Hotel hotel = hr.getHotel(hotelID);
		
		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (updateHotelButton != null) {
			//Call update hotel method and pass it the hotel object
			System.out.println("Update Hotel button was pressed");
			updateHotelInfo(hotelID, employeeSSN, password, request, response);
			
			//return 
			return;
		}
		
		//CHECK DELETE EMAIL BUTTONS: Get hotel emails and iterate over them to determine if a delete button was pressed

		
		List<String> hotelEmails = hotel.getEmails(); 
		String emailToDelete = null;
		for (String email: hotelEmails) {
			if (request.getParameter(email+"-delete") != null) {
				emailToDelete = email;
			}
		}
		//if emailToDelete is null no delete button was pressed 
		if (emailToDelete != null) {
			//Call delete email method and pass it the email to delete
			System.out.println("The email to delete is: "+emailToDelete);
			deleteEmail(hotelID, emailToDelete, employeeSSN, password,request, response);
			//return
			return;
		}
		
		//CHECK ADD EMAIL BUTTON
		String addEmailButton = request.getParameter("add-email");
		if (addEmailButton != null) {
			//Call add email method 
			System.out.println("Add email button was pressed");
			String emailToAdd = request.getParameter("email_to_add");
			
			addEmail(hotelID,emailToAdd, employeeSSN, password, request,response);
			

			//return
			return; 
		}
		
		//CHECK DELETE PHONE BUTTONS: Get hotel phones and iterate over them to determine if a delete button was pressed 
		List<String> hotelPhones = hotel.getPhones();
		String phoneToDelete = null;
		for (String phone: hotelPhones) {
			if (request.getParameter(phone+"-delete") != null) {
				phoneToDelete = phone;
			}
		}
		//if phoneToDelete is null no delete button was pressed 
		if (phoneToDelete != null) {
			//Call delete phone method and pass it the phone to delete
			System.out.println("The phone to delete is :"+phoneToDelete);
			deletePhone(hotelID,phoneToDelete,employeeSSN, password, request,response);
			
			//return
			return; 
		}
		
		//CHECK ADD PHONE BUTTON 
		String addPhoneButton = request.getParameter("add-phone");
		if (addPhoneButton != null) {
			//Call add phone method 
			System.out.println("Add phone button was pressed");
			String phoneToAdd = request.getParameter("phone_to_add");
			addPhone(hotelID,phoneToAdd, employeeSSN, password, request, response);
			
			//return 
			return;
		}
		
		
		
	}
	
	/**
	 * Update hotel info method
	 */
	protected void updateHotelInfo(String hotelID, String employeeSSN, String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Get the form info 
		String hotelName = request.getParameter("hotel_name");
		String hotelChainName = request.getParameter("hotel_chain");
		String managerSSN = request.getParameter("manager_ssn");
		String categoryString = request.getParameter("hotel_category");
		//convert to Date
		String startDateString = request.getParameter("start_date");
		String streetNumString = request.getParameter("street_num");
		String streetName = request.getParameter("street_name");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip_code");
		
		Address newAddress; 
		
		String message, failedMessage; 
		message = failedMessage = "";
		try {
			newAddress = new Address(Integer.parseInt(streetNumString),streetName,province,country,zipCode);
	
		} catch(NumberFormatException  e) {
			failedMessage = "Illegal format for street number, please enter a valid street number";
			response.sendRedirect("http://"+ipAddress+":8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return ; 
		}
		
		//validate start address 
		java.util.Date startDate = null; 
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
		} catch(ParseException e) {
			failedMessage = "Illegal date format"; 
			response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return ; 			
		
		}
		
		//update info in the DB 
		boolean validHotelName = hr.modifyHotelName(hotelID, hotelName);
		boolean validChain = hr.modifyHotelChain(hotelID, hotelChainName);
		boolean validSSN = hr.modifyHotelManager(hotelID, managerSSN);		
		boolean validCategory = hr.modifyHotelCategory(hotelID, Integer.parseInt(categoryString));
		boolean validDate = hr.modifyHotelManagerStartDate(hotelID, new java.sql.Date(startDate.getTime()));
		boolean validAddress = hr.modifyHotelAddress(hotelID, newAddress);
		
		if (!validHotelName || !validChain || !validSSN || !validCategory || !validDate || !validAddress) {
			failedMessage = "One or more inputted fields is not valid, please check your input and resubmit!";
			response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return ; 
		}
		//Redirect to displayHandRServlet
		message = "Fields updated successfully!";
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	/**
	 * Delete email
	 */
	protected void deleteEmail(String hotelID, String email, String employeeSSN, String password, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Delete the email 
		boolean deleted = hr.deleteHotelEmail(hotelID, email);
		String message, failedMessage; 
		message = failedMessage = "";
		
		//redirect to DisplayHandRServlet 
		if (!deleted) {
			failedMessage = "Oh oh something went wrong, could not delete email!";
			response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			
		} else {
			message = "Email successfully deleted!";

		}
		
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Add email
	 */
	protected void addEmail(String hotelID, String email, String employeeSSN, String password, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Check email to add is valid: 
		String emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
		Pattern pat = Pattern.compile(emailRegex);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!pat.matcher(email).matches()) {
			failedMessage = "This is an invalid email address";
			response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return;
		}
		
		//Add email to DB 
		boolean added = hr.addHotelEmail(hotelID, email);
		
		//Redirect to displayHandRServlet
		if (!added) {
			failedMessage = "Oh oh something went wrong, please add a valid email";
		} else {
			message = "Email added successfully!";

		}
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	
	/**
	 * Delete phone number
	 */
	protected void deletePhone(String hotelID, String phone, String employeeSSN, String password, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//Delete phone number from db 
		boolean deleted = hr.deleteHotelPhone(hotelID, phone);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		//redirect to DisplayHandRServlet 
		if (!deleted) {
			failedMessage = "Oh oh something went wrong, could not delete phone!";			
		} else {
			message = "Phone Number successfully deleted!";
		}
		
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Add phone number
	 */
	protected void addPhone(String hotelID, String phone, String employeeSSN, String password, HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		HotelAndRoomManagement hr = new HotelAndRoomManagement();
		
		//validate phone number 
		String phoneRegex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
		
		Pattern pattern = Pattern.compile(phoneRegex);
		
		boolean valid = pattern.matcher(phone).matches();
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!valid) {
			failedMessage = "This is not a valid international phone number";
			response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);
			return;
		}
		
		//Add phone to DB 
		boolean added = hr.addHotelPhone(hotelID, phone);
		
		if (!added) {
			failedMessage = "Oh oh, something went wrong, could not add phone number...";
			
		} else {
			message = "Phone number added successfully!";
		}
		
		//Redirect to HandRServlet 
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		try {
			hr.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	

}
