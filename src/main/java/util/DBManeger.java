package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManeger {

	String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "pass";

	public Connection getConnection() throws SQLException {

		Connection conn = null;

        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(false);

        return conn;
	}
}
