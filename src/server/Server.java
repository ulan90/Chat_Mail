package server;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.io.*;

import javax.swing.JOptionPane;

class Server
{
	final String DATABASE_URL = "jdbc:mysql://localhost/mail_db";
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private Statement st;

	private ArrayList<String> list  = new ArrayList<String>();

	static Vector ClientSockets;
	static Vector LoginNames;

	Server() throws Exception{
		ServerSocket soc=new ServerSocket(5217);
		ClientSockets=new Vector();
		LoginNames=new Vector();

		while(true){
			Socket CSoc=soc.accept();
			AcceptClient obClient=new AcceptClient(CSoc);
		}
	}

	public static void main(String args[]) throws Exception{
		Server ob=new Server();
	}

class AcceptClient extends Thread{
	private Socket ClientSocket;
	private DataInputStream din;
	private DataOutputStream dout;
	private String username_pwd[];
	private String login;
	private String password;
	AcceptClient (Socket CSoc) throws Exception{
		ClientSocket=CSoc;

		connect();

		din=new DataInputStream(ClientSocket.getInputStream());
		dout=new DataOutputStream(ClientSocket.getOutputStream());

		username_pwd = din.readUTF().split("&");
		while(!checkLogin(username_pwd)){
			dout.writeUTF("&false&");
			username_pwd=din.readUTF().split("&");
		}
		dout.writeUTF("&true&");
		System.out.println("User Logged In :" + login);
		LoginNames.add(login);
		ClientSockets.add(ClientSocket);
		start();
	}

	public void run(){
		while(true){
			try{
				try{
					String msg=new String();
					msg=din.readUTF();
					if(msg.equals("&queryContactList&")){
						databaseList();
						String contactList="";
						for(String s : list)
							contactList += s + "#";
						dout.writeUTF(contactList);
						list.clear();
					}
				}catch(Exception ex){}

				String msgFromClient=new String();
				msgFromClient=din.readUTF();
				StringTokenizer st=new StringTokenizer(msgFromClient);
				String Sendto=st.nextToken();                
				String MsgType=st.nextToken();
				int iCount=0;

				if(MsgType.equals("LOGOUT")){
					for(iCount=0;iCount<LoginNames.size();iCount++){
						if(LoginNames.elementAt(iCount).equals(Sendto)){
							LoginNames.removeElementAt(iCount);
							ClientSockets.removeElementAt(iCount);
							System.out.println("User " + Sendto +" Logged Out ...");
							break;
						}
					}
				}
				else{
					String msg="";
					while(st.hasMoreTokens()){
						msg=msg+" " +st.nextToken();
					}
					for(iCount=0;iCount<LoginNames.size();iCount++){
						if(LoginNames.elementAt(iCount).equals(Sendto)){    
							Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
							DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
							tdout.writeUTF(msg);
							break;
						}
					}
					if(iCount==LoginNames.size()){
						dout.writeUTF("I am offline");
					}
					else{}
				}
				if(MsgType.equals("LOGOUT")){
					break;
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
/////////////////////////////////////////////////////////////////////////
	//Authentication.java
		public void connect(){
			try{
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(DATABASE_URL, "mail_login", "123456");
				pst=con.prepareStatement("SELECT * FROM users WHERE user_name=? and password=?");
				
				st = con.createStatement();
				rs = st.executeQuery("SELECT * FROM users");
				ResultSetMetaData metaData = rs.getMetaData();
				int numberOfColumns = metaData.getColumnCount();
				System.out.println( "Authors Table of Books Database:\n" );
				for ( int i = 1; i <= numberOfColumns; i++ )
					System.out.printf( "%-17s\t", metaData.getColumnName( i ) );
				System.out.println();
				
				while(rs.next())
				{
					for ( int i = 1; i <= numberOfColumns; i++ )
						System.out.printf( "%-17s\t", rs.getObject( i ) );
					System.out.println();
				} // end while
			}
			catch( SQLException sqlException )
			{
				sqlException.printStackTrace();
			} // end catch
			catch(Exception e){
				System.out.println("catch");
				System.out.println(e);
			}
		}

		public boolean checkLogin(String[] arr){
			login = arr[0];
			password = arr[1];
			try{
				pst.setString(1, login);
				pst.setString(2, password);

				rs=pst.executeQuery();
				if(rs.next()){
					return true;
				}
				else
					return false;
			}catch(Exception e){
				System.out.println("Error while walidating "+e);
				return false;
			}
		}

		public void closeConnection(){
			try
			{
				rs.close();
				st.close();
				con.close();
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
		}

	//end Authentication.java	

	//ContactList.java
		public void databaseList(){
			try{
				rs=st.executeQuery("SELECT Name, Surname FROM users");
				while (rs.next()){
					list.add(rs.getString("Name") + " " + rs.getString("Surname"));
				}
			}
			catch (Exception e){
				System.out.println("Retrieving Data Fail");
			}
			/*try{
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection(DATABASE_URL, "mail_login", "123456");
				st = con.createStatement();
			}
			catch (Exception e){
				JOptionPane.showMessageDialog(null,"Failed to Connect to Database","Error Connection", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}*/
		}
		//end ContactList
}//end of AcceptClient()
}//end of Server()