<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // 세션에서 정보 꺼내기
    Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
    String loginedMemberName = (String) session.getAttribute("loginedMemberName");
    
    // 로그인이 되었는지 여부를 판단하는 변수
    boolean isLogined = (loginedMemberId != null);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>
<h1>메인 페이지</h1>

	<%
	if (isLogined) {
	%>
	<div>
		현재
	<%=loginedMemberName%>(<%=loginedMemberId%>)님 로그인 중
	</div>
	<div>
		<a href="../member/doLogout">로그아웃</a>
	</div>
	<%
	}
	%>
	<%
	if (!isLogined) {
	%>
	<div>
	 
	</div>
	<div>
		<a href="../member/login">로그인</a>
	</div>
	<%
	}
	%>
<ul>
<li>
<a href="../article/list">리스트로 이동</a>
</li>
<li>
<a href="../article/write">글쓰기 이동</a>
</li>


<li>
<a href="../member/join">회원가입 이동</a>
</li>


</ul>
</body>
</html>