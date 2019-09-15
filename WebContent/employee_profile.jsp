<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="com.hotelProject.Classes.*, java.util.*, com.hotelProject.model.*" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>HotelProject | Employee Profile</title>
</head>
<body>

</body>


	<h3>Welcome, ${employee.firstName}</h3>
	<form action="GoBackServlet">
		<input type="submit" name="log-out" value="Log Out">
		
	</form>
	<br><br>
	
		<p style="color:green"> ${success_message} </p>
		<p style="color:red"> ${failed_message} </p>
	
	<div class="employeeProfile">
		<h4 align=center> Modify My Profile</h4>
	
				
		<form action="ModifyEmployeeServlet" method="GET">
		
		
			SSN: <br> <input type="text" name="ssn" value="${employee.ssn}" readonly /> <br></br> 
			Hotel ID: <br> <input type="text" name="hotel-id" value="${employee.hotelID}" readonly /> <br><br>
			
			First name: <br> <input type="text" name="firstName" value="${employee.firstName}" required/> <br><br>
			Middle Name: <br> <input type="text" name="middleName" value ="${employee.middleName }" /> <br><br>
			Last Name: <br> <input type="text" name="lastName" value="${employee.lastName}" required/> <br><br>
			Password: <br> <input type="password" name="password" value="${employee.password }" required/> <br><br>
			Address Information: <br><br>
			<p style="color:red"> ${error_streetNum} </p>
			Street Number: <br> <input type="number" oninput="this.value=(parseInt(this.value)||'')" name="streetNumber" value="${address.streetNum }" required/> 
			
			<br><br>
			Street Name: <br> <input type="text" name="streetName" value="${address.streetName }" required/> <br><br>
			Province: <br> <input type="text" name="province" value="${address.province }" required/> <br><br>
			Country: <br> <input type="text" name="country" value="${address.country }" required/> <br> <br>
			Zip Code: <br> <input type="text" name="zip-code" value="${address.zipCode}" required/> <br><br>
						
			<input type="submit" value="Submit" /> 
		</form>
	
	
	</div>
	<br>
	<hr>
	
	
	<div class="roles" align= center>
		<h4 align=center>Employee Roles</h4>
		<form action="ModifyRoleServlet" method ="GET" >
			<input type="hidden" name="ssn" value="${employee.ssn}" />
			<table border=1>
				<tr>
					<th>Roles</th> 
					<th></th>
				</tr>
				<c:forEach var="tempRole" items="${employee.roles}">
					<tr>
					<td> ${tempRole} </td>
					<td><input type="hidden" name="${tempRole}" value="${tempRole}" />
					<input type="submit" name= "${tempRole}-to-delete" value="Delete" />
					</td>
					</tr>
				</c:forEach>
		
			</table>
			<br>
		
		</form>
	</div>
	<br>
	<div align=center>
		<form action="ModifyRoleServlet" method="GET">
			Add Role: <input type="text" name="role" required/> <input type="hidden" name="ssn" value="${employee.ssn}"/>
			<input type="submit" name="add" value="Add" />		
		</form>
	
	
	</div>
	<hr>
	
	<div class="add-rent-directly">
		<h4 align=center>Add Rent</h4>
		<form action="CreateRentServlet" method="GET">
			Start Date: <input type="date" name="start_date" placeholder="YYYY-MM-DD" value="${start_date}" required/><br><br>		
			End Date: <input type="date" name="end_date" placeholder="YYYY-MM-DD" value="${end_date}" required/>
			<input type="hidden" name="hotel_id" value="${employee.hotelID}"/>
			<input type="hidden" name="ssn" value="${employee.ssn}"/>
			<input type="submit" name="avail_rooms_button" value="View Available Rooms"/>
			<br><br>
			<table border = 1>
				<tr>
					<th>Room Number</th>
					<th>Capacity</th>
					<th>View Type</th>
					<th>Can be extended</th>
					<th>Problem </th>	
					<th>Price (Amenity prices included)</th>
					<th></th>	
				</tr>
				
				<c:forEach var="tempRoom" items="${rooms_avail}" >
					<tr>
						<td>${tempRoom.roomNumber}</td>
						<td>${tempRoom.capacity}</td>
						<td>${tempRoom.viewType }</td>
						<td>${tempRoom.isExtended}</td>
						<td>${tempRoom.problem}</td>
						<td>${tempRoom.price }</td>
						<td>
						<input type="hidden" name="hotel_id" value="${employee.hotelID}"/>
						<input type="hidden" name="ssn" value="${employee.ssn}"/>
						<input type="hidden" name="start_date" value="${start_date}"/>
						<input type="hidden" name="end_date" value="${end_date}" />
						<input type="submit" name="${tempRoom.roomNumber}-to-add" value="Select" /></td>
					</tr>
				
				</c:forEach>
				
				
			</table>
		
			
			
		
		
		
		</form>
	</div>
	<br>
	<hr>
	
	<div class="bookings-for-hotel">
		<h4 align=center> Bookings For Hotel </h4>
		<form action="ConvertBookingServlet" method="GET">
			<table border = 1>
				<tr>
					<th>Customer SSN </th>
					<th>Booking ID</th>
					<th>Room Number </th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Price (/night)</th>
					<th></th>
				</tr>
				
				<c:forEach var="tempBooking" items="${bookings}" >
					<tr>
						<td>${tempBooking.customerSSN}</td>
						<td>${tempBooking.bookingID}</td>
						<td>${tempBooking.roomNumber}</td>
						<td>${tempBooking.startDate}</td>
						<td>${tempBooking.endDate}</td>
						<td>${tempBooking.pricePerNight}</td>
						<td> <input type="hidden" name="hotel_id" value="${employee.hotelID }" >
							<input type="hidden" name="employee_ssn" value="${employee.ssn}">
						<input type="submit" name = "${tempBooking.bookingID}-Convert" value="Convert"></td>
			
					</tr>
				</c:forEach>
			</table>		
		</form>
	</div>
	<br>
	<hr>
	
	<div class="rents-responsible-for">
		<h4 align=center>Rents Responsible For</h4>
		<form action="PayRentServlet" method="GET">
			<table border=1>
				<tr>
					<th>Customer SSN</th>
					<th>Rent ID</th>
					<th>Room Number</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Rent</th>
					<th>Customer Balance</th>
					<th> Pay Rent</th>
				</tr>
				<c:forEach var="tempRent" items="${rents}">
					<tr>
						<td>${tempRent.customerSSN}</td>
						<td>${tempRent.rentID }</td>
						<td>${tempRent.roomNumber }
						<td>${tempRent.startDate}</td>
						<td>${tempRent.endDate }</td>
						<td>${tempRent.rentRate}</td>
						<td>${tempRent.customerBalance}</td>
						<td>
							<input type="number" step="any" min="1"  name="${tempRent.rentID}-Amount" placeholder="0.00"/>
							<input type="hidden" name="hotel_id" value="${employee.hotelID }" >
							<input type="hidden" name="employee_ssn" value="${employee.ssn}">
							<input type="submit" name="${tempRent.rentID}-Pay" value="Pay" >		
						
						</td>
					</tr>
				</c:forEach>	
			</table>
		</form>
	</div>
	<br>
	<hr>
	
	<div class="rents-to-archive">
		<h4 align=center>Rents to Archive</h4>
		<form action="ArchiveRentServlet" method="Get">
			<table border=1>
				<tr>
					<th>Customer SSN</th>
					<th>Rent ID</th>
					<th>Room Number</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Rent Rate</th>
					<th>Customer Balance </th>
					<th></th>
				</tr>
				<c:forEach var="tempPastRent" items="${past_rents }">
					<tr>
						<td>${tempPastRent.customerSSN}</td>
						<td>${tempPastRent.rentID }</td>
						<td>${tempPastRent.roomNumber }</td>
						<td>${tempPastRent.startDate }</td>
						<td>${tempPastRent.endDate }</td>
						<td>${tempPastRent.rentRate }</td>
						<td>${tempPastRent.customerBalance} </td>
						<td><input type="hidden" name="hotel_id" value="${employee.hotelID }" >
							<input type="hidden" name="employee_ssn" value="${employee.ssn}">
						<input type="submit" name="${tempPastRent.rentID}-Archive" value="Archive" /></td>			
					</tr>	
				</c:forEach>	
			</table>
		</form>
	</div>
	<br>
	<hr>
	
	<div class="archived-rents">
		<h4 align=center>Archived Rents (Permanent records)</h4>
			<table border = 1>
				<tr>
					<th>Customer SSN</th>
					<th>Rent ID</th>
					<th>Room Number</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Rent Rate</th>
					<th>Customer Balance</th>
				</tr>
				
				<c:forEach var="tempArchivedRent" items="${archived_rents}">
					<tr>
						<td>${tempArchivedRent.customerSSN} </td>
						<td>${tempArchivedRent.rentID} </td>
						<td>${tempArchivedRent.roomNumber} </td>
						<td>${tempArchivedRent.startDate} </td>
						<td>${tempArchivedRent.endDate} </td>
						<td>${tempArchivedRent.rentRate} </td>
						<td>${tempArchivedRent.customerBalance} </td>				
					</tr>
				
				</c:forEach>
			
			</table>
	</div>
	
	<div class="archived-bookings">
		<h4 align=center> Archived Bookings (Permanent records)</h4>
			<table border = 1>
				<tr>
					<th>Customer SSN</th>
					<th>Booking ID</th>
					<th>Room Number</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Price (/night)</th>
				
				</tr>
			
				<c:forEach var="tempArchivedBooking" items="${archived_bookings}">
					<tr>
						<td>${tempArchivedBooking.customerSSN}</td>
						<td>${tempArchivedBooking.bookingID}</td>
						<td>${tempArchivedBooking.roomNumber}</td>
						<td>${tempArchivedBooking.startDate}</td>
						<td>${tempArchivedBooking.endDate}</td>
						<td>${tempArchivedBooking.pricePerNight}</td>
					
					</tr>
	
				</c:forEach>
			</table>
	</div>
	<br>
	<hr>
	
	<div class="hotel-and-room-info" align=center>
		<form action="DisplayHandRServlet" method="GET">
			<input type="hidden" name="hotel_id" value="${employee.hotelID}">
			<input type="hidden" name="employee_ssn" value="${employee.ssn}">
			<input type="hidden" name="password" value="${employee.password}">
			<input type="submit" value="Hotel and Rooms Information">		
		</form>
		
	</div>
	<br>
	<hr>
	<div class="delete-accont" align=right>
		<form action="DeleteAccountServlet">
			<input type="hidden" name="employee_id" value="${employee.ssn}">
			<input type="hidden" name="user_type" value="employee">
			<input type="submit" name="delete_employee" value="Delete Account">
		</form>
	</div>
	

</body>
</html>