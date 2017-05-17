
public class User {

	private int uID;
	private String uFName;
	private String uLName;
	private String uType;
	private String uEmail;
	private String uPass;
	
	public User(int iD, String fName, String lName, String type, String email, String pass){
		this.uID = iD;
		this.uFName = fName;
		this.uLName = lName;
		this.uType = type;
		this.uEmail = email;
		this.uPass = pass;
	}
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
