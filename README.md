# ehotel
Hotel-Booking Application (CSI2132 - Database 1 project Winter 2019)<br/>

To access: http://ehotel.cf/ <br/>

To install from source: <br/>
<br/>
In order to run this web application, you will need to have three components installed:<br/>
• Java Development Kit (JDK): https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html <br/>
• Tomcat: https://tomcat.apache.org/download-90.cgi <br/>
• Eclipse IDE for Java EE Developers: <br/>
https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2019-03/R/eclipse-jee-2019-03-R-win32-x86_64.zip. <br/>
<br/>
<br/>
<br/>
About the project: <br/>
<br/>
<strong>Goal:</strong><br/>
This project’s goal is to develop an application that allows customers to book hotel rooms from five different hotel chains. The application has three components, the database, the server-side and the client side. We used PostgreSQL for the implementation of the database, Java for the server-side application, HTML for the client-side application, and Apache Tomcat for the client-server communication.<br/>
<br/>
<br/>
<strong>Project Overview:</strong><br/>
The application consists of three users: customers, employees and an administrator.<br/>

<strong>Administrator account: To login as administrator: SSN: admin password: passadmin</strong><br/>
<br/>
The administrator oversees information about the hotel chains. Particularly, the administrator can see all information pertinent to hotel chains, that is their emails, phone numbers as well as central offices’ addresses. The administrator can also see the hotels owned by the current hotel chains, as well as add and delete hotels owned by a specific chain. Furthermore, the administrator can see two views. View 1: the number of available rooms per area. View 2: the capacity of rooms of specific hotels. An administrator cannot delete their account.<br/>
<br/>
<br/>
Customer account: After signing up customers can access their information by logging in, the customer’s account allows customers to modify their information, see their current bookings and rents, and search for rooms based on different criteria. Once customers find a room of their liking, the can make a booking. A customer cannot delete their account unless all their rents are paid.<br/>
<br/>
<br/>
Employee account: After signing up employees can view and modify their profile information. Furthermore, employees are able to access bookings and rents of the hotel at which they work. As such, employees can see all the current bookings in their hotel, which they can convert to rents. When an employee converts a booking to a rent, they become responsible for it, as such, it will appear under the rents said employee is currently handling (all bookings converted to rents are automatically archived). Being responsible for a rent implies the employee can enter payments for that rent and follow through with it. Furthermore, once a rent’s end date has passed and the rent is fully paid, employees have the option to archive them. Incidentally, employees can see all the archived bookings and rents related to their hotel. Employees can also modify information about the hotel at which they work, including the rooms in that hotel. Further, employees are able to add more rooms to a hotel as well as delete rooms. An employee cannot delete their account unless they have no rents under their responsibility.<br/>
<br/>
Note: This project was built for the purpose of applying database design knowledge aquired in the CSI2132 course (Database 1).<br/>
<br/>
Copyright - Yesmine Zribi (2019)
