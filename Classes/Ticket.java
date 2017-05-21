//Class used as a framework for Ticket objects
public class Ticket {

	//Private variables corresponding the values found in the database
	private int tID, tUID;
	private String tTitle;
	private String tDesc;
	private String tStatus;
	private String tCreated;
	
	//The class constructor which assigns values to the private variables
	public Ticket(int ID,int UID, String Title, String Desc, String Status, String Created){
		this.tID = ID;
		this.tUID = UID;
		this.tTitle = Title;
		this.tDesc = Desc;
		this.tStatus = Status;
		this.tCreated = Created;
	}
	//Public methods which allow other classes to read the private values
	public int getID(){
		return this.tID;
	}
	public int getUID(){
		return this.tUID;
	}
	public String getTitle()
	{
		return this.tTitle;
	}
	public String getDesc()
	{
		return this.tDesc;
	}
	public String getStatus()
	{
		return this.tStatus;
	}
	public String getCreated()
	{
		return this.tCreated;
	}
}
