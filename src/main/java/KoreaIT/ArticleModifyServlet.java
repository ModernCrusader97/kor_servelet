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

@WebServlet("/article/modify")
public class ArticleModifyServlet extends HttpServlet {
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

			
			HttpSession session = request.getSession();
			Integer loginedMemberId = (Integer) session.getAttribute("loginedMemberId");
			
			if (loginedMemberId == null) {
			    response.getWriter().append("<script>alert('로그인 후 이용해주세요.'); location.replace('../member/login');</script>");
			    return;
			}
			

			
			int id = Integer.parseInt(request.getParameter("id"));
			SecSql sql = SecSql.from("SELECT * ");
			sql.append("FROM article ");
			sql.append( "WHERE id = ?", id);
			
			Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);
			
			if (articleRow == null) {
			    response.getWriter().append("<script>alert('존재하지 않는 게시글입니다.'); history.back();</script>");
			    return;
			}
			
			int articleMemberId = (int) articleRow.get("memberId");

			if (loginedMemberId != articleMemberId) {
			    response.getWriter().append("<script>alert('해당 게시글의 수정 권한이 없습니다.'); history.back();</script>");
			    return;
			}

			request.setAttribute("articleRow", articleRow);

			request.getRequestDispatcher("/jsp/article/modify.jsp").forward(request, response);

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