package client;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.*;

public class connectServerGui extends JFrame{
	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JTextField jTextField2;

	private String host = null;
	private int port = 0;
	private int i = 0;

	connectServerGui(){
		super("Connection To Server:");
		initComponents();
		setVisible(true);
	}
	private void initComponents() {
		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jTextField1 = new JTextField();
		jTextField2 = new JTextField();
		jButton1 = new JButton();
		jButton2 = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(new Rectangle(500, 250, 0, 0));

		jLabel1.setFont(new Font("Tahoma", 0, 12));
		jLabel1.setText("Address:");
		
		jLabel2.setFont(new Font("Tahoma", 0, 12));
		jLabel2.setText("Port:");

		jTextField1.setFont(new Font("Tahoma", 0, 12));
		jTextField1.setText("localhost");

		jTextField2.setEditable(false);
		jTextField2.setFont(new Font("Tahoma", 0, 12));
		jTextField2.setHorizontalAlignment(JTextField.CENTER);
		jTextField2.setText("5217");

		jButton1.setText("CONNECT");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				host = jTextField1.getText();
				port = Integer.parseInt(jTextField2.getText());
				if(host != null && port!=0){
					try {
						AuthenticationGui auth = new AuthenticationGui(host,port);
						setVisible(false);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Host is OFF!\nOr You Have Entered Incorrect Host Address!","FATAL ERROR!",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		jButton2.setText("EXIT");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(1);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(Alignment.TRAILING)
												.addComponent(jLabel1)
												.addComponent(jLabel2))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(Alignment.LEADING)
														.addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
														.addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)))
														.addGroup(layout.createSequentialGroup()
																.addComponent(jButton1)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(jButton2)))
																.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
												.addComponent(jButton1)
												.addComponent(jButton2))
												.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		pack();
	}
	public static void main(String[] args) throws Exception{
		connectServerGui cServerGui = new connectServerGui();
	}
}
