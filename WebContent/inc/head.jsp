<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="static/favicon.ico">
<link rel="icon" type="image/x-icon" href="static/favicon.ico">
<link rel="stylesheet" href="static/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="static/jquery-1.9.1.min.js"></script>
<script src="static/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="static/jquery-validation/1.15.0/jquery.validate.min.js"></script>
<script src="static/jquery-validation/1.15.0/localization/messages_zh.min.js"></script>