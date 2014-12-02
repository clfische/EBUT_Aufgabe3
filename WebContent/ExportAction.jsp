<%@ page session="true"
	import="de.htwg_konstanz.ebus.framework.wholesaler.api.bo.*,de.htwg_konstanz.ebus.framework.wholesaler.api.boa.*,java.math.BigDecimal,de.htwg_konstanz.ebus.framework.wholesaler.vo.util.PriceUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>eBusiness Framework Demo - Product List NEW</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<br>
	<%@ include file="authentication.jsp"%>
	<br>
	<%@ include file="navigation.jspfragment"%>

	<h1>Export</h1>

	<br />
	<%@ include file="error.jsp"%>

	<form
		action="<%=response.encodeURL("controllerservlet?action=" + Constants.ACTION_EXPORT_FILE)%>"
		method="POST">
		<input name="Datei" type="submit" value="start export"> <input
			type="radio" name="exportType" value="XML" checked>XML <input
			type="radio" name="exportType" value="XHTML"> XHTML<br>
	</form>
	<br>
	<br>
	<form
		action=" <%=response.encodeURL("controllerservlet?action=" + Constants.ACTION_EXPORT_FILE)%>"
		method="post">
		<p>
			<input type="submit" value="start" name="search"> search and
			export: <input name="searchString" type="text" size="12" maxlength="256">
		</p>

	</form>

</body>
</html>