import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBQueries {
	//Variables for the database connection
	private static final String URL = "jdbc:mysql://localhost:3306/itstool";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	
	//Variable used to report errors to the UI
	private String errorReport = null;
	
	//Variables for SQL operations
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
	
	//Class constructor
	public DBQueries(){
		try{
			//Setting values for the preparedStatements
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			selectAllTickets = connection.prepareStatement("SELECT * FROM ticket ORDER BY ticketStatus, ticketCreated");
			selectAllSpecificTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketStatus = ? ORDER BY ticketCreated");
			selectMyTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketUID = ? ORDER BY ticketStatus, ticketCreated");
			selectMySpecificTickets = connection.prepareStatement("SELECT * FROM ticket WHERE ticketStatus = ? AND ticketUID = ? ORDER BY ticketCreated");
			selectUser = connection.prepareStatement("SELECT * FROM user WHERE userEmail = ? AND userPass = ?");
			selectUserByID = connection.prepareStatement("SELECT * FROM user WHERE userID = ?");
			insertTicket = connection.prepareStatement("INSERT INTO ticket (ticketTitle, ticketUID, ticketDesc, ticketStatus, ticketCreated) VALUES (?,?,?,?,?)");
			updateTicket = connection.prepareStatement("UPDATE ticket SET ticketTitle = ?, ticketDesc = ?, ticketStatus = ? WHERE ticketID = ?");
			insertUser = connection.prepareStatement("INSERT INTO user (userFName, userLName, userType, userEmail, userPass) VALUES (?,?,?,?,?)");
			removeTicket = connection.prepareStatement("DELETE FROM ticket WHERE ticketID = ?");
			errorReport = "Working";
		}
		//If a sqlException is caught messages are sent to the console and errorReport is given a new value
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			System.out.println("Unable to connect to the db.");
			errorReport = "Connection Error";
		}
		
	}
	
	//This method allows other classes to read the variable "errorReport"
	public String getError(){
		return errorReport;
	}
	
	//Method used for fetching a list of tickets from the database
	public ArrayList<Ticket> getTickets(String[] query){
		//Creating the ArrayList for the tickets.
		ArrayList<Ticket> results = null;
		ResultSet resultSet = null;
		
		try
		{
			//Different preparedStatements are used depending on the value of query[0]
			if(query[0] == "2"){
				selectMyTickets.setString(1, query[2]);
				resultSet = selectMyTickets.executeQuery();
				results = new ArrayList<Ticket>();
			}else if(query[0] == "3"){
				selectAllSpecificTickets.setString(1, query[1]);
				resultSet = selectAllSpecificTickets.executeQuery(); 
				results = new ArrayList<Ticket>();
			}else if(query[0] == "4"){
				selectMySpecificTickets.setString(1, query[1]);
				selectMySpecificTickets.setString(2, query[2]);
				resultSet = selectMySpecificTickets.executeQuery(); 
				results = new ArrayList<Ticket>();
			}else{
				resultSet = selectAllTickets.executeQuery(); 
				results = new ArrayList<Ticket>();
			}
			
			//New ticket objects are created and placed into the ArrayList
			while(resultSet.next())
			{
				results.add(new Ticket(
					resultSet.getInt("ticketID"),
					resultSet.getInt("ticketUID"),
					resultSet.getString("ticketTitle"),
					resultSet.getString("ticketDesc"),
					resultSet.getString("ticketStatus"),
					resultSet.getString("ticketCreated")));
			}
		} //Checking for a sqlException
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			errorReport = "Connection Error";
			System.out.println("No connection");
			return null;
		}
		finally
		{
			try
			{
				//Closing the resultSet if it isn't null
				if(resultSet != null){
					resultSet.close();
				}
				
			}
			catch (SQLException sqlException)
			{
				sqlException.printStackTrace();
				System.out.println("Error when closing the resultSet.");
			}
		}
		//Finally returning the ArrayList filled with Tickets
		return results;
	
	}
	
	//Method for editing an existing ticket
	public void editTicket(int id, String title, String desc, String status){
		try {
			//Adding variables to the preparedStatement
			updateTicket.setString(1, title);
			updateTicket.setString(2,desc);
			updateTicket.setString(3,status);
			updateTicket.setInt(4,id);
		} catch (SQLException e) {
			//Error here isn't expected but is checked for debugging
			e.printStackTrace();
			System.out.println("Error when adding variables to preparedStatement.");
		}finally{
			try {
				//Executing the preparedStatement
				updateTicket.executeUpdate();
			
				//Checking for errors in the update process
			} catch (SQLException e) {
				
				e.printStackTrace();
				errorReport = "Update Error";
				System.out.println("Error in executing update.");
			}
		}
		
	}
	
	//Method for adding new tickets to the database
	public void insertNewTicket(int uid, String title, String desc, String status, Date date){
		try {
			//Adding variables to the preparedStatement
			insertTicket.setString(1, title);
			insertTicket.setInt(2, uid);
			insertTicket.setString(3, desc);
			insertTicket.setString(4, status);
			insertTicket.setDate(5, date);
			
			//Checking for SQLException
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error when creating new ticket.");
			
		}finally{
			try {
				//Executing the preparedStatement
				insertTicket.execute();
				
				//Checking for SQLException
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println("Error occurred while adding a new ticket to the db.");
				errorReport = "Insert Error";
				
			}
		}
	}
	
	//Method for deleting a ticket from the database
	public void deleteTicket(int ID){
		try {
			//Adding a variable to the preparedStatement
			removeTicket.setInt(1,ID);
			
			//Checking for a SQLException
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("Error while deleting a ticket.");
			
		}finally{
			try {
				//Executing the preparedStatement
				removeTicket.execute();
				
				//Checking for a SQLException
			} catch (SQLException e) {
				
				System.out.println("Error occurred while trying to remove a ticket");
				e.printStackTrace();
				errorReport = "Delete Error";
			}
		}
	}
	
	//Method used to search the database for a user with specific credentials
	public static User findUser(String email, char[] password){
		ResultSet resultSet = null;
		User results = null;
		
		//The JPasswordField returns an array of characters which is made into a String using a for -loop
		String pass = "";
		for(int i=0;i<password.length;i++){
			pass = pass + password[i];
		}
		try
		{
			//Adding variables to the preparedStatement and executing it
			selectUser.setString(1, email);
			selectUser.setString(2, pass);
			resultSet = selectUser.executeQuery(); 
		
			try{
				//Adding the results to a new User object
				while(resultSet.next()){
					results = new User(
							resultSet.getInt("userID"),
							resultSet.getString("userFName"),
							resultSet.getString("userLName"),
							resultSet.getString("userType"),
							resultSet.getString("userEmail"),
							resultSet.getString("userPass"));
				}
				//Checking for a SQLException
			}catch(SQLException e){
				e.printStackTrace();
				System.out.println("Error in attaching values to object User.");
			}
			
		}
		catch (SQLException sqlException)
		{
			//If no user is found the method return a null value
			System.out.println("No user found.");
			sqlException.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				if(resultSet != null){
					resultSet.close();
				}
				
			}
			catch (SQLException sqlException)
			{
				System.out.println("Error occurred while closing the resultSet.");
				sqlException.printStackTrace();
			}
		}
		
		//Return the User object
		return results;
	}
	
	//Method for searching the database for a user with specific ID
	public String findUserByID(int ID){
		ResultSet resultSet = null;
		String results = null;
		try
		{
			//Adding a variable to the preparedStatement and executing it
			selectUserByID.setInt(1, ID);
			resultSet = selectUserByID.executeQuery(); 

			while(resultSet.next()){
				//If a user is found then a String is created containing both the first name and the last name of the user
				results = resultSet.getString("userFName")+" "+resultSet.getString("userLName");
			}
			
		} // end try
		catch (SQLException sqlException)
		{
			System.out.println("Error occurred while searching for a user.");
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				if(resultSet != null){
					resultSet.close();
				}
				
			}
			catch (SQLException sqlException)
			{
				System.out.println("Error while closing resultSet.");
				sqlException.printStackTrace();
			}
		}
		
		//The String containing the users names is returned
		return results;
	}
	
	//Method used for adding a new user to the database
	public String registerNewUser(String email, char[] password, String type, String fName, String lName){
		//The JPasswordField returns an array of characters which is made into a String using a for -loop
		String pass = "";
		for(int i=0;i<password.length;i++){
			pass = pass + password[i];
		}
		
		try {
			//Adding values to the preparedStatement
			insertUser.setString(1, fName);
			insertUser.setString(2, lName);
			insertUser.setString(3, type);
			insertUser.setString(4, email);
			insertUser.setString(5, pass);
			
			try {
				//Executing the preparedStatement and returning a String if successful
				insertUser.execute();
				return "Success";
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Email is already taken.");
				//If an error is caught the method returns error messages
				return "Error2";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred in adding user to database.");
			return "Error1";
		}
		
	}
}