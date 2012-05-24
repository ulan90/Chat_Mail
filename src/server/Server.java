package server;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.io.*;

class Server
{
	final String DATABASE_URL = "jdbc:mysql://localhost/mail_db";
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private Statement st;

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
	private Socket ClientSocket = null;
	private DataInputStream din;
	private DataOutputStream dout;
	private String username_pwd[];
	private String login;
	private String password;

	private ArrayList<String> contactArrayList  = new ArrayList<String>();
	private ArrayList<String> nameSurnameArrayList  = new ArrayList<String>();
	
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
		queryClientNameSurname();
		dout.writeUTF("&true&"+arrayListToOneString(nameSurnameArrayList));
		sendContactList();
		nameSurnameArrayList.clear();
		System.out.println("User Logged In :" + login);
		LoginNames.add(login);
		ClientSockets.add(ClientSocket);
		start();
	}
	public void run(){
		while(true){
			try{
				String msgFromClient=new String();
				msgFromClient=din.readUTF();
				StringTokenizer st=new StringTokenizer(msgFromClient);
				String SendFrom=st.nextToken();
				String Sendto=st.nextToken();
				int iCount=0;

				if(Sendto.equals("LOGOUT")){
					for(iCount=0;iCount<LoginNames.size();iCount++){
						if(LoginNames.elementAt(iCount).equals(SendFrom)){
							LoginNames.removeElementAt(iCount);
							ClientSockets.removeElementAt(iCount);
							System.out.println("User " + SendFrom +" Logged Out ...");
							break;
						}
					}
				}

				else{
					String msg="";
					while(st.hasMoreTokens()){
						msg = msg + " " + st.nextToken();
					}
					for(iCount=0;iCount<LoginNames.size();iCount++){
						if(LoginNames.elementAt(iCount).equals(Sendto)){
							Socket tSoc=(Socket)ClientSockets.elementAt(iCount);
							DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
							tdout.writeUTF(SendFrom + " says :" + msg);
							break;
						}
					}
					if(iCount==LoginNames.size()){
						dout.writeUTF(Sendto + " says :I am offline");
					}
					else{}
				}
				if(Sendto.equals("LOGOUT")){
					break;
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void queryClientNameSurname(){
		try {
			rs = st.executeQuery("SELECT Name, Surname FROM users WHERE user_name='"+login+"'");
			while (rs.next()){
				nameSurnameArrayList.add(rs.getString("Name") + " " + rs.getString("Surname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String arrayListToOneString(ArrayList<String> l){
		String arrListToOneString="";
		for(String s : l)
			arrListToOneString += s + "#";
		return arrListToOneString;
	}

	public void sendContactList(){
		try{
			String msg=new String();
			msg=din.readUTF();
			if(msg.equals("&queryContactList&")){
				dout.writeUTF(arrayListToOneString(contactArrayList));
				contactArrayList.clear();
			}
		}catch(Exception ex){}
	}

	public void connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(DATABASE_URL, "mail_login", "123456");
			pst=con.prepareStatement("SELECT * FROM users WHERE user_name=? and password=?");
			st = con.createStatement();

			//print all users in a console
			/*st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM users");
			ResultSetMetaData metaData = rs.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			System.out.println( "Authors Table of users in Database:\n" );
			for ( int i = 1; i <= numberOfColumns; i++ )
				System.out.printf( "%-17s\t", metaData.getColumnName( i ) );
				System.out.println();

			while(rs.next())
			{
				for ( int i = 1; i <= numberOfColumns; i++ )
					System.out.printf( "%-17s\t", rs.getObject( i ) );
				System.out.println();
			} // end while*/
		}
		catch( SQLException sqlException ){
			sqlException.printStackTrace();
		} // end catch
		catch(Exception e){
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
				databaseList();
				return true;
			}
			else
				return false;
		}catch(Exception e){
			System.out.println("Error while walidating "+e);
			return false;
		}
	}

	public void databaseList(){
		try{
			rs=st.executeQuery("SELECT user_name FROM users");
			while (rs.next()){
				contactArrayList.add(rs.getString("user_name"));
			}
		}
		catch (Exception e){
			System.out.println("Retrieving Data Fail");
		}
	}

	public void closeConnection(){
		try{
			rs.close();
			st.close();
			con.close();
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
}//end of AcceptClient()
}//end of Server()