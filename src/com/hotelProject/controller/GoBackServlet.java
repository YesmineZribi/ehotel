package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Customer;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class GoBackServlet
 */
@WebServlet("/GoBackServlet")
public class GoBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoBackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Check which go back button was pressed
		String handRButton = request.getParameter("H_and_R_display");
		String employeeButton = request.getParameter("employee_profile_display");
		String logOutButton = request.getParameter("log-out");
		String selectedRoomInfoButton = request.getParameter("selected-room-info");
		String chainInfoButton = request.getParameter("chain-info");
		
		
		//if HandRButton was pressed go back to HandRServlet
		if (handRButton != null) {
			//Go back to HandRServlet
			goBackToHandRServlet(request,response);
			//return
			return;
		}
		
		//if employeeButton was pressed go back to employee profile
		if (employeeButton != null) {
			//Go back to employee profile 
			gobackToEP(request,response);
			
			//return 
			return;
		}
		
		if (logOutButton != null) {
			//Go back to login_signup page 
			gobackToLogin(request,response);
			//return 
			return;
		}
		
		if (selectedRoomInfoButton != null) {
			//Go back to Customer profile 
			gobackToCus(request,response);
			//return 
			return;
		}
		
		if (chainInfoButton != null) {
			//Go back to admin profile info 
			gobackToAdmin(request,response);
			return;
		}
	}
	
	/**
	 * Go back to AdminProfileDisplayServlet
	 */
	public void gobackToAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminProfileDisplayServlet?ssn=admin&password=passadmin");
	}
	
	
	/**
	 * Go back to HandRServlet
	 */
	protected void goBackToHandRServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Grab the hotel id 
		String hotelID = request.getParameter("hotel_id");
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		response.sendRedirect("http://localhost:8080/HotelProject/DisplayHandRServlet?hotel_id="+hotelID+"&message="+message+"&failed_message="+failedMessage+"&employee_ssn="+employeeSSN+"&password="+password);

		
	}

	
	/**
	 * Go back to EmployeeProfileDisplayServlet
	 */
	
	protected void gobackToEP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the fields necessary 
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employeeSSN+"&password="+password+"&user=employee&message="+message+"&failedMessage="+failedMessage);

	}

	
	/**
	 * Go back to login screen
	 */
	protected void gobackToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.sendRedirect("http://localhost:8080/HotelProject/login_signup.jsp");
		
		
	}
	
	
	/**
	 * Go back to CustomerProfileDisplayServlet
	 */
	protected void gobackToCus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get the fields necessary 
		String customerSSN = request.getParameter("customer_ssn");
		
		
		AccountManagement ac = new AccountManagement();
		
		//Get the customer 
		Customer c = ac.getCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

		try {
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
}
