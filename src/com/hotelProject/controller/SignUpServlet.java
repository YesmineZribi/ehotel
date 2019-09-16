package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Employee;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ipAddress = "24.202.253.67";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Figure out if it's an employee or customer signing up
		String customerSignButton = request.getParameter("customer_signup");
		String employeeSignButton = request.getParameter("employee_signup");
		
		if (customerSignButton != null) {
			System.out.println("Customer signing up");
			customerSignUp(request,response);
			return;
			
		}
		
		if (employeeSignButton != null) {
			System.out.println("Employee signing up");
			employeeSignUp(request,response);
			return;
		}
		
		//Send to the right method
		
	}
	
	protected void customerSignUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get all the necessary fields 
		String customerSSN = request.getParameter("customer_ssn");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String middleName = request.getParameter("middleName");
		String lastName = request.getParameter("lastName");
		String streetNumberString = request.getParameter("streetNumber");
		String streetName = request.getParameter("streetName");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip-code");
		
		//create customer object
		Customer c = new Customer(customerSSN, firstName, middleName, lastName, new Address(Integer.parseInt(streetNumberString), streetName, province, country, zipCode),new java.util.Date(), password);
		
		AccountManagement ac = new AccountManagement();
		
		//Add to DB
		boolean added = ac.addCustomer(c);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if(!added) { //failed, redirect back to log in
			failedMessage = "Could not add customer, either customer already exists or one of your inputs was invalid";
			response.sendRedirect("http://"+ipAddress+":8080/HotelProject/login_signup.jsp?message="+message+"&failed_message="+failedMessage);
			
		} else { //succeeded, go to profile
			message = "Customer added successfully!";
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failed_message="+failedMessage);
		}
		
		
		try {
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	protected void employeeSignUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get all necessary fields
		String hotelID = request.getParameter("hotel_ids");
		String employeeSSN = request.getParameter("employee_ssn");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String middleName = request.getParameter("middleName");
		String lastName = request.getParameter("lastName");
		String streetNumberString = request.getParameter("streetNumber");
		String streetName = request.getParameter("streetName");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip-code");
		
		//create employee object 
		Employee e = new Employee(employeeSSN, firstName, middleName, lastName, new Address(Integer.parseInt(streetNumberString),streetName, province, country, zipCode),new ArrayList<String>(),hotelID,password);
		
		AccountManagement ac = new AccountManagement();
		
		//Add employee to DB
		boolean added = ac.addEmployee(e);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!added) {
			failedMessage = "Could not add customer, either customer already exists or one of your inputs was invalid";
			response.sendRedirect("http://"+ipAddress+":8080/HotelProject/login_signup.jsp?message="+message+"&failed_message="+failedMessage);
		} else {
			message = "Employee added successfully!";
			response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+e.getSsn()+"&password="+e.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);
		}
		
		try {
			ac.getConnection().close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
