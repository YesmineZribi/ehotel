package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Customer;
import com.hotelProject.Classes.Employee;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class DeleteAccountServlet
 */
@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ipAddress = "http://192.168.0.38";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// Determine which button was pressed
		String deleteEmployeeButton = request.getParameter("delete_employee");
		
		String deleteCustomerButton = request.getParameter("delete_customer");
		
		if(deleteEmployeeButton != null) {
			//Call the delete employee method 
			deleteEmployee(request,response);
			//return 
			return;
		}
		
		if (deleteCustomerButton != null) {
			//Call delete customer method
			deleteCustomer(request,response);
			//return 
			return;
		}
	}
	
	/**
	 * Delete Employee
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		String employeeSSN = request.getParameter("employee_id");
		
		//Delete employee from DB 
		boolean deleted = ac.deleteEmployee(employeeSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!deleted) {
			failedMessage = "You still have rents you are responsible for, you cannot delete your account";
			
			//Get employee object 
			Employee employee = ac.getEmployee(employeeSSN);
			response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);
			
			
		} else {
			message = "Account successfully deleted...sorry to see you go";
			response.sendRedirect("http://"+ipAddress+":8080/HotelProject/login_signup.jsp?message="+message+"&failed_message="+failedMessage);
		}
		
		try {
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		AccountManagement ac = new AccountManagement();
		
		String customerSSN = request.getParameter("customer_ssn");
		
		//Delete customer from DB
		boolean deleted = ac.deleteCustomer(customerSSN);
		
		String message, failedMessage; 
		message = failedMessage = "";
		
		if (!deleted) {
			failedMessage = "You still have on-going rents you need to pay!";
			
			//Get customer object 
			Customer c = ac.getCustomer(customerSSN);
			response.sendRedirect("CustomerProfileDisplayServlet?ssn="+c.getSsn()+"&password="+c.getPassword()+"&user=customer&message="+message+"&failedMessage="+failedMessage);

			
		} else {
			message = "Account deleted...sorry to see you go";
			response.sendRedirect("http://"+ipAddress+":8080/HotelProject/login_signup.jsp?message="+message+"&failed_message="+failedMessage);

			
		}
		
		try {
			ac.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
