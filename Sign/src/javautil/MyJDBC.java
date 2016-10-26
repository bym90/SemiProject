package javautil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyJDBC {

	public MyJDBC() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.7:1521:orcl", "scott", "tiger");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public Statement getStmt(Connection con) {
		Statement stmt = null;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stmt;

	}
	// ���� ���⿡ �Լ� �ϳ��� �� �߰��� �����̸�...
	// ����� �ܺ� �ڿ��� �ݾ��־�� �ϴµ�... �̰͵� �Ź� �ʿ��� �۾��̴�.
	// ���� �ݴ� �۾��� con, stmt, rs�� �ݾ��־���Ѵ�.
	// �� 3������ �ϳ��� �Լ��� �̿��ؼ� �ݵ��� ����.

	public PreparedStatement getPstmt(String sql, Connection con){
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	public void close(Object obj){
		// 3���� Ŭ������ ���ÿ� ���� �� �ִ� ���� Object�̴�.
		try {
			// Object�� �� ��ȯ�ؼ� ����ϼ���
			if (obj instanceof Connection) {
				Connection temp = (Connection) obj;
				temp.close();
			}
			else if(obj instanceof Statement){
				Statement temp = (Statement) obj;
				temp.close();
			}
			else if(obj instanceof ResultSet){
				ResultSet temp = (ResultSet) obj;
				temp.close();
			}
			else if(obj instanceof PreparedStatement){
				PreparedStatement temp = (PreparedStatement) obj;
				temp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new MyJDBC();
	}
}
