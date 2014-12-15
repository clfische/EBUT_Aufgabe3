<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<title>ImportBMEcat</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>
<h1>Import</h1>
<%@ include file="header.jsp" %>

<%@ include file="authentication.jsp" %>
<%@ include file="navigation.jspfragment" %>

<form method="POST" enctype="multipart/form-data" action="<%= response.encodeURL("controllerservlet?action=" + Constants.ACTION_UPLOAD_FILE) %>"> 
  File to upload: <input type="file" name="Datei"><br/>
  <br/>
  <input type="submit" value="Press"> to upload the file!
</form>

<%@ include file="error.jsp"%>
</body>
</html>