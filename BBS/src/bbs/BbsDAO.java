package bbs;

// (���� ������ ���� ��ü�� ����) ������ DB�� �����ؼ� � �����͸� ���� �� �ֵ��� �ϴ� ������ �ϴ� Ŭ����
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// �����ͺ��̽� ���� ��ü�� ���� (���������� �����ͺ��̽����� ȸ�� ������ �ҷ����ų� �ְ��� �� �� ���)
public class BbsDAO {
	// Ctrl+Shift+O ������ �Ʒ� ��ü �߰�
	private Connection conn;
	private ResultSet rs;

	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";  // 3306�� �� ��ǻ�Ϳ� ��ġ�� mySQL �� ��ü�� ����
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �Խ��ǿ� ���� �ۼ��� �� ���� ������ �ð��� �����ͼ� �־���
	public String getDate() {
		String SQL = "SELECT NOW()";  // ���� �ð��� ������
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";  // �����ͺ��̽� ����
	}
	
	
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  // ���� �ð��� ������
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // ù��° �Խù��� ���
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // �����ͺ��̽� ����
	}
	
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());  //getNext() : �������� ������ �� �Խù� ��ȣ
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);  // 1: ������ �Ǹ� 0���� �ٲ�� �κ�
			return pstmt.executeUpdate();  // ���������� �����ߴٸ� 0�̻��� ����� ��ȯ
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // �����ͺ��̽� ����
	}	
}