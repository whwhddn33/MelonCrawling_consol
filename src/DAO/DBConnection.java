package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public static Connection conn = null;
	public static final String driver = "oracle.jdbc.driver.OracleDriver";
	public static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
	public static final String user = "web";
	public static final String password = "web";

	private DBConnection() {}
	
	public static Connection getConnection() {
	if (conn == null) {
		try {
			System.out.println("jdbc driver 로딩중");
			Class.forName(driver);
			System.out.println("jdbc driver 로딩성공");
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("오라클 연결 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("오라클 연결 실패(jdbc 드라이버 로딩 실패)");
		} catch (SQLException e) {
			System.out.println("오라클 연결 실패");
		}
	}
	return conn;
	}
	
}
