package client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;

public class AuthenticationGui extends JFrame{
	private JLabel l_name,l_pass;
	private JTextField t_name;
	private JPasswordField t_pass;

	private JButton button;
	private JButton button2;
	private Container c;
	private handler handle;

	private Socket soc;
	private DataOutputStream dout;
	private DataInputStream din;

	private String username;
	private String pwd;
	private String fromServer="&false&";
	private String[] contactList;
	private Registration registr;
	
	AuthenticationGui(String socket, int port) throws Exception{
		super("Login:");
		soc=new Socket(socket,port);
		din=new DataInputStream(soc.getInputStream());
		dout=new DataOutputStream(soc.getOutputStream());

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try{
					dout.writeUTF("LOGOUT&LOGOUT");
					System.exit(1);
				}catch(Exception ex){}
			}
		});
		
		c=getContentPane();
		c.setLayout(new FlowLayout());

		handle = new handler();

		//swing components
		l_name=new JLabel("Username");
		l_pass=new JLabel("Password");
		t_name=new JTextField(15);
		t_pass=new JPasswordField(15);
		
		button=new JButton("Login");
		button2=new JButton("Registration");

		//adding action listener to the button
		button.addActionListener(handle);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
					try {
						registr = new Registration(soc);
					} catch (IOException e) {}
			}
		});
		//add to container
		c.add(l_name);
		c.add(t_name);
		c.add(l_pass);
		c.add(t_pass);
		c.add(button);
		c.add(button2);
		
		//visual
		setVisible(true);
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
						if(pwd.isEmpty() && username.equals("LOGOUT") || username.isEmpty()){
							JOptionPane.showMessageDialog(null, "You didn't enter login or password!","Failed!!!",JOptionPane.ERROR_MESSAGE);
							t_pass.setText(null);
						}
						else if(username.equals("LOGOUT")){
							JOptionPane.showMessageDialog(null, "Login failed!","Failed!!!",JOptionPane.ERROR_MESSAGE);
							t_pass.setText(null);
						}
						else if (!username.isEmpty() && !pwd.isEmpty()){
							dout.writeUTF(username.toLowerCase()+"&"+pwd);
							fromServer = din.readUTF();
							if(fromServer.equals("&false&")){
								JOptionPane.showMessageDialog(null, "Login failed!","Failed!!!",JOptionPane.ERROR_MESSAGE);
								t_pass.setText(null);
							}
							else{
								/*String nameSurname[] = fromServer.split("&true&");
								fromServer="";
								for(String s : nameSurname)
									fromServer += s;
								nameSurname = fromServer.split("#");*/
								ContactList cList = new ContactList(username);
								setVisible(false);
								registr.setVisible(false);
							}
						}
					}catch(Exception ex){}
				}
		}
    }

	public class ContactList extends JFrame implements Runnable{
		private DefaultListModel model;
		private JButton jButton1;
		private JButton jButton2;
		private JList dbList;
		private JPanel jPanel1;
		private JPanel jPanel2;
		private JPanel jPanel3;
		private JScrollPane jScrollPane1;
		private JScrollPane jScrollPane2;
		private JScrollPane jScrollPane3;
		private JTextArea jTextArea1;
		private JTextArea jTextArea2;
		private Thread t=null;

		private String chatWith;
		private String username;
		private String messageFromServer = "serverIsOnline";
		ContactList(String uname) throws IOException{
			super( uname );
			username = uname;
			initComponents();
			t=new Thread(this);
			t.start();
		}

		// Firstly I draw a GUI of contact list and messaging in the NetBeans and copied the code of GUI
		// then I made some changes added listener to "EXIT","Send" buttons and mouse click to a JList
		public void initComponents() throws IOException{
			setResizable(false);
			jPanel1 = new JPanel();
			jPanel2 = new JPanel();
			jPanel3 = new JPanel();

			jScrollPane1 = new JScrollPane();
			jScrollPane2 = new JScrollPane();
			jScrollPane3 = new JScrollPane();

			jTextArea1 = new JTextArea();
			DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			jTextArea1.setEditable( false );
			jTextArea2 = new JTextArea();

			jButton1 = new JButton();
			jButton2 = new JButton();
			
			setBounds(new Rectangle(200, 100, 0, 0));

			model = new DefaultListModel();
			dbList = new JList(model);

			addWindowListener(new WindowAdapter() {
			      public void windowClosing(WindowEvent e) {
			    	  if(!messageFromServer.equals("#EXIT#")){
				    	  try{
								dout.writeUTF(username + " LOGOUT");
							}catch(Exception ex){}
			    	  }
			    	  System.exit(1);
			      }
			});

			jPanel3.setBackground(new Color(0, 204, 0));

			jPanel1.setBackground(new Color(0, 204, 0));
			jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Message", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

			jTextArea1.setColumns(20);
			jTextArea1.setRows(5);
			jScrollPane1.setViewportView(jTextArea1);

			jTextArea2.setColumns(20);
			jTextArea2.setRows(5);
			jScrollPane2.setViewportView(jTextArea2);

			jButton1.setText("Send");
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!messageFromServer.equals("#EXIT#"))
						SENDButtonPressed(evt);
					else
						JOptionPane.showMessageDialog(null, "SERVER IS OFF!!!\nSORRY FOR DISCOMFORT!!!","From Server:",JOptionPane.ERROR_MESSAGE);
				}
			});

			GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
					.addComponent(jScrollPane2, GroupLayout.Alignment.TRAILING)
					.addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
							.addGap(0, 0, Short.MAX_VALUE)
							.addComponent(jButton1))
			);
			jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jButton1))
			);

			jPanel2.setBackground(new java.awt.Color(0, 204, 0));
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Contacts:", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

			jButton2.setText("EXIT");
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!messageFromServer.equals("#EXIT#")){
						try{
							dout.writeUTF(username + " LOGOUT");
						}catch(Exception ex){}
					}
					System.exit(1);
				}
			});
			//add all user names to ArrayList
			databaseList();

			jScrollPane3.setViewportView(dbList);

			dbList.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent me){
					chatWith = dbList.getSelectedValue().toString();
				}
			  }
			);
			GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2Layout.setHorizontalGroup(
					jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(jPanel2Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(jScrollPane3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
									.addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
											.addGap(0, 0, Short.MAX_VALUE)
											.addComponent(jButton2)))
											.addContainerGap())
			);
			jPanel2Layout.setVerticalGroup(
					jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(jScrollPane3)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jButton2))
			);

			GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
			jPanel3.setLayout(jPanel3Layout);
			jPanel3Layout.setHorizontalGroup(
					jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(jPanel3Layout.createSequentialGroup()
							.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			jPanel3Layout.setVerticalGroup(
					jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);

			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			);
			pack();
			setVisible(true);
		}
		public void SENDButtonPressed(ActionEvent evt) {
			if(dbList.isSelectionEmpty()){
				JOptionPane.showMessageDialog(null, "Select the user to send message!","FATAL ERROR!",JOptionPane.ERROR_MESSAGE);
			}
			else{
				try {
					dout.writeUTF(username + " " + chatWith + " " + " " + jTextArea2.getText().toString());
					jTextArea1.append("\n" + username + " Says :" + jTextArea2.getText().toString());
					jTextArea2.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }

		public void databaseList() throws IOException{
			try{
				dout.writeUTF("&queryContactList&");
				contactList = din.readUTF().split("#");
				model.clear();
				for(int i = 0; i < contactList.length; i++)
					if(!contactList[i].equals(username))
						model.addElement(contactList[i]);
			}catch(Exception ex){}
		}

		public void run(){
			while(true){
				try{
					messageFromServer = din.readUTF();
					if(messageFromServer.equals("#REFRESHLIST#")){
						contactList = din.readUTF().split("#");
						model.clear();
						for(int i = 0; i < contactList.length; i++)
							if(!contactList[i].equals(username))
								model.addElement(contactList[i]);
					}
					else if(messageFromServer.equals("#EXIT#")){
						JOptionPane.showMessageDialog(null, "Server is Shut Down!!!\nSorry For Discomfort!!!","From Server",JOptionPane.ERROR_MESSAGE);
						break;
					}
					else {
						jTextArea1.append("\n" + messageFromServer);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}//end of sub class ----> ContactList
}// end of main class class ----> AuthenticationGui
