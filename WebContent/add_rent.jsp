<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Add Rent</title>
</head>
<body>

	<h4>Add Rent</h4>
	<form action="AddRentServlet" method="GET">
		Customer SSN: <input type="text" name="customer_ssn" /> <br><br>
		Employee SSN: <input type="text" name="employee_ssn" value="<%= request.getParameter("employee_ssn") %>" readonly /> <br><br>
		Hotel ID: <input type="text" name="hotel_id" value="<%= request.getParameter("hotel_id") %>" readonly /> <br><br>
		Room Number: <input type="number" name="room_number" value="<%= request.getParameter("room_number") %>" readonly /> <br><br>
		Room Capacity: <input type="number" name="room_capacity" value="<%= request.getParameter("room_capacity") %>" readonly /><br><br>
		View Type: <input type="text" name="view_type" value="<%= request.getParameter("view_type") %>" readonly/><br><br>
		Is Extended: <input type="text" name="is_extended" value="<%= request.getParameter("is_extended") %>" readonly/><br><br>
		Problem: <input type="text" name="problem" value="<%= request.getParameter("problem") %>" readonly/> <br><br>
		Rent: <input type="number" name="rent" value="<%= request.getParameter("room_price") %>" readonly/> <br><br>
		Customer Balance: <input type="number" step="any" min="1" name="customer_balance" /> <br><br>

		Start Date: <input type="text" name="start_date" value="<%= request.getParameter("start_date") %>"/ readonly><br><br>		
		End Date: <input type="text" name="end_date" value="<%= request.getParameter("end_date") %>" readonly>	<br><br>
		
		<input type="submit" value="submit" />	
	
	
	</form>
	<i>*<%= request.getParameter("amenities") %></i>
	

</body>
</html>