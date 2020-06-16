package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;

public class AtualizarDados {

    public static void main(String[] args) {
	
	Connection coon = null;
	PreparedStatement st = null;
	
	try {
	    coon = DB.getConnection();
	    st = coon.prepareStatement("update seller "
	    	+ "set BaseSalary "
	    	+ "= BaseSalary + ? "
	    	+ "where "
	    	+ "(DepartmentId = ?)");
	    
	    st.setDouble(1, 40.00);
	    st.setInt(2, 2);

	    int rowsAfected = st.executeUpdate();
	    
	    System.out.println("Done! Linhas Afetadas: " + rowsAfected);
	    
	}catch (SQLException e) {
	    e.printStackTrace();
	}finally {
	    DB.closeStatement(st);
	    DB.closeConnection();
	}

    }

}
