package server;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.io.*;

class Server{
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private Statement st;
	private static serverGUI sGUI;
	private static dbAccessGUI dbAccGUI;
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
		sGUI = new serverGUI();
		sGUI.setVisible(true);
		dbAccGUI = new dbAccessGUI();
		Server ob = new Server();
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
	private ArrayList<String> contactArrayListToRegistrate  = new ArrayList<String>();
	
	AcceptClient (Socket CSoc) throws Exception{
		con = dbAccGUI.getCon();
		ClientSocket=CSoc;

		din=new DataInputStream(ClientSocket.getInputStream());
		dout=new DataOutputStream(ClientSocket.getOutputStream());
		
		pst=con.prepareStatement("SELECT * FROM users WHERE user_name=? and password=?");
		st = con.createStatement();
		
		username_pwd = din.readUTF().split("&");
		if(username_pwd[0].equals("REGISTR"))
			Registrate(1);

		if(!username_pwd[0].equals("LOGOUT")){
			while(!checkLogin(username_pwd)){
				dout.writeUTF("&false&");
				username_pwd=din.readUTF().split("&");
			}
			queryClientNameSurname();
			dout.writeUTF("&true&");  //+arrayListToOneString(nameSurnameArrayList));
			sendContactList();
			nameSurnameArrayList.clear();
			sGUI.jTextArea1.append("\nUser Logged In :" + login);
			LoginNames.add(login);
			ClientSockets.add(ClientSocket);
			for(int i=0;i<ClientSockets.size();i++){
				if(!LoginNames.elementAt(i).equals(login)){
					Socket ClSoc=(Socket)ClientSockets.elementAt(i);
					DataOutputStream dataOutt=new DataOutputStream(ClSoc.getOutputStream());
					dataOutt.writeUTF("\t"+login + " --> ENTERED THE ROOM");
				}
			}
			sGUI.setSockets(ClientSockets);
			start();
		}
		else{
			ClientSocket=null;
			din = null;
			dout = null;
		}
	
	}

	public void Registrate(int regCount) throws IOException, SQLException{
		int j=0;
		if(regCount==0)
			username_pwd = din.readUTF().split("&");
		databaseList();
		for(int i=0; i<contactArrayList.size(); i++)
			if(username_pwd[1].equals(contactArrayList.get(i))){
				dout.writeBoolean(false);
				contactArrayList.clear();
				j++;
				break;
			}
		if(j>0)
			Registrate(0);
		else{
			dout.writeBoolean(true);
			String messgFromClient[] = 	din.readUTF().split("&");
			st.executeUpdate("INSERT INTO users (Name, Surname, user_name, password)" + "VALUES ('"+messgFromClient[1]+"','"+messgFromClient[2]+"','"+messgFromClient[0]+"','"+messgFromClient[3]+"')");
			databaseList();
			for(int i=0;i<ClientSockets.size();i++){
				try {
					Socket ClSoc=(Socket)ClientSockets.elementAt(i);
					DataOutputStream dataOut=new DataOutputStream(ClSoc.getOutputStream());
					dataOut.writeUTF("#REFRESHLIST#");
					dataOut.writeUTF(arrayListToOneString(contactArrayList));
				} catch (IOException e) {}
			}
			contactArrayList.clear();
			username_pwd = din.readUTF().split("&");
			if(username_pwd[0].equals("REGISTR")){
				contactArrayList.clear();
				Registrate(1);
			}
		}
		contactArrayList.clear();
	}

	public void run(){
		while(true){
			try{
				String a = new String();
				String msgFromClient=new String();
				msgFromClient=din.readUTF();
				StringTokenizer st=new StringTokenizer(msgFromClient);
				String SendFrom=st.nextToken();
				String Sendto=st.nextToken();
				int iCount=0;

				if(Sendto.equals("LOGOUT")){
					for(iCount=0;iCount<LoginNames.size();iCount++){
						if(LoginNames.elementAt(iCount).equals(SendFrom)){
							a=LoginNames.elementAt(iCount).toString();
							LoginNames.removeElementAt(iCount);
							ClientSockets.removeElementAt(iCount);
							sGUI.setSockets(ClientSockets);
							sGUI.jTextArea1.append("\nUser " + SendFrom + " Logged Out ...");
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
					for(int i=0;i<ClientSockets.size();i++){
						try {
							Socket ClSoc=(Socket)ClientSockets.elementAt(i);
							DataOutputStream dataOut=new DataOutputStream(ClSoc.getOutputStream());
							dataOut.writeUTF("\t" + a + " --> LEFT THE ROOM!");
						} catch (IOException e) {}
					}
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
	public String arrayListToOneString(ArrayList<String> list){
		String arrListToOneString="";
		for(String s : list)
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