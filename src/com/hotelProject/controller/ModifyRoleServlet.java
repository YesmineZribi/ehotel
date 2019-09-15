package com.hotelProject.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotelProject.Classes.Employee;
import com.hotelProject.model.AccountManagement;

/**
 * Servlet implementation class ModifyRoleServlet
 */
@WebServlet("/ModifyRoleServlet")
public class ModifyRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyRoleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AccountManagement ac = new AccountManagement();
		
		// Step 1 (a) : Determine which button was pushed 
		String addButton = request.getParameter("add");
		
	
		// Step 1 (b): Grab all other parameters 
		String ssn = request.getParameter("ssn");
		
		System.out.println("the ssn");
		System.out.println(ssn);
		
		// Step 2 : create employee object from ssn 
		Employee employee = ac.getEmployee(ssn);
		
		String message = ""; 
		String failedMessage = "";
		// Step 3 (a) : if add was invoked - add role to employee object
		if (addButton != null) {

			String role = request.getParameter("role");
			
			// Step 3 (b): add role to db 
			boolean canAdd = ac.addRole(ssn, role);
			
			if(canAdd) {
				employee.addRole(role);
				
				// Step 3 (c): forward success message + every other employee attribute
				message = "Role succesfully added!";
				
			} else {
				failedMessage = "You alread added this role!";
			}
				
		} else {
			// Step 4 (a): if delete was involved - delete role from employee object
			//Find which delete button was pushed
			List<String> employeeRoles = employee.getRoles();
			String roleToDelete = employeeRoles.get(0);
			String temporaryButton;
			//Loop over the roles, grab the name of the button to which it isn't null
			for (String r: employeeRoles) {
				temporaryButton = request.getParameter(r+"-to-delete") ;
				if (temporaryButton != null) {
					roleToDelete = r; 
					System.out.println("The role to delete is "+roleToDelete);
					System.out.println("Deleting from employee");
					employee.deleteRole(roleToDelete);
					System.out.println(employee.getRoles());			
					
					// Step 4 (b): delete role from db 
					ac.removeRole(ssn, roleToDelete);
					
					// Step 4 (c): forward success message
					message = "Role successfully deleted!";
					break;
				}
			}
		
		}
		
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
