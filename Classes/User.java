//Class used as a framework for User objects
public class User {

	//Private variables corresponding the values found in the database
	private int uID;
	private String uFName;
	private String uLName;
	private String uType;
	private String uEmail;
	private String uPass;
	
	//The class constructor which assigns values to the private variables
	public User(int iD, String fName, String lName, String type, String email, String pass){
		this.uID = iD;
		this.uFName = fName;
		this.uLName = lName;
		this.uType = type;
		this.uEmail = email;
		this.uPass = pass;
	}
	
	//Public methods which allow other classes to read the private values
	public int getID()
	{
		return this.uID;
	}
	public String getFName()
	{
		return this.uFName;
	}
	public String getLName()
	{
		return this.uLName;
	}
	public String getType()
	{
		return this.uType;
	}
	public String getEmail()
	{
		return this.uEmail;
	}
	public String getPass()
	{
		return this.uPass;
	}

}
