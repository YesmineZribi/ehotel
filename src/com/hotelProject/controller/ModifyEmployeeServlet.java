package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Address;
import com.hotelProject.Classes.Employee;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class ModifyEmployeeServlet
 */
@WebServlet("/ModifyEmployeeServlet")
public class ModifyEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyEmployeeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		// Step 1: Get all the fields 
		String ssn = request.getParameter("ssn");
		String firstName = request.getParameter("firstName");
		String middleName = request.getParameter("middleName");
		String lastName = request.getParameter("lastName");
		String password = request.getParameter("password");
		String streetNum = request.getParameter("streetNumber");
		String streetName = request.getParameter("streetName");
		String province = request.getParameter("province");
		String country = request.getParameter("country");
		String zipCode = request.getParameter("zip-code");
		
		// Step 2: Get the employee object 
		Employee employee = ac.getEmployee(ssn);
		
		//Modify all necessary fields in employee
		employee.setFirstName(firstName); employee.setMiddleName(middleName); employee.setLastName(lastName);
		employee.setPassword(password);
		
		// Step 3: Create address object from the address fields 
		Address address;
		String message = ""; String failedMessage = "";
		
		try {
			 address = new Address(Integer.parseInt(streetNum), streetName, province, country,zipCode);
		} catch(NumberFormatException e) {
			//if invalid street num notify user
			 failedMessage = "Invalid street number please enter an integer";
			response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);
			
			return;
		}
		
		employee.setAddress(address);
		
		//Modify all attributes in the database
		ac.modifyFirstName(ssn, firstName);
		ac.modifyMiddleName(ssn, middleName);
		ac.modifyLastName(ssn, lastName);
		ac.modifyPassword(ssn, password);
		ac.modifyAddress(ssn, address);	
		
		message = "fields updated successfully!";

		response.sendRedirect("EmployeeProfileDisplayServlet?ssn="+employee.getSsn()+"&password="+employee.getPassword()+"&user=employee&message="+message+"&failedMessage="+failedMessage);

		try {
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
