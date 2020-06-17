package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Transacao {

    public static void main(String[] args) {

	Connection coon = null;
	Statement st = null;

	try {
	    // executei 2 comandos, mas no meio do caminho deu um erro, ouh nous! portanto apenas as opecações antes do erro
	    // funcionaram 
	    
	    coon = DB.getConnection();
	    
	    //nada vai ser processado até que isso aqui seja verdadeiro
	    coon.setAutoCommit(false);
	    
	    st = coon.createStatement();
	    
	    int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 where DepartmentId = 4");
	    
	    
	   // int x = 1;
	    /*if (x < 3) {
		throw new SQLException("fake error");
	    }*/
	
	    
	    int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 where DepartmentId = 3");
	    
	    //confirmando que tudo tá certinho e vai comitar
	    coon.commit();
	    
	    System.out.println("rows 1 = " + rows1 + "\nrows 2 = " + rows2);
	    
	} catch (SQLException e) {
	    //se der merda, volta ao estado normal do banco
	    try {
		coon.rollback();
		throw new DbException("Transação falhou! Banco de dados voltando ao estado anterior\nMotivo: " + e.getMessage());
		
	    } catch (SQLException e1) {
		//aqui é se der erro no rollback, quando tentei voltar e não conseguiu
		throw new DbException("Ops... erro ao tentar voltar a transação.\nMotivo: " + e1.getMessage());
	    }

	} finally {
	    DB.closeStatement(st);
	    DB.closeConnection();
	}

    }

}
