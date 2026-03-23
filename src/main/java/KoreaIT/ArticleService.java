package KoreaIT;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import KoreaIT.SecSql;
import KoreaIT.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class ArticleService {
	private Connection conn;
	
	
	public ArticleService(Connection conn) {
        this.conn = conn;
    }
	
	public Map<String, Object> getArticleById(int id) {
	    SecSql sql = SecSql.from("SELECT *");
	    sql.append(" FROM article");
	    sql.append(" WHERE id = ?", id);
	    
	    return DBUtil.selectRow(conn, sql);
	}

    public List<Map<String, Object>> getForPrintArticles(int page, int itemsInAPage)
    { 
    	int limitFrom = (page - 1) * itemsInAPage; 	
    	

    	SecSql sql = SecSql.from("SELECT A.*, M.name AS writerName");
    	sql.append(" FROM article AS A");
    	sql.append(" INNER JOIN `member` AS M");
    	sql.append(" ON A.memberId = M.id");
    	sql.append("ORDER BY A.id DESC");
    	sql.append("LIMIT ?, ?;", limitFrom, itemsInAPage);
    	
    	
    	return DBUtil.selectRows(conn, sql);
    }

    public int getTotalCnt() 
    { 
    	SecSql sql = SecSql.from("SELECT COUNT(*)");
    	sql.append("FROM article");    	
    	return DBUtil.selectRowIntValue(conn, sql);
    }
    
    public Map<String, Object> getForPrintArticleById(int id) 
    {
    	
    	SecSql sql = SecSql.from("SELECT A.*, M.name AS writerName");
    	sql.append(" FROM article AS A");
    	sql.append(" INNER JOIN `member` AS M");
    	sql.append(" ON A.memberId = M.id");
    	sql.append(" WHERE A.id = ?", id);
    	
    	return DBUtil.selectRow(conn, sql);
    }



    public int write(int memberId, String title, String body)
    { 
    	SecSql sql = SecSql.from("INSERT INTO article");
    	sql.append("SET regDate = NOW(),");
    	sql.append("updateDate = NOW(),");
    	sql.append("memberId = ?,", memberId);
    	sql.append("title = ?,", title);
    	sql.append("`body` = ?", body);

    	return  DBUtil.insert(conn, sql);
    }


    public int modify(int id, String title, String body) {
        var sql = SecSql.from("UPDATE article");
        sql.append("SET updateDate = NOW(),");
        sql.append("title = ?,", title);
        sql.append("`body` = ?", body);
        sql.append("WHERE id = ?", id);
        
        return DBUtil.update(conn, sql);
    }

    // 6. 게시글 삭제
    public int delete(int id) 
    {	
    	SecSql sql = SecSql.from("DELETE");
    	sql.append("FROM article");
    	sql.append("WHERE id = ?", id);

    	return DBUtil.delete(conn, sql);    	
    }


    // --- [비즈니스 로직 / 검증 서비스] ---

    // 7. 수정/삭제 권한 확인 로직 (서비스 계층에서 판단)
    public boolean actorCanModify(int loginedMemberId, Map<String, Object> articleRow) 
    {
    	
    }
    
    public boolean actorCanDelete(int loginedMemberId, Map<String, Object> articleRow) 
    {
    	
    }
}
