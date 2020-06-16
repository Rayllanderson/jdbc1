package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import db.DB;
import db.DbException;

public class Program {

    public static void main(String[] args) {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	Scanner scan = new Scanner(System.in);

	boolean flag = false;
	
	Connection coon = null;
	PreparedStatement st = null;

	while (!flag) {

	    System.out.println("[ 1 ] - Adicionar Vendedor");
	    System.out.println("[ 2 ] - Atualizar Dados");
	    System.out.println("[ 3 ] - Imprimir Tabela Vendedor");
	    System.out.println("[ 4 ] - SAIR");
	    int op = scan.nextInt();

	    switch (op) {
	    case 1:
		 ResultSet imprimir = null;
		System.out.println("Adicionar vendedor");
		System.out.print("Nome: ");
		try {
		    
		    Connection c1 = DB.getConnection();
		    Statement s1 = c1.createStatement();
		    imprimir = s1.executeQuery("select Id, Name from department");
		    
		    scan.nextLine();
		    String nome = scan.nextLine();
		    System.out.print("Email: ");
		    String email = scan.nextLine();
		    System.out.print("Data Nascimento (dd/mm/aaaa): ");
		    Date date = sdf.parse(scan.next());
		    System.out.print("Salário: R$");
		    double salary = scan.nextDouble();
		    System.out.println("Departamento: ");
		    while (imprimir.next()) {
			System.out.println("[ " + imprimir.getInt("Id") + " ] - " + imprimir.getString("Name"));
		    }
		    int department = scan.nextInt();

		    addSeller(coon, st, nome, email, date, salary, department);

		    System.out.println();
		    break;

		} catch (ParseException e) {
		    e.printStackTrace();
		} catch (SQLException e) {
		    e.printStackTrace();
		}finally {
		    DB.closeResultSet(imprimir);
		}
	    case 2:
		System.out.println("ops... essa função ainda não está disponível :( volte mais tarde");
		break;
	    case 3:
		Statement stm = null;
		ResultSet rs = null;
		try {
		    if (coon == null) {
			coon = DB.getConnection();
		    }
		    
		    stm = coon.createStatement();
		    
		    rs = stm.executeQuery("select * from seller, department"
		    	+ " where seller.DepartmentId = department.Id");
		    
		    System.out.println("ID  |  NOME   |  BIRTHDATE   |   SALARY   | DEPARTMENT ID");
		    while (rs.next()) {
			System.out.println(rs.getInt("Id") + ", " + rs.getString("Name")
			+ ", " + rs.getDate("BirthDate", sdf.getCalendar())
			+ ", R$" + rs.getDouble("BaseSalary")
			+", " + rs.getInt("DepartmentId"));
		    }
		    break;
		} 
		catch (SQLException e) {
		    e.printStackTrace();
		}finally {
		    DB.closeResultSet(rs);
		}
		
	    case 4:
		
		DB.closeStatement(st);
		DB.closeConnection();
		flag = true;
		break;
	    }
	}
	scan.close();
    }

    public static void addSeller(Connection coon, PreparedStatement st, String nome, String email, Date date, double salary, int department) {

	coon = DB.getConnection();
	ResultSet rs = null;
	try {
	    st = coon.prepareStatement("INSERT INTO seller" 
	+ " (Name, Email, BirthDate, BaseSalary, DepartmentId) "
        + "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

	    st.setString(1, nome);
	    st.setString(2, email);
	    st.setDate(3, new java.sql.Date(date.getTime()));
	    st.setDouble(4, salary);
	    st.setInt(5, department);

	    int afectedRows = st.executeUpdate();
	    rs = st.getGeneratedKeys();
	    
	    System.out.println("Done!\nLinhas Afetadas: " + afectedRows);
	    while (rs.next()) {
		int id = rs.getInt(1);
		 System.out.println("Novo cadastro, ID: " + id);
	    }
	   
	} catch (SQLException e) {
	    throw new DbException(e.getMessage());
	}finally {
	    DB.closeResultSet(rs);
	}
    }

}
