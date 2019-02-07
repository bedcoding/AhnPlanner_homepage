package bbs;

// (실제 데이터 접근 객체의 약자) 실제로 DB에 접근해서 어떤 데이터를 빼올 수 있도록 하는 역할을 하는 클래스
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

// 데이터베이스 접근 객체의 약자 (실질적으로 데이터베이스에서 회원 정보를 불러오거나 넣고자 할 때 사용)
public class BbsDAO {
	// Ctrl+Shift+O 눌러서 아래 객체 추가
	private Connection conn;
	private ResultSet rs;

	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";  // 3306은 내 컴퓨터에 설치된 mySQL 그 자체를 뜻함
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 게시판에 글을 작성할 때 현재 서버의 시간을 가져와서 넣어줌
	public String getDate() {
		String SQL = "SELECT NOW()";  // 현재 시간을 가져옴
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";  // 데이터베이스 오류
	}
	
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  // 현재 시간을 가져옴
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫번째 게시물인 경우
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류
	}
	
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());  //getNext() : 다음번에 쓰여야 할 게시물 번호
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);  // 1: 삭제가 되면 0으로 바뀌는 부분
			return pstmt.executeUpdate();  // 성공적으로 수행했다면 0이상의 결과를 반환
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류
	}
	
	
	// 특정한 페이지에 따른 총 10개의 게시글을 가져옴
	public ArrayList<Bbs> getList(int pageNumber) {
		// bbsAvailable = 1 : 삭제가 되지 않아서 Available이 1인 글만 가져옴
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,  getNext() - (pageNumber - 1) * 10);  
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;  // 게시글 리스트 10개 보여주기
	}
	
	
	// 페이징 처리를 위해 존재하는 함수
	public boolean nextPage(int pageNumber) {
		// bbsAvailable = 1 : 삭제가 되지 않아서 Available이 1인 글만 가져옴
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,  getNext() - (pageNumber - 1) * 10);  
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return true;  // 다음 페이지로 넘어갈 수 있음을 알려주는 함수 (다음 페이지가 존재하는가?)
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	// 하나의 글 내용을 불러오는 함수
	public Bbs getBbs(int bbsID) {
		// bbsAvailable = 1 : 삭제가 되지 않아서 Available이 1인 글만 가져옴
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);  
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	// 글 수정
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);  //getNext() : 다음번에 쓰여야 할 게시물 번호
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();  // 성공적으로 수행했다면 0이상의 결과를 반환
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류
	}
	
	
	// 글 삭제
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);  //getNext() : 다음번에 쓰여야 할 게시물 번호
			return pstmt.executeUpdate();  // 성공적으로 수행했다면 0이상의 결과를 반환
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류
	}
}