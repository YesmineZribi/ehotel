<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.hotelProject.model.SearchRooms, java.util.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Customer Profile</title>
</head>
<body>

	<h3>Welcome, ${customer.firstName} </h3>
	<form action="GoBackServlet">
		<input type="submit" name="log-out" value="Log Out">
		
	</form>
	<br><br>
	
	<div class="customerProfile">
		<h4 align=center> Modify My Profile</h4>
		
		<p style="color:green"> ${success_message} </p>
		<p style="color:red"> ${failed_message} </p>
		
		<form action="ModifyCustomerProfileServlet" method="GET">
		
		
			SSN: <br> <input type="text" name="ssn" value="${customer.ssn}" readonly /> <br></br> 
			Registration Date <br> <input type="text" name="registration-date" value="${customer.registrationDate}" readonly /> <br><br>
			
			First name: <br> <input type="text" name="firstName" value="${customer.firstName}" required/> <br><br>
			Middle Name: <br> <input type="text" name="middleName" value ="${customer.middleName }"/> <br><br>
			Last Name: <br> <input type="text" name="lastName" value="${customer.lastName}" required/> <br><br>
			Password: <br> <input type="password" name="password" value="${customer.password }" required/> <br><br>
			Address Information: <br><br>
			Street Number: <br> <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="streetNumber" value="${address.streetNum }" required/> 
			
			<br><br>
			Street Name: <br> <input type="text" name="streetName" value="${address.streetName }" required/> <br><br>
			Province: <br> <input type="text" name="province" value="${address.province }" required/> <br><br>
			Country: <br> <input type="text" name="country" value="${address.country }" required/> <br> <br>
			Zip Code: <br> <input type="text" name="zip-code" value="${address.zipCode}" required/> <br><br>
						
			<input type="submit" value="Submit" /> 
		</form>	
	
	</div>
	<hr>
	
	<div class="customerBookings">
		<h4 align=center>My Bookings</h4>
		
		<table border = 1>
			<tr>
				<th>Booking ID</th>
				<th>Hotel</th>
				<th>Room Number</th>
				<th>Start Date</th>
				<th>End Date</th>
				<th>Price (/night)</th>
			</tr>
			<c:forEach var="tempBooking" items="${bookings}" >
				<tr>
					<td>${tempBooking.bookingID}</td>
					<td>${tempBooking.hotelID}</td>
					<td>${tempBooking.roomNumber}</td>
					<td>${tempBooking.startDate}</td>
					<td>${tempBooking.endDate}</td>
					<td>$${tempBooking.pricePerNight}</td>
				</tr>		
			</c:forEach>
		</table>
	</div>
	<br>
	<br>
	<hr>
	
	<div class="customerRents">
		<h4 align="center">My Rents</h4>
			<table border = 1>
			<tr>
				<th>Rent ID</th>
				<th>Hotel</th>
				<th>Room Number</th>
				<th>Start Date</th>
				<th>End Date</th>
				<th>Price (/night)</th>
				<th>Customer Balance</th>
			</tr>
			<c:forEach var="tempRent" items="${rents}" >
				<tr>
					<td>${tempRent.rentID}</td>
					<td>${tempRent.hotelID}</td>
					<td>${tempRent.roomNumber}</td>
					<td>${tempRent.startDate}</td>
					<td>${tempRent.endDate}</td>
					<td>$${tempRent.rentRate}</td>
					<td>$${tempRent.customerBalance}</td>
				</tr>		
			</c:forEach>
		</table>
	</div>
	<br>
	<hr>
	
	<div class="search-button">
		<h4 align=center>Search for more rooms to book </h4>
		<br>
		<form action="searchRoomsServlet" method="GET">
			Desired Start Date: <input type="date" name="start_date" placeholder="YYYY-MM-DD">&nbsp &nbsp
			Desired End Date: <input type="date" name="end_date" placeholder="YYYY-MM-DD"><br><br>
			Min Price: $<input type="number" step="any" min="1" name="min_price">&nbsp &nbsp 
			Max Price: $<input type="number" step="any" min="1" name="max_price"> <br><br>
			<% 
				SearchRooms rs = new SearchRooms();
				List<String> hotelChainNames = rs.getHotelChainNames();
				pageContext.setAttribute("hotel_chains", hotelChainNames);
				
				rs.getConnection().close();
			%>
			Preferred Hotel Chains: <br>
			<c:forEach var="chain" items="${hotel_chains}">
				<input type="checkbox" name="preferred_chain" value="${chain}"/>${chain} <br>
			</c:forEach> <br><br>
			
			<%
				int maxNum = rs.getMaxNumOfRoom();
			pageContext.setAttribute("max_num", maxNum);
			
			%>
			
			Preferred Number of Rooms In Hotel:
			<input type="number" oninput="this.value=(parseInt(this.value)||'')" name="num_of_rooms" min="1" max="${max_num}"><br><br>
			
			
			Min Capacity: <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="min_capacity" min="1" max="5" step="1"> &nbsp &nbsp 
			Max Capacity: <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="max_capacity" min="1" max="5" step="1">
			
			<br><br>
			Desired View: <select name="view_type">
								<option> </option>
								<option>Sea View</option>
								<option>Mountain View</option>
							</select>	<br><br>
			<%
				List<String> areas = rs.getAllRoomsAreas();
				pageContext.setAttribute("areas", areas);
				
			%>
			<!--  
			Preferred Areas: <br>
			<c:forEach var="area" items="${areas}">
				<input type="checkbox" name="preferred_area" value="${area}"/>${area} <br>				
			</c:forEach> -->
			
			<input type="hidden" name="customer_ssn" value="${customer.ssn}">
			<p align=center><input type="submit" value="Search For Rooms" /></p>
		</form>
	</div>
	
	<br>
	<hr>
		<div class="delete-accont" align=right>
		<form action="DeleteAccountServlet">
			<input type="hidden" name="customer_ssn" value="${customer.ssn}">
			<input type="hidden" name="user_type" value="customer">
			<input type="submit" name="delete_customer" value="Delete Account">
		</form>
	</div>

</body>
</html>