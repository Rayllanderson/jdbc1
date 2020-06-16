package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;

public class InserindoDados {

    public static void main(String[] args) {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// ---------------INSERINDO DADOS-------------------------

	Connection coon = null;
	PreparedStatement pst = null;

	try {
	    coon = DB.getConnection();
	    pst = coon.prepareStatement("INSERT INTO seller " 
	    + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
	    + "VALUES " 
	    + "(?, ?, ?, ?, ?)", 
	    Statement.RETURN_GENERATED_KEYS);

	    // 1 - primeira interrogação (?)
	    pst.setString(1, "Monkey D. Luffy");
	    pst.setString(2, "MonkeyDLuffy@gmail.com");
	    pst.setDate(3, new java.sql.Date(sdf.parse("05/05/1997").getTime()));
	    pst.setDouble(4, 1500000000.00);
	    pst.setInt(5, 1);

	    int linhasAfetadas = pst.executeUpdate();
	    
	    if (linhasAfetadas > 0 ) {
		ResultSet rs = pst.getGeneratedKeys();
		while (rs.next()) {
		    int id = rs.getInt(1);
		    System.out.println("Done! ID: " + id);
		}
	    }else {
		System.out.println("Nenhuma linha foi afetada");
	    }

	   
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	} finally {
	    DB.closeStatement(pst);
	    DB.closeConnection();
	}

    }

}
