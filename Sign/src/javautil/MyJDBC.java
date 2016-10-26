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
	// 내일 여기에 함수 하나를 더 추가할 예정이며...
	// 사용한 외부 자원은 닫아주어야 하는데... 이것도 매번 필요한 작업이다.
	// 원래 닫는 작업은 con, stmt, rs도 닫아주어야한다.
	// 이 3가지를 하나의 함수를 이용해서 닫도록 하자.

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
		// 3개의 클래스를 동시에 받을 수 있는 것은 Object이다.
		try {
			// Object는 형 변환해서 사용하세요
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
