package KoreaIT;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import KoreaIT.SecSql;
import KoreaIT.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/member/doLogin")
public class MemberDoLoginServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 없음");
			e.printStackTrace();
		}

		String url = "jdbc:mysql://127.0.0.1:3306/AM_jsp_2026_02?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
		String user = "root";
		String password = "";

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, user, password);
			response.getWriter().append("연결 성공");


			String loginId = request.getParameter("loginId");
			String loginPw = request.getParameter("loginPw");
			
			SecSql sql = SecSql.from("SELECT *");
		    sql.append("FROM `member` ");
		    sql.append("WHERE loginId = ?", loginId);

		    Map<String, Object> memberRow = DBUtil.selectRow(conn, sql);
			
		    if (memberRow.isEmpty()) {
		        response.getWriter().append("<script>alert('존재하지 않는 아이디입니다.'); history.back();</script>");
		        return;
		    }
		    
		    if (memberRow.get("loginPw").equals(loginPw) == false) {
		        response.getWriter().append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
		        return;
		    }
		    String name = (String) memberRow.get("name");
		    
		    // 로그인 성공 처리
		    HttpSession session = request.getSession();
		    session = request.getSession();
		    session.setAttribute("loginedMemberId", memberRow.get("id"));
		    session.setAttribute("loginedMemberName", name);
			response.getWriter()
					.append(String.format("<script>alert('%s님, 반갑습니다!'); location.replace('../home/main')</script>", name));

		} catch (SQLException e) {
			System.out.println("에러 : " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}