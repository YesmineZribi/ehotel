<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | HRs Info</title>
</head>
<body>
	<p style="color: red"> ${failed_message}</p>
	<p style="color:green">${message}</p>
	<div class="hotel-info">
		<h4 align=center>Update Hotel Information</h4>
		
		<form action="UpdateHotelServlet" method="GET">
			Hotel ID: <input type="text" name="hotel_id" value="${hotel.hotelID}" readonly><br><br>
			Hotel Name: <input type="text" name="hotel_name" value="${hotel.hotelName}" required><br><br>
			Category: <input type="number" oninput="this.value=(parseInt(this.value)||'')" min=1 max=5 name="hotel_category" value="${hotel.category}"><br><br>
			Owned by Hotel Chain: <input type="text" name="hotel_chain" value="${hotel.hotelChainName}" required><br><br>
			Manager SSN: <input type="text" name="manager_ssn" value="${hotel.managerSSN}"> <br><br>
			Manager Start Date: <input type="text" name="start_date" value="${hotel.startDate}" placeholder="YYYY-MM-DD"><br><br>
			Number of Rooms: <input type="number" name="room_num" value="${hotel.numOfRooms}" readonly ><br><br>	
			Address: <br><br> 
			Street Number: <input type="number" oninput="this.value=(parseInt(this.value)||'')" min= 1 name="street_num" value="${address.streetNum}" required> <br><br>
			Street Name: <input type="text" name="street_name" value="${address.streetName}" required><br><br>
			Province: <input type="text" name="province" value="${address.province}" required><br><br>
			Country: <input type="text" name="country" value="${address.country}" required><br><br>
			Zip Code: <input type="text" name="zip_code" value="${address.zipCode}" required><br><br>
			
			<input type="hidden" name="employee_ssn" value="${employee_ssn}">
			<input type="hidden" name="password" value="${password}">
			<input type="submit" name="update-hotel-info" value="Submit">
		</form>

	</div>
	<br>
	<hr> 
	
	<div align=center class="hotel-emails">

		<h5>Update Hotel Email(s)</h5>
		<form action="UpdateHotelServlet" method="GET">
			<table border = 1>
				<tr>
					<th>Email</th>
					<th></th>
				</tr>
				
				<c:forEach var="email" items="${hotel.emails}">
					<tr>
						<td>${email}</td>
						<td><input type="hidden" name="hotel_id" value="${hotel.hotelID}">
						<input type="hidden" name="employee_ssn" value="${employee_ssn}">
						<input type="hidden" name="password" value="${password}">
						<input type="submit" name="${email}-delete" value="Delete">
						</td>
					
					
					</tr>
				</c:forEach>
				
			</table>
		</form>
		<br>
		
		<form action="UpdateHotelServlet" method="GET">
			Add Email: <input type="text" name="email_to_add" placeholder="email@email.com" required>
			<input type="hidden" name="hotel_id" value="${hotel.hotelID}">
			<input type="hidden" name="employee_ssn" value="${employee_ssn}">
			<input type="hidden" name="password" value="${password}">			
			<input type="submit" name="add-email" value="Add">
		
		</form>
	</div>
	<br>
	<hr>
	<div align=center>
		<h5>Update Hotel Phone Number(s)</h5>
		<form action="UpdateHotelServlet">
			<table border=1>
				<tr>
					<th>Phone Number</th>
					<th></th>
				</tr>
				
				<c:forEach var="phone" items="${hotel.phones}">
					<tr>
					<td>${phone}</td>
					<td><input type="hidden" name="hotel_id" value="${hotel.hotelID}">
					<input type="hidden" name="employee_ssn" value="${employee_ssn}">
					<input type="hidden" name="password" value="${password}">					
					<input type="submit" name="${phone}-delete" value="Delete">					
					</tr>
					
				</c:forEach>
			
			</table>
		
		</form>
		<br>

		<form action="UpdateHotelServlet" method="GET">
			Add Phone Number (International Format): <input type="text" name="phone_to_add" placeholder="ex: +1 5417543010"required>
			<input type="hidden" name="hotel_id" value="${hotel.hotelID}">
			<input type="hidden" name="employee_ssn" value="${employee_ssn}">
			<input type="hidden" name="password" value="${password}">		
			<input type="submit" name="add-phone" value="Add">
		</form>
	
	</div>
	<br>
	<hr>
	
	
	<div align=center class="room-info">
		<h4 align=center> Update Room Information</h4>
			<form action="DirectToUpdateOrDeleteServlet" method="GET">
	 			<table border=1>
					<tr>
						<th>Room Number</th>
						<th>Capacity</th>
						<th>View Type</th>
						<th>Can be Extended</th>
						<th>Problem</th>
						<th>Price (Amenity included)</th>
						<th>Update Room (and View/Update Room Amenities)</th>
						<th></th>
						
					</tr>
					
					<c:forEach var="tempRoom" items="${hotel_rooms}">
						<tr>
							<td>${tempRoom.roomNumber}</td>
							<td>${tempRoom.capacity}</td>
							<td>${tempRoom.viewType }</td>
							<td>${tempRoom.isExtended}</td>
							<td>${tempRoom.problem}</td>
							<td>${tempRoom.price}</td>
							<td>
							<input type="hidden" name="hotel_id" value="${hotel.hotelID}"/>
							<input type="hidden" name="employee_ssn" value="${employee_ssn}">
							<input type="hidden" name="password" value="${password}">
							<input type="submit" name="${tempRoom.roomNumber}-update" value="Update" /></td>		
							
							<td>
							<input type="hidden" name="hotel_id" value="${hotel.hotelID}"/>
							<input type="hidden" name="employee_ssn" value="${employee_ssn}">
							<input type="submit" name="${tempRoom.roomNumber}-delete" value="Delete" /></td>					
						</tr>
					</c:forEach>	
				</table>			
			</form>
			<br>
			
				
	</div>
	<div class="add-room">
		<h5>Add Room: </h5>
		<form action="AddRoomServlet" method="GET">
			Room Number: <input type="number" min=1 oninput="this.value=(parseInt(this.value)||'')" name="room_number" required><br><br>
			Capacity: <input type="number" name="capacity" required><br><br>
			View Type: 
			<select name="view_type">
				<option>Mountain View</option>
				<option>Sea View</option>
			</select>
			<br><br>
			Can be Extended:
			<select name="is_extended">
				<option>true</option>
				<option>false</option>
			</select> <br><br>
			Problem: <input type="text" name="problem"><br><br>
			Price: $<input type="number" name="price" min="1" step="any" required>
			<i>*Include amenities price</i> <br><br>
			<input type="hidden" name="hotel_id" value="${hotel.hotelID}">
			<input type="hidden" name="employee_ssn" value="${employee_ssn}">
			<input type="hidden" name="password" value="${password}">
			<input type="submit" value="Add Room">
		</form>	
	</div>
	<br>
	
	<div class="go-back">
	<form action="GoBackServlet">
		<input type="hidden" name="hotel_id" value="${hotel.hotelID}">
		<input type="hidden" name="employee_ssn" value="${employee_ssn}">
		<input type="hidden" name="password" value="${password}">
		<input type="submit" name="employee_profile_display" value="Click to go back">
	</form>


	</div>

	
	

</body>
</html>