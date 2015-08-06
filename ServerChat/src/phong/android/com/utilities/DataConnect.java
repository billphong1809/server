package phong.android.com.utilities;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnect {
	public static Connection getConnection() {
		Connection con = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chat_users", "root", "vanphong");
			con.setAutoCommit(true);

		} catch (Exception e) {

			System.out.println("Database.getConnection() Error -->"
					+ e.getMessage());
			e.printStackTrace();
		}
		return con;
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}