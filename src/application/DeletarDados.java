package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbIntegrityException;

public class DeletarDados {

    public static void main(String[] args) {
	Connection coon = null;
	PreparedStatement st = null;

	try {
	    coon = DB.getConnection();
	    st = coon.prepareStatement("DELETE FROM department " + "where " + "Id = ?");
	    st.setInt(1, 4);

	    int rowsAfected = st.executeUpdate();

	    System.out.println("Done! Linhas Afetadas: " + rowsAfected);

	} catch (SQLException e) {
	   throw new DbIntegrityException(e.getMessage());
	   
	}finally {
	    DB.closeStatement(st);
	    DB.closeConnection();
	}

    }
}