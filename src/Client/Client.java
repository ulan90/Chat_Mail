package Client;

import java.net.*;
import java.io.*;
import java.awt.*;
//import java.sql.*;
//import java.util.*;
class Client extends Frame implements Runnable
{
	//static String username,password;
	private Socket soc;
	private TextField tf;
	private TextArea ta;
	private Button btnSend,btnClose;
	private String sendTo;
	private String LoginName;
	private Thread t=null;
	private DataOutputStream dout;
	private DataInputStream din;
	Client(String LoginName,String chatwith) throws Exception{
		super(LoginName);
		this.LoginName=LoginName;
		sendTo=chatwith;
		tf=new TextField(50);
		ta=new TextArea(50,50);
		btnSend=new Button("Send");
		btnClose=new Button("Close");
		soc=new Socket("127.0.0.1",5217);

		din=new DataInputStream(soc.getInputStream());
		dout=new DataOutputStream(soc.getOutputStream());
		dout.writeUTF(LoginName);

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
    /*public static void users(){
    	System.out.println("USERS");
    	System.out.println("Enter Your username:");
        Scanner in = new Scanner(System.in);
        username = in.nextLine();
        System.out.println("Enter password");
        password = in.nextLine();
    }*/
	public static void main(String args[]) throws Exception{
		AuthenticationGui auth=new AuthenticationGui();
		/*String username = auth.getUserName();
		ContactList contact = new ContactList();
		String chatWith = contact.getChatwith();
		Client Client1=new Client(username,chatWith);
		Client1.setup();*/
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