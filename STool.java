import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;

public class STool extends JFrame{
	private JTextField emailField, passwordField;
	public JLabel errorLabel;
	private JScrollPane scrollPanel;
	private JPanel loginContainer, contentContainer, ticketPanel;
	private JButton loginButton;
	private JComboBox ticketFilter, statusFilter;
	private JButton[] buttons;
	private JPanel cardStack;
	public User currentUser;
	private DBQueries dbQueries;
	private ArrayList<Ticket> tickets;
	private Ticket cT;
	//tValues[0] = type of query, tValues[1] = ticketStatus for the query, tValues[2] = userID for the query
	private String[] tValues = {"1","",""};
	private JButton btnLogout;
	private String currentCard;
	private JPanel headerPanel;
	
	public STool(){
		super("STool - Support Tool");
		setResizable(false);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setBounds(0,0,800,600); 
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		dbQueries = new DBQueries();
		
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
		((AbstractDocument)emailField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		emailField.setBounds(300, 233, 200, 20);
		emailField.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(emailField);
			
		passwordField = new JPasswordField();
		((AbstractDocument)emailField.getDocument()).setDocumentFilter(new LimitDocumentFilter(40));
		passwordField.setBounds(300, 278, 200, 20);
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		loginContainer.add(passwordField);

		loginButton = new JButton("Login");
		loginButton.setBounds(350, 309, 100, 23);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(dbQueries.errorReport == "Connection error"){
					dbQueries = new DBQueries();
					if(dbQueries.errorReport != "Connection error"){
						currentUser = DBQueries.findUser(emailField.getText(), passwordField.getText());
						switchCards();
					}
				}else{
					currentUser = DBQueries.findUser(emailField.getText(), passwordField.getText());
					switchCards();
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
		
		errorLabel = new JLabel("");
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(250, 377, 300, 14);
		loginContainer.add(errorLabel);
		
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
		//headerPanel.setBounds(0, 0, 780, 40);
		contentContainer.add(headerPanel);
		
		JButton updateList = new JButton("Refresh");
		headerPanel.add(updateList);
		String[] sFOptions = {"Active tickets","All tickets","Closed tickets"};
		statusFilter = new JComboBox(sFOptions);
		headerPanel.add(statusFilter);
		
		String[] tFOptions = {"All tickets","My tickets"};
		ticketFilter = new JComboBox(tFOptions);
		headerPanel.add(ticketFilter);
		
		JButton newTicket = new JButton("New ticket");
		headerPanel.add(newTicket);
		
		btnLogout = new JButton("Logout");
		headerPanel.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentUser = null;
				switchCards();
			}
		});
		newTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openTicketDetails(new Ticket(-1,-1,"","","",""));
			}
		});
		updateList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Code for creating new tickets here
				printTickets();
			}
		});
		
		ticketPanel = new JPanel();
		//ticketPanel.setBounds(0, 0, 708, 518);
		contentContainer.add(ticketPanel);
		ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
		
		scrollPanel = new JScrollPane(ticketPanel);
		//scrollPanel.setBounds(0, 40, 740, 420);
		scrollPanel.setPreferredSize(new Dimension(740,520));
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
		contentContainer.add(scrollPanel);
		
		if(dbQueries.errorReport == "Connection error"){
			errorLabel.setText("Connection error");
		}

	}
	
	private void printTickets()
	{		
		ticketPanel.removeAll();
		
		setFilterCriteria((String)statusFilter.getSelectedItem(),(String)ticketFilter.getSelectedItem());
		tickets = dbQueries.getTickets(tValues);
		if(dbQueries.errorReport == "Connection error"){
			CardLayout cl = (CardLayout) cardStack.getLayout();
			errorLabel.setText("Connection error");
			cl.show(cardStack, "loginScreen");
		}
		
		buttons = new JButton[tickets.size()];
		for (int row=0; row<tickets.size(); row++){
			cT = tickets.get(row);
			buttons[row] = new JButton(cT.getTitle()+" | "+cT.getCreated());
			if(cT.getStatus().equals("Closed")){
				buttons[row].setBackground(Color.GRAY);
			}
			buttons[row].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object src = e.getSource();
					
					for(int i=0;i<buttons.length;i++){
						if(buttons[i] == src){
							openTicketDetails(tickets.get(i));
						}
					}
				}
			});
			
			ticketPanel.add(buttons[row]);
		}
		ticketPanel.validate();
		ticketPanel.repaint();
		scrollPanel.validate();
	}
	//sF = results from statusFilter, tF = results from ticketFilter
	private void setFilterCriteria(String sF, String tF){
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
	
	private void openTicketDetails(Ticket t){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		JTextField titleField = new JTextField();
		((AbstractDocument)titleField.getDocument()).setDocumentFilter(new LimitDocumentFilter(60));
	    JTextArea descField = new JTextArea(10, 1);
	    	descField.setLineWrap(true);
	    descField.setWrapStyleWord(true);
	    descField.setBorder(BorderFactory.createLineBorder(Color.black));
	    
	    String[] s = {"Active","Closed"};
	    JComboBox statusField = new JComboBox(s);
 
	    // A JPanel is needed so we can add UI components to a standard dialog
	    JPanel myPanel = new JPanel();
	    JPanel subPanel = new JPanel();
	    
	    JLabel created = new JLabel("Created: "+sqlDate);
	    subPanel.add(created);
	    created.setAlignmentX(0.5f);
	    
	    subPanel.add(statusField);
	    myPanel.add(subPanel);
	    
	    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
	    // Adding the PlateNR field to the JPanel
	    JLabel title;
	    myPanel.add(title = new JLabel("Title"));
	    title.setAlignmentX(0.5f);
	    myPanel.add(titleField);
	    
	    // Adding the Color field to the JPanel
	    JLabel desc;
	    myPanel.add(desc = new JLabel("Description"));
	    desc.setAlignmentX(0.5f);
	    myPanel.add(descField);
	    
	    if(t.getID() != -1){
	    	 myPanel.setBorder(BorderFactory.createTitledBorder("By: "+dbQueries.findUserByID(t.getUID())));
	    	 statusField.setSelectedItem(t.getStatus());
	    	 titleField.setText(t.getTitle());
	    	 descField.setText(t.getDesc());
	    	 created.setText("Created: "+t.getCreated());
	    }
	   
	    

	    if(t.getID() != -1){
	    	Object[] options = {"Submit","Delete","Cancel"};
		    int result = JOptionPane.showOptionDialog(null, myPanel, "Ticket Details", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		    
		    if (result == JOptionPane.YES_OPTION) {
		    	if(t.getID() != -1){
		    		dbQueries.editTicket(t.getID(), titleField.getText(), descField.getText(), (String)statusField.getSelectedItem());
		    	}else{
		    		dbQueries.insertNewTicket(currentUser.getID(),titleField.getText(), descField.getText(), (String)statusField.getSelectedItem(), sqlDate);
		    	}
		    	
		    	printTickets();
		    }else if(result == JOptionPane.NO_OPTION){
		    	int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete this ticket?", "Ticket Details", JOptionPane.OK_CANCEL_OPTION);
		    	if(confirm == JOptionPane.OK_OPTION){
		    		dbQueries.deleteTicket(t.getID());
		    	}
		    }
	    }else{
	    	Object[] options = {"Submit","Delete","Cancel"};
		    int result = JOptionPane.showConfirmDialog(null, myPanel, "Ticket Details", JOptionPane.OK_CANCEL_OPTION);
		    
		    if (result == JOptionPane.OK_OPTION) {
		    	if(titleField.getText().equals("") || descField.getText().equals("")){
		    		System.out.println("Fill fields pls.");
		    	}else{
		    		if(t.getID() != -1){
			    		dbQueries.editTicket(t.getID(), titleField.getText(), descField.getText(), (String)statusField.getSelectedItem());
			    	}else{
			    		dbQueries.insertNewTicket(currentUser.getID(),titleField.getText(), descField.getText(), (String)statusField.getSelectedItem(), sqlDate);
			    	}
		    	}
		    	
		    	printTickets();
		    }
	    }
	    printTickets();
	    
	}
	
private void openRegistrationWindow(){
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
	    
	    regFNLabel.setAlignmentX(0.5f);
	    regLNLabel.setAlignmentX(0.5f);
	    regELabel.setAlignmentX(0.5f);
	    regPLabel.setAlignmentX(0.5f);
	    regTLabel.setAlignmentX(0.5f);
	    
	    regEField.setText(emailField.getText());
	    regPField.setText(passwordField.getText());
	    
	    String[] s = {"User","Admin"};
	    JComboBox regTField = new JComboBox(s);
 
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

	    // Displaying the input dialog: a standard Confirmation dialog showing the text fields from the JPanel myPanel
	    int result = JOptionPane.showConfirmDialog(null, myPanel, "New User", JOptionPane.OK_CANCEL_OPTION);
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	if(regEField.getText().equals("") || regPField.getPassword().equals("") || regFNField.getText().equals("") || regLNField.getText().equals("")){
	    		errorLabel.setText("All registration fields have to be filled.");
	    	}else if(regEField.getText().indexOf('@') == -1){
	    		errorLabel.setText("Email is not valid!");
	    	}else{
	    		String res = dbQueries.registerNewUser(regEField.getText(),regPField.getPassword(), (String)regTField.getSelectedItem(), regFNField.getText(), regLNField.getText());
	    		if(res == "Error1"){
	    			errorLabel.setText("Error ocurred in adding user to database.");
	    		}else if(res == "Error2"){
	    			errorLabel.setText("Email is already taken.");
	    		}else{
	    			errorLabel.setText("");
	    		}
	    		
	    	}
	    }
	}
	
	private void switchCards(){
		CardLayout cl = (CardLayout) cardStack.getLayout();
		
		if(currentCard == "login"){
			if(currentUser != null){
				errorLabel.setText("");
				
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
				
			}else{errorLabel.setText("Error: No user found");}
		}else{
			cl.show(cardStack, "loginScreen");
			currentCard = "login";
			passwordField.setText("");
			emailField.setText("");
		}
		
	}

	public static void main(String[] args) {
		STool frame = new STool();
		frame.setVisible(true);     
		
	}
}

