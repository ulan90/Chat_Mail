package client;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.Socket;

import javax.swing.GroupLayout.Alignment;
public class Registration extends JFrame{
	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JPasswordField jPasswordField1;
	private JPasswordField jPasswordField2;
	private JTextField jTextField1;
	private JTextField jTextField2;
	private JTextField jTextField3;

	private DataOutputStream dout;
	private DataInputStream din;
	private ArrayList<String> registrList;

	Registration(Socket soc) throws IOException{
		super("Registration:");
		ArrayList<String> registrList = new ArrayList<String>();
		din=new DataInputStream(soc.getInputStream());
		dout=new DataOutputStream(soc.getOutputStream());
		
		initComponents();
		setVisible(true);
	}
	private void initComponents() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();
		jTextField3 = new JTextField();
		jPasswordField1 = new JPasswordField();
		jPasswordField2 = new JPasswordField();
		jButton1 = new JButton();
		jButton2 = new JButton();

		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		
		setBounds(new Rectangle(200, 50, 0, 0));

		jLabel1.setFont(new Font("Tahoma", 0, 14));
		jLabel1.setText("User Name:");

		jLabel2.setFont(new Font("Tahoma", 0, 14));
		jLabel2.setText("Name:");

		jLabel3.setFont(new Font("Tahoma", 0, 14));
		jLabel3.setText("Surname:");

		jLabel4.setFont(new Font("Tahoma", 0, 14));
		jLabel4.setText("Password:");

		jLabel5.setFont(new Font("Tahoma", 0, 14));
		jLabel5.setText("Confirm Password:");

		jTextField1.setFont(new Font("Tahoma", 0, 14));

		jTextField2.setFont(new Font("Tahoma", 0, 14));

		jTextField3.setFont(new Font("Tahoma", 0, 14));

		jPasswordField1.setFont(new Font("Tahoma", 0, 14));

		jPasswordField2.setFont(new Font("Tahoma", 0, 14));

		jButton1.setFont(new Font("Tahoma", 0, 14));
		jButton1.setText("Registr");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					jButton1ActionPerformed(evt);
				} catch (IOException e) {}
			}
		});

		jButton2.setFont(new Font("Tahoma", 0, 14));
		jButton2.setText("Exit");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addGap(59, 59, 59)
										.addGroup(layout.createParallelGroup(Alignment.TRAILING)
												.addComponent(jLabel1)
												.addComponent(jLabel4)
												.addComponent(jLabel3)
												.addComponent(jLabel2)))
												.addGroup(layout.createSequentialGroup()
														.addContainerGap()
														.addComponent(jLabel5)))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(Alignment.LEADING)
																.addGroup(layout.createSequentialGroup()
																		.addComponent(jButton1)
																		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(jButton2))
																		.addGroup(layout.createSequentialGroup()
																				.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
																						.addComponent(jTextField3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
																						.addComponent(jPasswordField2, Alignment.LEADING)
																						.addComponent(jTextField1, Alignment.LEADING)
																						.addComponent(jPasswordField1, Alignment.LEADING)
																						.addComponent(jTextField2))
																						.addGap(0, 0, Short.MAX_VALUE)))
																						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
								.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE)
												.addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel3))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(Alignment.BASELINE)
														.addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel4))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(Alignment.BASELINE)
																.addComponent(jPasswordField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																.addComponent(jLabel5))
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(layout.createParallelGroup(Alignment.BASELINE)
																		.addComponent(jButton1)
																		.addComponent(jButton2))
																		.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		pack();
	}

	private void jButton1ActionPerformed(ActionEvent evt) throws IOException {
		boolean a;
		if(!jTextField1.getText().isEmpty() && !jTextField2.getText().isEmpty() && !jTextField3.getText().isEmpty() && !jPasswordField1.getText().isEmpty() && !jPasswordField2.getText().isEmpty()){
			if(!checkUsername()){
				JOptionPane.showMessageDialog(null, "You Have Entered Wrong User Name!","Failed!!!",JOptionPane.ERROR_MESSAGE);
			}
			else if(!checkName())
				JOptionPane.showMessageDialog(null, "You Have Entered Wrong Name!","Failed!!!",JOptionPane.ERROR_MESSAGE);
			else if(!checkSurName())
				JOptionPane.showMessageDialog(null, "You Have Entered Wrong Surname!","Failed!!!",JOptionPane.ERROR_MESSAGE);
			else if(!checkPassword())
				JOptionPane.showMessageDialog(null, "1)Password must contain only numbers!\n2)Minimum 6 and maximum 14 numbers!","Failed!!!",JOptionPane.ERROR_MESSAGE);
			else if(!confirmPassword())
				JOptionPane.showMessageDialog(null, "Your password and confirm password fields don't match!","Failed!!!",JOptionPane.ERROR_MESSAGE);
			else if(registrate(jTextField1.getText().toString())){
				String msg =jTextField1.getText().toLowerCase()+"&"+jTextField2.getText()+"&"+jTextField3.getText()+"&"+jPasswordField1.getText();
				dout.writeUTF(msg);
				JOptionPane.showMessageDialog(null, "You are registered!\nLogin :" + jTextField1.getText() + "\nPassword :" + jPasswordField1.getText(),"Success!!!",JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
			}
			else
				JOptionPane.showMessageDialog(null, "User Name Which You Have Entered\nIs Already Exist In Database","Failed!!!",JOptionPane.ERROR_MESSAGE);
		}
		else JOptionPane.showMessageDialog(null, "You Left Some Fields Empty!","Failed!!!",JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean checkUsername(){
		Pattern p = Pattern.compile("[a-zA-Z]{3,16}[_]?[0-9]{0,4}@[a-zA-Z]{3,16}\\.[a-zA-Z]{2,4}");
        Matcher m = p.matcher(jTextField1.getText());
        return m.matches();
	}
	
	public boolean checkName(){
		Pattern p = Pattern.compile("[a-zA-Z]{3,16}");
        Matcher m = p.matcher(jTextField2.getText());
        return m.matches();
	}
	
	public boolean checkSurName(){
		Pattern p = Pattern.compile("[a-zA-Z]{4,16}[ ]?[a-zA-Z]{0,15}");
        Matcher m = p.matcher(jTextField3.getText());
        return m.matches();
	}
	public boolean checkPassword(){
		Pattern p = Pattern.compile("[0-9]{6,14}");
        Matcher m = p.matcher(jPasswordField1.getText());
        return m.matches();
	}
	public boolean confirmPassword(){
		if(jPasswordField1.getText().equals(jPasswordField2.getText()))
			return true;
		else return false;
		
	}
	
	public boolean registrate(String text) throws IOException{
		dout.writeUTF("REGISTR&"+text);
		if(din.readBoolean()){
			return true;
		}
		else return false;
	}
}
