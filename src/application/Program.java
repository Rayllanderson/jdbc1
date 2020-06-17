package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import db.DB;
import db.DbException;

public class Program {

    static Scanner scan = new Scanner(System.in);
    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {

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
		} finally {
		    DB.closeResultSet(imprimir);
		}
	    case 2:
		Statement stImp = null;
		PreparedStatement stA = null;
		ResultSet rsImp = null;
		ResultSetMetaData rsmd = null;

		try {
		    if (coon == null) {
			coon = DB.getConnection();
		    }
		    stImp = coon.createStatement();
		    rsImp = stImp.executeQuery("select * from seller");
		    imprimirColunas(rsImp, rsmd);
		    System.out.println("\nQual campo você gostaria de atualizar? ");
		    int num = scan.nextInt();
		    String opcaoAlterar = escolha(rsImp, rsmd, num);
		    imprimirIdName(rsImp, stImp, opcaoAlterar);
		    int id = scan.nextInt();
		    imprimirADdosAtuais(rsImp, stImp, opcaoAlterar, id);
		    atualizarDados(opcaoAlterar, id, stA, coon);
		    break;

		} catch (SQLException e) {
		    e.printStackTrace();
		} catch (ParseException e) {
		    e.printStackTrace();
		} finally {
		    DB.closeResultSet(rsImp);
		    DB.closeStatement(stA);
		    DB.closeStatement(stImp);
		}

		// System.out.println("ops... essa função ainda não está disponível :( volte
		// mais tarde");

	    case 3:
		Statement stm = null;
		ResultSet rs = null;
		try {
		    if (coon == null) {
			coon = DB.getConnection();
		    }

		    stm = coon.createStatement();

		    rs = stm.executeQuery(
			    "select * from seller, department" + " where seller.DepartmentId = department.Id");

		    System.out.println("ID  |  NOME   |  BIRTHDATE   |   SALARY   | DEPARTMENT ID");
		    while (rs.next()) {
			System.out.println(rs.getInt("Id") + ", " + rs.getString("Name") + ", "
				+ rs.getDate("BirthDate", sdf.getCalendar()) + ", R$" + rs.getDouble("BaseSalary")
				+ ", " + rs.getInt("DepartmentId"));
		    }
		    break;
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
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

    public static void addSeller(Connection coon, PreparedStatement st, String nome, String email, Date date,
	    double salary, int department) {

	coon = DB.getConnection();
	ResultSet rs = null;
	try {
	    st = coon.prepareStatement("INSERT INTO seller" + " (Name, Email, BirthDate, BaseSalary, DepartmentId) "
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
	} finally {
	    DB.closeResultSet(rs);
	}
    }

    public static void imprimirColunas(ResultSet rsImp, ResultSetMetaData rsmd) throws SQLException {

	rsmd = rsImp.getMetaData();
	int columnCount;
	columnCount = rsmd.getColumnCount();

	for (int i = 2; i <= columnCount; i++) {
	    String name = rsmd.getColumnName(i);
	    System.out.println("[ " + i + " ] - " + name);
	}

    }

    private static void imprimirADdosAtuais(ResultSet rsImp, Statement stImp, String opcaoAlterar, int id)
	    throws SQLException {
	System.out.println("Dados atuais: ");

	if (opcaoAlterar != "Name") {
	    rsImp = stImp.executeQuery("select Name, " + opcaoAlterar + " from seller where Id = " + id);
	} else {
	    rsImp = stImp.executeQuery("select " + opcaoAlterar + " from seller where Id = " + id);
	}
	while (rsImp.next()) {
	    if (opcaoAlterar.equals("BaseSalary")) {
		System.out.println(rsImp.getString("Name") + " - " + rsImp.getDouble(opcaoAlterar));
	    } else if (opcaoAlterar.equals("Name") || opcaoAlterar.equals("Email")) {
		System.out.println(rsImp.getString("Name") + " - " + rsImp.getString(opcaoAlterar));
	    } else if (opcaoAlterar.equals("DepartmentId")){
		System.out.println(rsImp.getString("Name") + " - " + rsImp.getInt(opcaoAlterar));
	    }else {
		System.out.println(rsImp.getString("Name") + " - " + rsImp.getDate(opcaoAlterar));
	    }
	}
    }

    public static String escolha(ResultSet rsImp, ResultSetMetaData rsmd, int num) {
	try {
	    rsmd = rsImp.getMetaData();
	    return rsmd.getColumnName(num);

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private static void imprimirIdName(ResultSet rsImp, Statement stImp, String opcaoAlterar) throws SQLException {
	System.out.println("Escolha pelo ID a pessoa que você deseja alterar o " + opcaoAlterar);

	rsImp = stImp.executeQuery("select Id, Name from seller");

	while (rsImp.next()) {
	    System.out.println(rsImp.getInt("Id") + " - " + rsImp.getString("Name"));
	}

    }

    public static void atualizarDados(String opcaoAlterar, int id, PreparedStatement stA, Connection coon)
	    throws SQLException, ParseException {
	stA = coon.prepareStatement("update seller set " + opcaoAlterar + " = ? where Id = " + id);

	if (opcaoAlterar.equals("BaseSalary")) {
	    System.out.println("Novo valor: R$");
	    double newSalary = scan.nextDouble();
	    stA.setDouble(1, newSalary);

	} else if (opcaoAlterar.equals("Name") || opcaoAlterar.equals("Email")) {
	    System.out.print("Novo valor: ");
	    String newValue = scan.nextLine();
	    stA.setString(1, newValue);
	} else if (opcaoAlterar.equals("DepartmentId")) {
	    System.out.print("Novo valor: ");
	    int deparmentId = scan.nextInt();
	    stA.setInt(1, deparmentId);
	}else {
	    System.out.print("Nova data (dd/mm/aaaa): ");
	    Date date = sdf.parse(scan.next());
	    stA.setDate(1, new java.sql.Date(date.getTime()));
	}

	int rowsAfected = stA.executeUpdate();
	System.out.println("Done! Linhas Afetadas: " + rowsAfected);
    }

}
