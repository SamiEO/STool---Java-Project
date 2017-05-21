import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.SwingConstants;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.text.AbstractDocument;

//The primary class file used for UI and interacting the the user
public class STool extends JFrame{
	private static final long serialVersionUID = 5251326548716021957L;
	//Private variables and UI elements that need to be declared as class variables
	private JTextField emailField;
	private JPasswordField passwordField;
	private JLabel lErrorLabel, cErrorLabel;
	private JScrollPane scrollPanel;
	private JPanel cardStack, loginContainer, contentContainer, ticketPanel, headerPanel;
	private JButton loginButton, logoutButton;
	private JComboBox<String> ticketFilter, statusFilter;
	private JButton[] buttons;
	private User currentUser;
	private DBQueries dbQueries;
	private ArrayList<Ticket> tickets;
	private Ticket cT;
	//tValues[0] = type of query, tValues[1] = ticketStatus for the query, tValues[2] = userID for the query
	private String[] tValues = {"1","",""};
	private String currentCard;
	
	//The class constructor
	public STool(){
		//Settings for the window
		super("STool - Support Tool");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		//Creating a new object of the DBQueries class
		dbQueries = new DBQueries();
		
	//Creating the UI
		
		//cardStack is  used to switch the view between login screen and the content screen
		currentCard = "login";
		cardStack = new JPanel();
		System.out.println();
		cardStack.setBounds(0, 0, 800, 600);
		getContentPane().add(cardStack);
		cardStack.setLayout(new CardLayout(0, 0));
		
		loginContainer = new JPanel();
		cardStack.add(loginContainer, "loginScreen");
		loginContainer.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.WHITE, null, null, null));
		loginContainer.setLayout(null);
		
		JLabel lblStool = new JLabel("STool");
		lblStool.setBounds(350, 192, 100, 14);
		lblStool.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(lblStool);
		
		emailField = new JTextField();
		//Setting a maximum length for the inputField
		((AbstractDocument)emailField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		emailField.setBounds(300, 233, 200, 20);
		emailField.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(emailField);
			
		passwordField = new JPasswordField();
		//Setting a maximum length for the inputField
		((AbstractDocument)emailField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		passwordField.setBounds(300, 278, 200, 20);
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(passwordField);

		loginButton = new JButton("Login");
		loginButton.setBounds(350, 309, 100, 23);
		//Adding an ActionListener to the button
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(emailField.getText().equals("") || passwordField.getPassword().length == 0){
					lErrorLabel.setText("Please fill both fields");
				}else{
					lErrorLabel.setText("");
					//If a specific error is found in the dbQueries object, it is recreated
					if(dbQueries.getError().equals("Connection Error")){
						
						dbQueries = new DBQueries();
						
						if(!dbQueries.getError().equals("Connection Error")){
							
							//The findUser() -function will return an User object if the database contains a user with a given email and password
							currentUser = DBQueries.findUser(emailField.getText(), passwordField.getPassword());
							
							if(currentUser != null){
								//If a user is found a method is called to switch the card in the cardStack
								switchCards();
							}else{
								lErrorLabel.setText("User not found.");
							}
						}
					}else{
						currentUser = DBQueries.findUser(emailField.getText(), passwordField.getPassword());
						
						if(currentUser != null){
							switchCards();	
						}else{
							lErrorLabel.setText("User not found.");
						}
					}
				}
				
			}
		});
		loginContainer.add(loginButton);
		
		JLabel emailLabel = new JLabel("Email");
		emailLabel.setBounds(350, 217, 100, 14);
		emailLabel.setForeground(Color.GRAY);
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(emailLabel);
		
		JLabel PasswordLabel = new JLabel("Password");
		PasswordLabel.setBounds(350, 264, 100, 14);
		PasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		PasswordLabel.setForeground(Color.GRAY);
		loginContainer.add(PasswordLabel);
		
		lErrorLabel = new JLabel("");
		lErrorLabel.setForeground(Color.RED);
		lErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lErrorLabel.setBounds(250, 377, 300, 14);
		loginContainer.add(lErrorLabel);
		
		//If the registration button is pressed a method is called
		JButton btnRegister = new JButton("Register");
		
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openRegistrationWindow();
			}
		});
		btnRegister.setBounds(350, 343, 100, 23);
		loginContainer.add(btnRegister);
		
		contentContainer = new JPanel();
		cardStack.add(contentContainer, "contentScreen");
		contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.PAGE_AXIS));
		
		headerPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) headerPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentContainer.add(headerPanel);
		
		JButton updateList = new JButton("Refresh");
		headerPanel.add(updateList);
		String[] sFOptions = {"Active tickets","All tickets","Closed tickets"};
		//Fun fact! If the JComboBox warnings are fixed the design view from WindowBuilder stops working
		statusFilter = new JComboBox(sFOptions);
		headerPanel.add(statusFilter);
		
		String[] tFOptions = {"All tickets","My tickets"};
		ticketFilter = new JComboBox(tFOptions);
		headerPanel.add(ticketFilter);
		
		JButton newTicket = new JButton("New ticket");
		headerPanel.add(newTicket);
		
		logoutButton = new JButton("Logout");
		headerPanel.add(logoutButton);
		
		cErrorLabel = new JLabel("");
		cErrorLabel.setForeground(Color.RED);
		cErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cErrorLabel.setPreferredSize(new Dimension(780,20));
		headerPanel.add(cErrorLabel);
		
		//If the logout button is pressed the currenUser variable is set to null and switchCards() -method is called
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentUser = null;
				switchCards();
			}
		});
		
		//If the "New ticket" -button is pressed the openTicketDetails() -method is called with an empty Ticket object
		newTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openTicketDetails(new Ticket(-1,-1,"","","",""));
			}
		});
		
		//ActionListener for refreshing the listed Tickets
		updateList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printTickets();
			}
		});
		
		ticketPanel = new JPanel();
		contentContainer.add(ticketPanel);
		ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
		
		scrollPanel = new JScrollPane(ticketPanel);
		scrollPanel.setPreferredSize(new Dimension(740,520));
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		contentContainer.add(scrollPanel);
		
		if(dbQueries.getError() == "Connection Error"){
			lErrorLabel.setText("Connection Error");
		}

	}
	
	//Private method for printing out Tickets found in the database
	private void printTickets()
	{		
		//Removing all UI elements in the JPanel tickePanel
		ticketPanel.removeAll();
		
		//Calling the method setFilterCriteria to set values for "tValues" which is used to determinate what tickets are fetched
		setFilterCriteria((String)statusFilter.getSelectedItem(),(String)ticketFilter.getSelectedItem());
		
		//getTickets returns an ArrayList full of Ticket objects
		tickets = dbQueries.getTickets(tValues);
		
		//In case of a connection error the method returns a null value and the user is sent back to the login screen
		if(tickets == null){
			CardLayout cl = (CardLayout) cardStack.getLayout();
			lErrorLabel.setText("Connection Error");
			cl.show(cardStack, "loginScreen");
		}else{
			//If the method returns an array a button will be created for each Ticket
			
			buttons = new JButton[tickets.size()];
			
			for (int row=0; row<tickets.size(); row++){
				
				cT = tickets.get(row);
				
				//Each button displays the title and the time of creation of the Ticket
				buttons[row] = new JButton(cT.getTitle()+" | "+cT.getCreated());
				
				//Tickets with "Closed" status are given a grey background
				if(cT.getStatus().equals("Closed")){
					buttons[row].setBackground(Color.GRAY);
				}
				
				//The buttons are given ActionListeners
				buttons[row].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//src is used to identify which button was pressed
						Object src = e.getSource();
						
						//The buttons[] -array is looped through untill the right button is found 
						for(int i=0;i<buttons.length;i++){
							if(buttons[i] == src){
								//A method is called to view the details of the Ticket
								openTicketDetails(tickets.get(i));
							}
						}
					}
				});
				//Adding the button to the JPanel
				ticketPanel.add(buttons[row]);
			}
			//Making sure the changes to the UI are displayed
			ticketPanel.validate();
			ticketPanel.repaint();
			
			//scrollPanel is validated to make sure the scollbar appears when there are enough buttons
			scrollPanel.validate();
		}
		
	}
	//Private method used to set the criteria of which tickets we want to fetch
	private void setFilterCriteria(String sF, String tF){
		//sF = results from statusFilter, tF = results from ticketFilter
		if(sF == "All tickets" && tF == "All tickets"){
			tValues[0] = "1";
		}else if(sF == "All tickets" && tF == "My tickets"){
			tValues[0] = "2";
		}else if(sF != "All tickets" && tF == "All tickets"){
			tValues[0] = "3";
		} if(sF != "All tickets" && tF == "My tickets"){
			tValues[0] = "4";
		}
			
			
		if((String)statusFilter.getSelectedItem() == "Active tickets"){
			tValues[1] = "Active";
		}else{
			tValues[1] = "Closed";
		}
		if((String)ticketFilter.getSelectedItem() == "My tickets"){
			tValues[2] = ""+currentUser.getID()+"";
		}
	}
	
	//Private method used to display the information of a specific Ticket
	private void openTicketDetails(Ticket t){
		
		Date date = new Date();
		
		//sqlDate displays date in same format as the database does
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		//Setting up a new window to display the information
		
		JTextField titleField = new JTextField();
		((AbstractDocument)titleField.getDocument()).setDocumentFilter(new LimitDocumentFilter(60));
	    JTextArea descField = new JTextArea(10, 1);
	    	descField.setLineWrap(true);
	    descField.setWrapStyleWord(true);
	    descField.setBorder(BorderFactory.createLineBorder(Color.black));
	    
	    String[] s = {"Active","Closed"};
	    JComboBox<String> statusField = new JComboBox(s);

	    JPanel myPanel = new JPanel();
	    JPanel subPanel = new JPanel();
	    
	    JLabel created = new JLabel("Created: "+sqlDate);
	    subPanel.add(created);
	    created.setAlignmentX(0.5f);
	    
	    subPanel.add(statusField);
	    myPanel.add(subPanel);
	    
	    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
	    
	    JLabel title;
	    myPanel.add(title = new JLabel("Title"));
	    title.setAlignmentX(0.5f);
	    myPanel.add(titleField);
	    
	    JLabel desc;
	    myPanel.add(desc = new JLabel("Description"));
	    desc.setAlignmentX(0.5f);
	    myPanel.add(descField);
	    
	    //The ID is used to check if we are displaying an existing ticket or want to create a new one
	    
	    if(t.getID() != -1){
	    	 myPanel.setBorder(BorderFactory.createTitledBorder("By: "+dbQueries.findUserByID(t.getUID())));
	    	 statusField.setSelectedItem(t.getStatus());
	    	 titleField.setText(t.getTitle());
	    	 descField.setText(t.getDesc());
	    	 created.setText("Created: "+t.getCreated());
	    }
	   
	    //If the ticket is an existing one we give them three options, edit, delete and do nothing
	    if(t.getID() != -1){
	    	Object[] options = {"Submit","Delete","Cancel"};
		    int result = JOptionPane.showOptionDialog(null, myPanel, "Ticket Details", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		    
		    if (result == JOptionPane.YES_OPTION) {
		    	if(t.getID() != -1){
		    		dbQueries.editTicket(t.getID(), titleField.getText(), descField.getText(), (String)statusField.getSelectedItem());
		    		if(dbQueries.getError().equals("Update Error")){
		    			cErrorLabel.setText("Error occurred when updating the ticket");
		    		}
		    	}else{
		    		dbQueries.insertNewTicket(currentUser.getID(),titleField.getText(), descField.getText(), (String)statusField.getSelectedItem(), sqlDate);
		    		if(dbQueries.getError().equals("Insert Error")){
		    			cErrorLabel.setText("Error occurred when inserting the ticket");
		    		}
		    	}
		    	
		    	printTickets();
		    }else if(result == JOptionPane.NO_OPTION){
		    	
		    	int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this ticket?", "Ticket Details", JOptionPane.OK_CANCEL_OPTION);
		    	if(confirm == JOptionPane.OK_OPTION){
		    		dbQueries.deleteTicket(t.getID());
		    		if(dbQueries.getError().equals("Delete Error")){
		    			cErrorLabel.setText("Error occurred when deleting the ticket");
		    		}
		    	}
		    }
		    //If we are creating a new ticket the user is only give the choices to submit the Ticket or cancel
	    }else{
		    int result = JOptionPane.showConfirmDialog(null, myPanel, "Ticket Details", JOptionPane.OK_CANCEL_OPTION);
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	if(titleField.getText().equals("") || descField.getText().equals("")){
		    		
		    		cErrorLabel.setText("Please fill all fields when creating new tickets.");
		    		
		    	}else{
		    		//Calling the method insertNewTicket in class DBQueries to add the ticket to the database
			    	dbQueries.insertNewTicket(currentUser.getID(),titleField.getText(), descField.getText(), (String)statusField.getSelectedItem(), sqlDate);
		    	
		    	}
		    	
		    	printTickets();
		    }
	    }
	    //Printing Tickets again regardless of what was done
	    printTickets();
	    
	}
	
	//Method for opening a new window for registration
	private void openRegistrationWindow(){
		//Creating local variables and giving them maximum lengths
		
		JTextField regFNField = new JTextField(10);
		((AbstractDocument)regFNField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		JTextField regLNField = new JTextField(10);
		((AbstractDocument)regLNField.getDocument()).setDocumentFilter(new LimitDocumentFilter(50));
		JTextField regEField = new JTextField();
		((AbstractDocument)regEField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		JPasswordField regPField = new JPasswordField();
		((AbstractDocument)regPField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		
		JLabel regFNLabel = new JLabel();
	    JLabel regLNLabel = new JLabel();
	    JLabel regPLabel = new JLabel();
	    JLabel regELabel = new JLabel();
	    JLabel regTLabel = new JLabel();
	    
	    //Centering the texts
	    regFNLabel.setAlignmentX(0.5f);
	    regLNLabel.setAlignmentX(0.5f);
	    regELabel.setAlignmentX(0.5f);
	    regPLabel.setAlignmentX(0.5f);
	    regTLabel.setAlignmentX(0.5f);
	    
	    regEField.setText(emailField.getText());
	    
	    //In order to get the value from the passwordField we have to turn it into a String from a char[]
	    String p = "";
	    char[] pc = passwordField.getPassword();
	    
	    for(int i=0;i<pc.length;i++){
	    	p = p + pc[i];
	    }
	    regPField.setText(p);
	    
	    String[] s = {"User","Admin"};
	    JComboBox<String> regTField = new JComboBox(s);
 
	    // A JPanel is needed so we can add UI components to a standard dialog
	    JPanel myPanel = new JPanel();
	    JPanel fPanel = new JPanel();
	    JPanel lPanel = new JPanel();
	    
	    fPanel.add(regFNLabel = new JLabel("First Name"));
	    fPanel.add(regFNField);

	    lPanel.add(regLNLabel = new JLabel("Last Name"));
	    lPanel.add(regLNField);

	    
	    myPanel.add(regELabel = new JLabel("Email"));
	    myPanel.add(regEField);
	    myPanel.add(regPLabel = new JLabel("Password"));
	    myPanel.add(regPField);
	    myPanel.add(regTLabel = new JLabel("Usertype"));
	    myPanel.add(regTField);
	    myPanel.add(fPanel);
	    myPanel.add(lPanel);
	    
	    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));

	    int result = JOptionPane.showConfirmDialog(null, myPanel, "New User", JOptionPane.OK_CANCEL_OPTION);
	    
	    //If the user presses the "Ok" -button we check that all inputFields have been filled before forwarding the values
	    if (result == JOptionPane.OK_OPTION) {
	    	
	    	if(regEField.getText().equals("") || regPField.getPassword().equals("") || regFNField.getText().equals("") || regLNField.getText().equals("")){
	    		
	    		lErrorLabel.setText("All registration fields have to be filled.");
	    		
	    	}else if(regEField.getText().indexOf('@') == -1){
	    		
	    		lErrorLabel.setText("Email is not valid!");
	    		
	    	}else{
	    		//The method returns a String which will be "Success" if the user is added to the database
	    		String res = dbQueries.registerNewUser(regEField.getText(),regPField.getPassword(), (String)regTField.getSelectedItem(), regFNField.getText(), regLNField.getText());
	    		
	    		if(res == "Error1"){
	    			
	    			lErrorLabel.setText("Error ocurred in adding user to database.");
	    			
	    		}else if(res == "Error2"){
	    			
	    			lErrorLabel.setText("Email is already taken.");
	    			
	    		}else{
	    			
	    			lErrorLabel.setText("");
	    			
	    		}
	    		
	    	}
	    }
	}
	
	//Method used for switching between the login screen and content screen
	private void switchCards(){
		CardLayout cl = (CardLayout) cardStack.getLayout();
		
		//Checking the value of currentCard
		if(currentCard == "login"){
			if(currentUser != null){
				lErrorLabel.setText("");
				
				if(!currentUser.getType().equals("Admin")){
					ticketFilter.setSelectedItem("My tickets");
					System.out.println("User: "+currentUser.getEmail()+"\nType: "+currentUser.getType());
					ticketFilter.setEnabled(false);
				}else{
					ticketFilter.setSelectedItem("All tickets");
					ticketFilter.setEnabled(true);
				}
				
				setFilterCriteria((String)statusFilter.getSelectedItem(),(String)ticketFilter.getSelectedItem());
				cl.show(cardStack, "contentScreen");
				currentCard = "content";
				printTickets();
				
			}else{lErrorLabel.setText("Error: No user found");}
		}else{
			cl.show(cardStack, "loginScreen");
			passwordField.setText("");
			emailField.setText("");
			currentCard = "login";
		}
		
	}
	
	//The main method which is used to start the UI
	public static void main(String[] args) {
		STool frame = new STool();
		frame.setVisible(true);     
		
	}
}

