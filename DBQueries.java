import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBQueries {
	private static final String URL = "jdbc:mysql://localhost:3306/itstool";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	
	public String errorReport = null;
	private Connection connection = null;
	private PreparedStatement selectAllTickets = null;
	private PreparedStatement selectAllSpecificTickets = null;
	private PreparedStatement selectMyTickets = null;
	private PreparedStatement selectMySpecificTickets = null;
	private PreparedStatement insertTicket = null;
	private PreparedStatement updateTicket = null;
	private PreparedStatement removeTicket = null;
	private static PreparedStatement selectUser = null;
	private PreparedStatement selectUserByID = null;
	private static PreparedStatement insertUser = null;
	
	public DBQueries(){
		try{
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Starts a connection to the database
			selectAllTickets = connection.prepareStatement("SELECT * FROM ticket ORDER BY ticketStatus, ticketTitle");
			selectAllSpecificTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketStatus = ? ORDER BY ticketTitle");
			selectMyTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketUID = ? ORDER BY ticketStatus, ticketTitle");
			selectMySpecificTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketStatus = ? AND ticketUID = ? ORDER BY ticketTitle");
			selectUser = connection.prepareStatement("SELECT * FROM user WHERE userEmail = ? AND userPass = ?");
			selectUserByID = connection.prepareStatement("SELECT * FROM user WHERE userID = ?");
			insertTicket = connection.prepareStatement("INSERT INTO ticket (ticketTitle, ticketUID, ticketDesc, ticketStatus, ticketCreated) VALUES (?,?,?,?,?)");
			updateTicket = connection.prepareStatement("UPDATE ticket SET ticketTitle = ?, ticketDesc = ?, ticketStatus = ? WHERE ticketID = ?");
			insertUser = connection.prepareStatement("INSERT INTO user (userFName, userLName, userType, userEmail, userPass) VALUES (?,?,?,?,?)");
			removeTicket = connection.prepareStatement("DELETE FROM ticket WHERE ticketID = ?");
			errorReport = null;
		}
		catch(SQLException sqlException){
			//sqlException.printStackTrace();
			System.out.println("Unable to connect to the db.");
			errorReport = "Connection error";
			//System.exit(1);
		}
		
	}
	
	public ArrayList<Ticket> getTickets(String[] query){
		ArrayList<Ticket> results = null;
		ResultSet resultSet = null;
		
		try
		{
			if(query[0] == "2"){
				selectMyTickets.setString(1, query[2]);
				resultSet = selectMyTickets.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query
				results = new ArrayList<Ticket>();
			}else if(query[0] == "3"){
				selectAllSpecificTickets.setString(1, query[1]);
				resultSet = selectAllSpecificTickets.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query
				results = new ArrayList<Ticket>();
			}else if(query[0] == "4"){
				selectMySpecificTickets.setString(1, query[1]);
				selectMySpecificTickets.setString(2, query[2]);
				resultSet = selectMySpecificTickets.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query
				results = new ArrayList<Ticket>();
			}else{
				resultSet = selectAllTickets.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query
				results = new ArrayList<Ticket>();
			}
			
		
			while(resultSet.next()) // for each row returned by the select query...
			{
				results.add(new Ticket(
					resultSet.getInt("ticketID"),
					resultSet.getInt("ticketUID"),
					resultSet.getString("ticketTitle"),
					resultSet.getString("ticketDesc"),
					resultSet.getString("ticketStatus"),
					resultSet.getString("ticketCreated")));
			}
		} // end try
		catch (SQLException sqlException)
		{
			//sqlException.printStackTrace();
			errorReport = "Connection error";
			System.out.println("No connection");
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException sqlException)
			{
				//sqlException.printStackTrace();
			}
		} // end finally
		
		return results;
	
	}
	
	public void editTicket(int id, String title, String desc, String status){
		try {
			updateTicket.setString(1, title);
			updateTicket.setString(2,desc);
			updateTicket.setString(3,status);
			updateTicket.setInt(4,id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error adding variable to preparedStatement.");
		}finally{
			try {
				int result = updateTicket.executeUpdate();
				System.out.println("Edited: "+result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error in executing update.");
			}
		}
		
	}
	
	public void insertNewTicket(int uid, String title, String desc, String status, Date date){
		try {
			insertTicket.setString(1, title);
			insertTicket.setInt(2, uid);
			insertTicket.setString(3, desc);
			insertTicket.setString(4, status);
			insertTicket.setDate(5, date);
			insertTicket.setDate(6, date);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				insertTicket.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Faaailed");
			}
		}
	}
	
	public void deleteTicket(int ID){
		try {
			removeTicket.setInt(1,ID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				removeTicket.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to remove ticket");
				e.printStackTrace();
			}
		}
	}
	
	public static User findUser(String email, String password){
		ResultSet resultSet = null;
		User results = null;
		try
		{
			selectUser.setString(1, email);
			selectUser.setString(2, password);
			resultSet = selectUser.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query
		
			while(resultSet.next()) // for each row returned by the select query...
			{
				results = new User(
						resultSet.getInt("userID"),
						resultSet.getString("userFName"),
						resultSet.getString("userLName"),
						resultSet.getString("userType"),
						resultSet.getString("userEmail"),
						resultSet.getString("userPass"));
					 // get the value associated to the year column
			}
		} // end try
		catch (SQLException sqlException)
		{
			System.out.println("No user found.");
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException sqlException)
			{
				System.out.println("No user found.");
				sqlException.printStackTrace();
			}
		} // end finally
		
		return results;
	}
	
	public String findUserByID(int ID){
		ResultSet resultSet = null;
		String results = null;
		try
		{
			selectUserByID.setInt(1, ID);
			resultSet = selectUserByID.executeQuery(); // Here is where we actually execute the select query. resultSet contains the rows returned by the query

			while(resultSet.next()){
				results = resultSet.getString("userFName")+" "+resultSet.getString("userLName");
			}
			
		} // end try
		catch (SQLException sqlException)
		{
			System.out.println("No user found.");
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException sqlException)
			{
				System.out.println("No user found2.");
				sqlException.printStackTrace();
			}
		} // end finally
		
		return results;
	}
	
	public String registerNewUser(String email, char[] password, String type, String fName, String lName){
		String pass = "";
		for(int i=0;i<password.length;i++){
			pass = pass + password[i];
		}
		
		try {
			insertUser.setString(1, fName);
			insertUser.setString(2, lName);
			insertUser.setString(3, type);
			insertUser.setString(4, email);
			insertUser.setString(5, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error1";
		}finally{
			try {
				insertUser.execute();
				return "Success";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "Error2";
			}
		}
		
	}
}