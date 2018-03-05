package sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Util.SQLUtil;


/**
 * Servlet implementation class SQLGatewayServlet
 */
@WebServlet("/SQLGatewayServlet")
public class SQLGatewayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SQLGatewayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config); //added due to web page posting suggestion to correct ServletContext being null
		try{ 
			/*				
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
Connection con = null;
			
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setIntegratedSecurity(true);
			ds.setServerName("localhost");
			ds.setPortNumber(54987); 
			ds.setDatabaseName("test");
			con = ds.getConnection();
			
			
			String dbURL = "jdbc:sqlserver://localhost\\C:\\Program Files\\Microsoft SQL Server\\MSSQL13.DEV\\MSSQL";
			String username = "";
			String password = "";
			connection = DriverManager.getConnection(dbURL, username, password);
		*/
			
			
		    String url = "jdbc:sqlserver://localhost:1434;databaseName=test;integratedSecurity=true";
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		    connection = DriverManager.getConnection(url);
		}
		catch(ClassNotFoundException e){
			System.out.println("Database driver not found.");
		}
		catch(SQLException e){
			System.out.println("Error opening the db connection: "
					+ e.getMessage()); 
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try{
			connection.close();
		}
		catch(SQLException e){
			System.out.println("Error closing the db connection: "
					+ e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String sqlStatement = request.getParameter("sqlStatement");
		String message = "";
		
		try{
	/*		String contextPath = getServletContext().getContextPath();
			System.out.println(contextPath); */
			Statement statement = connection.createStatement();
			sqlStatement = sqlStatement.trim();
			String sqlType = sqlStatement.substring(0, 6);
			if (sqlType.equalsIgnoreCase("select")){
				ResultSet resultSet = statement.executeQuery(sqlStatement);
				//create a string that contains a HTML-formatted result set
				message = SQLUtil.getHtmlRows(resultSet);
				
				
			}
			else
			{
				int i = statement.executeUpdate(sqlStatement);
				if (i ==0) //this is a DDL statement
					message = "The statement executed successfully.";
				else
					message = "the statment executed successfully.<br>"
							+ i + " row(s) affected.";
			}
			statement.close();

			
		}
		catch (SQLException e){
			message = "Error executing the SQL statement: <br>"
					+ e.getSQLState();
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		session.setAttribute("sqlStatement", sqlStatement);
//try{		
		String contextPath = getServletContext().getContextPath();
		System.out.println(contextPath);
	
		RequestDispatcher dispatcher =
			getServletContext().getRequestDispatcher(
						"/SqlGateway.jsp"); 
		System.out.println(dispatcher.toString());
		dispatcher.forward(request,  response);
 //  }
/*catch (NullPointerException e){
	
	System.out.println(e.getMessage());
	
}*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

