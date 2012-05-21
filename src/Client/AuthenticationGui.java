package Client;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AuthenticationGui extends JFrame{
	private JLabel l_name,l_pass;
	private JTextField t_name;
	private JPasswordField t_pass;		//A special JTextField which hides input text

	private JButton button;
	private Container c;
	
	private String username;

	private handler handle;
	private Authentication auth;
	AuthenticationGui()
    {
		super("Login:");

		c=getContentPane();
		c.setLayout(new FlowLayout());

		auth = new Authentication();
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
	
	public String getUserName(){
		return username;
	}
	
	class handler implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			//checks if the button clicked
			if(ae.getSource()==button)
			{
				char[] temp_pwd=t_pass.getPassword();
				username = t_name.getText();
				String pwd=null;
				pwd=String.copyValueOf(temp_pwd);
				System.out.println("Username,Pwd:"+username+","+pwd);

				//The entered username and password are sent via "checkLogin()" in Authentication() class which return boolean
				if(auth.checkLogin(t_name.getText(), pwd))
				{
					ContactList contact = new ContactList();
					auth.closeConnection();
					setVisible(false);
				}
				else{
					//a pop-up box
					JOptionPane.showMessageDialog(null, "Login failed!","Failed!!",JOptionPane.ERROR_MESSAGE);
					t_pass.setText(null);
				}
			}//end if
		}//method
    }//handler
}//class