package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;

public class Program {

    public static void main(String[] args) {

	Connection coon = null;
	Statement st = null;
	ResultSet rs = null;

	try {
	    coon = DB.getConnection();

	    st = coon.createStatement();

	    rs = st.executeQuery("select * from department");

	    while (rs.next()) {
		// pega o inteiro que ta no campo 'id':
		System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} finally {
	    if (rs != null) {
		DB.closeResultSet(rs);
		DB.closeStatement(st);
		DB.closeConnection();
	    }
	}
    }
}
