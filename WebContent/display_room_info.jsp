<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Room Info </title>
</head>
<body>
	
	<p style="color:green">${message}</p>
	<p style="color:red">${failed_message}</p>
	
	<div class="update-room">
		<h4 align=center>Update Room Information</h4>
		
		<form action="UpdateRoomServlet" method="GET">
			Hotel ID: <input type="text" name="hotel_id" value="${room.hotelID}" readonly> <br><br>
			Room Number: <input type="number" oninput="this.value=(parseInt(this.value)||'')" step="1" min="1" name="new_room_number" value="${room.roomNumber}" required><br><br>
			Capacity: <input type="number" oninput="this.value=(parseInt(this.value)||'')" min=1  name="capacity" value="${room.capacity}" required><br><br>
			Is Extended: <select name="is_extended">
							<option selected>${room.isExtended}</option>
							<option>${not room.isExtended}</option>
						</select> <br><br>
			View Type: <select name="view_type">
							<c:choose>
								<c:when test="${room.viewType eq 'SeaView'}">
									<option selected>Sea View</option>
									<option>Mountain View </option>
								</c:when>
								<c:otherwise>
									<option> Mountain View</option>
									<option> Sea View</option>
								</c:otherwise>
							</c:choose>	
						</select><br><br>
			Problem: <input type="text" name="problem" value="${room.problem}"><br><br>
			Price: $<input type="number" step="any" min="1" name="price" value="${room.price}" required> <br><br>
			<input type="hidden" name="old_room_number" value="${room.roomNumber}">
			<input type="hidden" name="employee_ssn" value="${employee_ssn}">
			<input type="hidden" name="password" value="${password}">
			<input type="submit" name="update-room" value="Submit">
		</form>
	</div>
	<br>
	<div class="room-amenities">
		<h5 align=center>Room Amenities</h5>
		<form action="UpdateRoomServlet" method="GET">
			<table border=1>
				<tr>
					<th>Type</th>
					<th>Price (included in room fee)</th>
					<th></th>
				</tr>
				<c:forEach var="amenity" items="${amenities}">
					<tr>
						<td>${amenity.type}</td>
						<td>$${amenity.price}</td>
						<td><input type="hidden" name="hotel_id" value="${amenity.hotelID}">
						<input type="hidden" name="old_room_number" value="${room.roomNumber}">
						<input type="hidden" name="employee_ssn" value="${employee_ssn}">
						<input type="hidden" name="password" value="${password}">
						<input type="submit" name="${amenity.type}-delete" value="Delete">
						</td>
					</tr>		
				</c:forEach>	
			</table>
		</form>
	</div>
	<br>
	
	<div class="add-amenity">
	Add Amenity: <br><br>
		<form action="UpdateRoomServlet" method="GET">
		Type: <input type="text" name="type" required> &nbsp
		Price: $<input type="number" step="any" min="1" name="amenity_price" placeholder="0.00"required> &nbsp
		<input type="hidden" name="hotel_id" value="${room.hotelID}">
		<input type="hidden" name="old_room_number" value="${room.roomNumber}">
		<input type="hidden" name="employee_ssn" value="${employee_ssn}">
		<input type="hidden" name="password" value="${password}">
		<input type="submit" name="add-amenity" value="Add">
		
		</form>
	
	</div>
	<br>
	
	<div class="go-back">
	<form action="GoBackServlet">
		<input type="hidden" name="hotel_id" value="${room.hotelID}">
		<input type="hidden" name="employee_ssn" value="${employee_ssn}">
		<input type="hidden" name="password" value="${password}">
		<input type="submit" name="H_and_R_display" value="Click to go back">
	</form>


	</div>
	
	
	
	
	
	

</body>
</html>