<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.hotelProject.model.HotelAndRoomManagement, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Login or Sign Up</title>
</head>
<body>
	
	
	<% 
		String message = request.getParameter("message");
		boolean displayMessage = false;
		if (message != null) {
			displayMessage = true;
		}
		
		String failedMessage = request.getParameter("failed_message");
		boolean displayFailed = false; 
		if (failedMessage != null) {
			displayFailed = true;
		}
		
		pageContext.setAttribute("displayFailed",displayFailed);
		pageContext.setAttribute("displayMessage",displayMessage);
		
		
	%>
	
	<c:if test="${displayMessage}">
			<p style="color:green"><%=request.getParameter("message") %></p>
		
	</c:if>
	<c:if test="${displayFailed}">
		<p style="color:red"><%=request.getParameter("failed_message") %></p>
	</c:if>
	
	<div class="login">
		<h3 align=center>Login</h3>
		<br>
		
		
		<p style="color:red"> ${error_message} </p>
		
		<form action="LoginServlet" method="GET">
		
			<select name="user">
				<option>customer</option>
				<option>employee</option>
				<option>admin</option>
			</select> <br> <br>
				
			SSN <br>
			<input type="text" name="ssn" placeholder="enter your SSN"/>
			
			<br><br>
			Password:<br>
			 <input type="password" name="password" placeholder="enter your password"/>
			 
			<br><br>
			
			<input type="submit" value="Submit"/>
		
		
		</form>	
	</div>
	
	<br><br>
	<hr>
	
	<div class="sign-up">
		<h3 align=center>Customer Sign up </h3>
		<br>
		<form action="SignUpServlet" method="GET">
			
			SSN <br>
			<input type="number" oninput="this.value=(parseInt(this.value)||'')"  name="customer_ssn" placeholder="enter your SSN" required/><br><br>
			Password:<br>
			<input type="password" name="password" placeholder="enter your password" required minlength=6 required/><br><br>
			
			First name: <br> <input type="text" name="firstName" placeholder="enter your first name" required/> <br><br>
			Middle Name: <br> <input type="text" name="middleName" placeholder ="enter your middle name"/> <br><br>
			Last Name: <br> <input type="text" name="lastName" placeholder="enter your last name" required/> <br><br>
			Address Information: <br><br>
			Street Number: <br> <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="streetNumber" placeholder="enter your street number" required/> <br><br>
			Street Name: <br> <input type="text" name="streetName" placeholder="enter your street name" required/> <br><br>
			Province: <br> <input type="text" name="province" placeholder="enter your province" required/> <br><br>
			Country: <br> <input type="text" name="country" placeholder="enter your country" required/> <br> <br>
			Zip Code <br> <input type="text" name="zip-code" placeholder="enter your Zip Code" required><br><br>		
			<input type="submit" name="customer_signup" value="Submit" /> 			
		</form>
	</div>
	<br><br>
	<hr>
		<div class="sign-up">
		<h3 align=center>Employee Sign up </h3>
		<br>
		
		<form action="SignUpServlet" method="GET">
		
			<%
				HotelAndRoomManagement hr = new HotelAndRoomManagement();
				List<String> hotelIDs = hr.getAllHotelIDs();
				pageContext.setAttribute("hotels",hotelIDs);
				
				hr.getConnection().close();
				
			%>
			
			Hotel ID: <br>
			<select name="hotel_ids">
			<c:forEach var="hotel_id" items="${hotels}">
				<option>${hotel_id}</option>
			</c:forEach>
			</select><br><br>
			SSN <br>
			<input type="number" oninput="this.value=(parseInt(this.value)||'')"  name="employee_ssn" placeholder="enter your SSN" required/><br><br>
			Password:<br>
			<input type="password" name="password" placeholder="enter your password" required minlength=6 required/><br><br>
			
			First name: <br> <input type="text" name="firstName" placeholder="enter your first name" required /> <br><br>
			Middle Name: <br> <input type="text" name="middleName" placeholder ="enter your middle name"/> <br><br>
			Last Name: <br> <input type="text" name="lastName" placeholder="enter your last name" required /> <br><br>
			Address Information: <br><br>
			Street Number: <br> <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="streetNumber" placeholder="enter your street number" required/> <br><br>
			Street Name: <br> <input type="text" name="streetName" placeholder="enter your street name" required/> <br><br>
			Province: <br> <input type="text" name="province" placeholder="enter your province" required/> <br><br>
			Country: <br> <input type="text" name="country" placeholder="enter your country" required/> <br> <br>
			Zip Code <br> <input type="text" name="zip-code" placeholder="enter your Zip Code" required><br><br>			
			<input type="submit" name="employee_signup" value="Submit" /> 			
		</form>
	</div>


		
		
		

</body>
</html>