<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Project login</title>
</head>
<body>
	<%@ page import="java.sql.*"%>
	<%@ page import="javax.sql.*"%>
	<%
String userid=request.getParameter("usr");  
String pwd=request.getParameter("pwd"); 

if(userid.equals("admin") && pwd.equals("adminadmin")){
	response.sendRedirect("AdminMainMenu.jsp");
}
else 
{ 
out.println("Invalid Admin Credentials, Try again"); 
} 
%>
<a href="home.html">Home</a>
</body>
</html>