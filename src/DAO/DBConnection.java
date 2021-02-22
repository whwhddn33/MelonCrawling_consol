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
			System.out.println("jdbc driver �ε���");
			Class.forName(driver);
			System.out.println("jdbc driver �ε�����");
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("����Ŭ ���� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����Ŭ ���� ����(jdbc ����̹� �ε� ����)");
		} catch (SQLException e) {
			System.out.println("����Ŭ ���� ����");
		}
	}
	return conn;
	}
	
}
