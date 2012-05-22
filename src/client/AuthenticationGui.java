package client;


//I'VE COPYIED ALL CLASSES INTO AuthenticationGui.java FILE
//not finished yet,at next commitment I think, I'll finish


import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AuthenticationGui extends JFrame{
	private JLabel l_name,l_pass;
	private JTextField t_name;
	private JPasswordField t_pass;		//A special JTextField which hides input text

	private JButton button;
	private Container c;
	
	private Socket soc;
	private DataOutputStream dout;
	private DataInputStream din;

	
	private String username;
	private String pwd;
	private String fromServer="&false&";
	private String[] contactList;

	private handler handle;
//	private Server server;

	AuthenticationGui() throws Exception{
		super("Login:");
		
		soc=new Socket("127.0.0.1",5217);
		din=new DataInputStream(soc.getInputStream());
		dout=new DataOutputStream(soc.getOutputStream());
		
		c=getContentPane();
		c.setLayout(new FlowLayout());

//		server = new Server();
		handle = new handler();

		//swing components
		l_name=new JLabel("Username");
		l_pass=new JLabel("Password");
		t_name=new JTextField(15);
		t_pass=new JPasswordField(15);
		button=new JButton("Login");

		//adding action listener to the button
		button.addActionListener(handle);

		//add to container
		c.add(l_name);
		c.add(t_name); 
		c.add(l_pass);
		c.add(t_pass);
		c.add(button);

		//visual
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500,250,300,130);
		setResizable(false);
	}

	class handler implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			//checks if the button clicked
				if(ae.getSource()==button){
					try{
						char[] temp_pwd=t_pass.getPassword();
						username = t_name.getText();
						pwd=null;
						pwd=String.copyValueOf(temp_pwd);
						if(pwd.isEmpty() || username.isEmpty()){
							JOptionPane.showMessageDialog(null, "You didn't enter login or password!","Failed!!!",JOptionPane.ERROR_MESSAGE);
							t_pass.setText(null);
						}
						if (!username.isEmpty() && !pwd.isEmpty()){
							dout.writeUTF(username+"&"+pwd);
							fromServer = din.readUTF();
							if(fromServer.equals("&false&")){
								JOptionPane.showMessageDialog(null, "Login failed!","Failed!!!",JOptionPane.ERROR_MESSAGE);
								t_pass.setText(null);
							}
							else{
								ContactList cList = new ContactList(username);
								setVisible(false);
							}
						}
					}catch(Exception ex){}
				}
		}
    }
	public static void main(String args[]) throws Exception{
		AuthenticationGui authGui = new AuthenticationGui();
	}
	public class ContactList extends JFrame{
		private DefaultListModel model;
		private JList dbList;
		private JPanel p;
		private String chatWith;
		private String username;
		ContactList(String uname) throws IOException{
			super( "@mail.ru" );
			setBounds(1000,10,300,650);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			username = uname;

			//Constructing JList and DefaultListModel
			model = new DefaultListModel();
			dbList = new JList(model);
			
			//Setting up JList property
			dbList.setVisibleRowCount(29);
			dbList.setFixedCellHeight(20);
			dbList.setFixedCellWidth(250);
			dbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
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
			
			dbList.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent me){
					chatWith = dbList.getSelectedValue().toString();
					if(chatWith != null){
						try {
							System.out.println(username+","+chatWith);
							Client client = new Client(username,chatWith);
							client.setup();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			  }
			);
		}
		
		public void databaseList() throws IOException{
			try{
				dout.writeUTF("&queryContactList&");
				contactList = din.readUTF().split("#");
				model.clear();
				for(int i = 0; i < contactList.length; i++)
					model.addElement(contactList[i]);
			}catch(Exception ex){}
		}
	}
	
	class Client extends JFrame implements Runnable
	{
		private TextField tf;
		private TextArea ta;
		private Button btnSend,btnClose;
		private String sendTo;
		private String LoginName;
		private Thread t=null;
		
		Client(String LoginName,String chatwith) throws Exception{
			super(LoginName);
			this.LoginName=LoginName;
			sendTo=chatwith;
			tf=new TextField(50);
			ta=new TextArea(50,50);
			btnSend=new Button("Send");
			btnClose=new Button("Close");
			
			t=new Thread(this);
			t.start();
		}
		
		public void setup(){
			setSize(600,400);
			setLayout(new GridLayout(2,1));

			add(ta);
			Panel p=new Panel();

			p.add(tf);
			p.add(btnSend);
			p.add(btnClose);
			add(p);
			show();
		}
		
		public boolean action(Event e,Object o){
			if(e.arg.equals("Send")){
				try{
					dout.writeUTF(sendTo + " "  + "DATA" + " " + tf.getText().toString());
					ta.append("\n" + LoginName + " Says:" + tf.getText().toString());
					tf.setText("");
				}catch(Exception ex){}
			}
			else if(e.arg.equals("Close")){
				try{
					dout.writeUTF(LoginName + " LOGOUT");
					System.exit(1);
				}catch(Exception ex){}
			}

			return super.action(e,o);
		}
		
		public void run(){
			while(true){
				try{
					ta.append( "\n" + sendTo + " Says :" + din.readUTF());
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}//class