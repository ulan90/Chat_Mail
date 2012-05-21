package Client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class ContactList extends JFrame{
	final String DATABASE_URL = "jdbc:mysql://localhost/mail_db";
	private DefaultListModel model;
	private JList dbList;
	private JPanel p;
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private String chatWith;
	private String username;

	private String[] contactNames;
	AuthenticationGui authGui = new  AuthenticationGui();
	Client client;
	ContactList(){
		super( "@mail.ru" );
		setBounds(1000,10,300,650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Constructing JList and DefaultListModel
		model = new DefaultListModel();
		dbList = new JList(model);

		//Setting up JList property
		dbList.setVisibleRowCount(29);
		dbList.setFixedCellHeight(20);
		dbList.setFixedCellWidth(250);
		dbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		username = authGui.getUserName();
		dbList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent me){
				chatWith = dbList.getSelectedValue().toString();
				if(chatWith != null){
					try {
						chat();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(temp);
					//JOptionPane.showMessageDialog(Jlist.this,temp.recordNumber + " - "+temp.description);
				}
			}
		  }
		);
		
		p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Contacts:"));
		p.add(new JScrollPane(dbList));
		
		//Setting up the container ready for the components to be added.
		Container pane = getContentPane();
		setContentPane(pane);

		//Setting up the container layout
		GridLayout grid = new GridLayout(1,2);
		pane.setLayout(grid);

		databaseList();

		pane.add(p);
		setVisible(true);
		setResizable(false);
	}
	
	public void chat() throws Exception{
		Client client = new Client(username,chatWith);
		client.setup();
	}
	
	public String getChatwith(){
		return chatWith;
	}
	
	public void databaseList(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(DATABASE_URL, "mail_login", "123456");
			st = con.createStatement();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null,"Failed to Connect to Database","Error Connection", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		try{
			model.clear();
			rs=st.executeQuery("SELECT Name, Surname FROM users");
			while (rs.next()){
				model.addElement(rs.getString("Name"));// + " " + rs.getString("Surname"));
			}
		}
		catch (Exception e){
			System.out.println(e);
			System.out.println("Retrieving Data Fail");
		}
	}
}