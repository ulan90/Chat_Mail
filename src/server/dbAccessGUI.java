package server;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.GroupLayout.Alignment;

public class dbAccessGUI extends JFrame{
	private String DATABASE_URL = "jdbc:mysql://localhost/mail_db"; // by default
	private String dbUserName = "mail_login"; // by default
	private String dbUserPassword = "123456"; // by default
	private JButton jButton1;
	private JCheckBox jCheckBox1;
	private JCheckBox jCheckBox2;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JPasswordField jPasswordField1;
	private JTextField jTextField1;
	private JTextField jTextField2;
	private Connection con;
	int counter = 0;

	dbAccessGUI(){
		super("Change the Database:");
		initComponents();
		connectDB(); // by default it's connected to "jdbc:mysql://localhost/mail_db" database.
		setVisible(true);
	}
	private void initComponents() {
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();
		jPasswordField1 = new JPasswordField();
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jButton1 = new JButton();
		jCheckBox1 = new JCheckBox();
		jCheckBox2 = new JCheckBox();
		
		setBounds(new Rectangle(520, 100, 0, 0));

		jTextField1.setEditable(false);
		jTextField1.setFont(new Font("Tahoma", 0, 14));
		jTextField1.setHorizontalAlignment(JTextField.CENTER);
		jTextField1.setText("jdbc:mysql://localhost/mail_db");

		jTextField2.setEditable(false);
		jTextField2.setFont(new Font("Tahoma", 0, 14));
		jTextField2.setHorizontalAlignment(JTextField.CENTER);
		jTextField2.setText("mail_login");

		jPasswordField1.setFont(new Font("Tahoma", 0, 14));
		jPasswordField1.setHorizontalAlignment(JTextField.CENTER);
		jPasswordField1.setText("123456");

		jLabel1.setFont(new Font("Tahoma", 0, 14));
		jLabel1.setText("Database URL :");

		jLabel2.setFont(new Font("Tahoma", 0, 14));
		jLabel2.setText("User Name :");

		jLabel3.setFont(new Font("Tahoma", 0, 14));
		jLabel3.setText("Password:");

		jButton1.setFont(new Font("Tahoma", 0, 14));
		jButton1.setText("CHANGE");
		
		addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		    	  setVisible(false);
		      }
		});

		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jCheckBox1.setFont(new Font("Tahoma", 0, 14));
		jCheckBox1.setText("Edit");
		jCheckBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jCheckBox1ActionPerformed(evt);
			}
		});

		jCheckBox2.setFont(new Font("Tahoma", 0, 14));
		jCheckBox2.setSelected(true);
		jCheckBox2.setText("None");
		jCheckBox2.setEnabled(false);
		jCheckBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jCheckBox2ActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addComponent(jLabel1)
								.addComponent(jLabel2)
								.addComponent(jLabel3))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
												.addGap(0, 0, Short.MAX_VALUE))
												.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
														.addGap(0, 0, Short.MAX_VALUE)
														.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
																.addGroup(layout.createSequentialGroup()
																		.addComponent(jCheckBox1)
																		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(jCheckBox2)
																		.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(jButton1))
																		.addGroup(layout.createParallelGroup(Alignment.LEADING)
																				.addComponent(jTextField1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
																				.addComponent(jPasswordField1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)))))
																				.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE)
												.addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel3))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(jCheckBox1)
														.addGroup(layout.createParallelGroup(Alignment.BASELINE)
																.addComponent(jButton1)
																.addComponent(jCheckBox2)))
																.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pack();
	}// </editor-fold>

	private void jButton1ActionPerformed(ActionEvent evt) {
		DATABASE_URL = jTextField1.getText();
		dbUserName = jTextField2.getText();
		dbUserPassword = jPasswordField1.getText();
		connectDB();
	}
	
	public void connectDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(DATABASE_URL, dbUserName, dbUserPassword);
			if(counter!=0)
				JOptionPane.showMessageDialog(null, "You Have ChangedYour Database To:\n" + DATABASE_URL,"From Database:",JOptionPane.INFORMATION_MESSAGE);
			counter++;
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "1) Check Database URL,\n2) Check User Name,\n3) Check Password.","From Database:",JOptionPane.ERROR_MESSAGE);
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public Connection getCon(){
		return con;
	}
	private void jCheckBox1ActionPerformed(ActionEvent evt) {                                           
		jTextField1.setEditable(true);
		jTextField2.setEditable(true);
		jCheckBox1.setEnabled(false);
		jCheckBox2.setEnabled(true);
		jCheckBox2.setSelected(false);
    }

	private void jCheckBox2ActionPerformed(ActionEvent evt) {                                           
		jTextField1.setEditable(false);
		jTextField2.setEditable(false);
		jCheckBox2.setEnabled(false);
		jCheckBox1.setEnabled(true);
		jCheckBox1.setSelected(false);
	}
}
