package server;

import java.awt.event.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
public class serverGUI extends JFrame{
	private JButton jButton1;
	private JScrollPane jScrollPane1;
	
	private Vector ClientSockets;

	protected JTextArea jTextArea1;
	serverGUI(){
		super("SERVER");
		initComponents();
	}
	public void setSockets(Vector cSockets){
		ClientSockets = cSockets;
	}
	private void initComponents() {
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jButton1 = new JButton();

		setBounds(new Rectangle(100, 100, 0, 0));
		setResizable(false);

		super.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(ClientSockets!= null)
					SendMessageToClientsServerIsShutDown();
				System.exit(1);
			}
		});

		jTextArea1.setBackground(new Color(219, 244, 61));
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1.setEditable(false);
		jTextArea1.append("\tSOCKET --> 5217");
		jScrollPane1.setViewportView(jTextArea1);

		jButton1.setText("STOP");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(ClientSockets!= null)
					SendMessageToClientsServerIsShutDown();
				System.exit(1);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addComponent(jScrollPane1))
										.addGroup(layout.createSequentialGroup()
												.addGap(170, 170, 170)
												.addComponent(jButton1)
												.addGap(0, 161, Short.MAX_VALUE)))
												.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
						.addContainerGap())
		);

		pack();
	}

	public void SendMessageToClientsServerIsShutDown(){
		for(int i=0;i<ClientSockets.size();i++){
			try {
				Socket tSoc=(Socket)ClientSockets.elementAt(i);
				DataOutputStream tdout=new DataOutputStream(tSoc.getOutputStream());
				tdout.writeUTF("#EXIT#");
			} catch (IOException e) {}
		}
	}
}
