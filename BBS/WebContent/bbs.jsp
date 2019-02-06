<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<!-- 부트스트랩 -->
<meta name="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="css/bootstrap.css">

<title> JSP 게시판 웹 사이트 </title>
</head>

<body>
	<%
		// 로그인이 된 사람은 그 로그인 정보를 담는다
		String userID = null;
		if(session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
	%>

	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">JSP 웹 사이트</a>
		</div>
		
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav"> 
				<!-- class="active"를 통해 현재 접속한 페이지를 표시한다 -->
				<li><a href="main.jsp">메인</a></li> 
				<li class="active"><a href="bbs.jsp">게시판</a></li>
			</ul>
			<%
				// 로그인이 되어 있지 않는 경우
				if(userID == null) {
			%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"> 접속하기 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="login.jsp"> 로그인 </a></li>
							<li><a href="join.jsp"> 회원가입 </a></li>
						</ul>
					</li>
				</ul>
			<%
				} else {
				// 로그인이 되어 있는 경우
			%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"> 회원관리 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="logoutAction.jsp"> 로그아웃 </a></li>
						</ul>
					</li>
				</ul>
			<%
				}
			%>
		</div>	
	</nav>
	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align; center; border; 1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color; #eeeeee; text-align; center;"> 번호 </th>
						<th style="background-color; #eeeeee; text-align; center;"> 제목 </th>
						<th style="background-color; #eeeeee; text-align; center;"> 작성자 </th>
						<th style="background-color; #eeeeee; text-align; center;"> 작성일 </th>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td> 1 </td>
						<td> 학습플래너 </td>
						<td> 안성민 </td>
						<td> 2019.01.01 </td>
					</tr>
				</tbody>
			</table>
			<a href="write.jsp" class="btn btn-primary pull-right"> 글쓰기 </a>
		</div>
	</div>
	<script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>