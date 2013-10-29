package org.smash.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextPane;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.smash.res.SmashProperties;
/**
 * The dialog that appears when you launch the game for the first time
 * @author LordOfBees
 *
 */
public class StartDialog extends JDialog {

	private static final long serialVersionUID = 8709498037610011688L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUser;
	private JTextPane lblBoom;
	
	private SmashProperties properties;

	/**
	 * Create the dialog.
	 */
	public StartDialog(SmashProperties property) {
		this.properties = property;
		setResizable(false);
		setTitle("Smash It - Welcome");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setSize(500, 183);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JTextPane txt = new JTextPane();
		txt.setFont(new Font("Simplified Arabic Fixed", Font.BOLD, 12));
		txt.setEditable(false);
		txt.setBackground(Color.LIGHT_GRAY);
		txt.setText("Welcome, it's the first time you play Smash It !\nPlease choose an username, but be aware it can NOT be changed after.");
		txt.setBounds(10, 11, 474, 48);
		contentPanel.add(txt);
		
		txtUser = new JTextField();
		txtUser.setText("User");
		txtUser.setFont(new Font("Simplified Arabic Fixed", Font.BOLD, 12));
		txtUser.setToolTipText("Your Username");
		txtUser.setBounds(240, 100, 171, 20);
		contentPanel.add(txtUser);
		txtUser.setColumns(12);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 120, 494, 33);
			contentPanel.add(buttonPane);
			buttonPane.setBackground(Color.LIGHT_GRAY);
			buttonPane.setBorder(null);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(txtUser.getText().length()>12 || txtUser.getText().length()<3){
							lblBoom.setText("Your username must contains at least 3 characters and up to 12 !");
							txtUser.selectAll();
							return;
						}
						dispose();
						properties.setUsername(txtUser.getText());
						SFrame frame = new SFrame(properties);
						frame.setVisible(true);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		JLabel lblYourUsername = new JLabel("Your Username :");
		lblYourUsername.setFont(new Font("Simplified Arabic Fixed", Font.ITALIC, 12));
		lblYourUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblYourUsername.setBounds(35, 100, 194, 20);
		contentPanel.add(lblYourUsername);
		
		lblBoom = new JTextPane();
		lblBoom.setForeground(Color.RED);
		lblBoom.setFont(new Font("Simplified Arabic Fixed", Font.BOLD, 12));
		lblBoom.setEditable(false);
		lblBoom.setBackground(Color.LIGHT_GRAY);
		lblBoom.setBounds(10, 61, 474, 33);
		contentPanel.add(lblBoom);
	}
}
