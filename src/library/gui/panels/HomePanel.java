package library.gui.panels;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import library.database.DbManager;
import library.gui.Manager;

/**
 * @author Roland
 * Displays the Date, Session, and Version
 */
public class HomePanel extends JPanel implements ActionListener {
	private static JLabel logo;
	private static JLabel session;
	private static JButton setSession;
	private static JLabel info;
	private static SimpleDateFormat dateDispFormat;
	
	public HomePanel()	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//initialize image logo
		try {
			BufferedImage img = ImageIO.read(new File("logo.png"));
			logo = new JLabel(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Date[] sessionDates = DbManager.getSession();
		dateDispFormat = new SimpleDateFormat("MMM dd, yyyy");
		session = new JLabel("Session: "+dateDispFormat.format(sessionDates[0])+" - "+dateDispFormat.format(sessionDates[1]));
		session.setAlignmentX(Component.CENTER_ALIGNMENT);
		session.setFont(new Font("Arial", Font.PLAIN, 24));
		
		setSession = new JButton("Set Session");
		setSession.addActionListener(this);
		setSession.setAlignmentX(Component.CENTER_ALIGNMENT);
		setSession.setFont(new Font("Arial", Font.PLAIN, 16));
		
		info = new JLabel("Version "+Manager.version);
		info.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(logo);
		add(session);
		add(setSession);
		add(info);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == setSession)	{
			String d1, d2;
			d1 = JOptionPane.showInputDialog(this, "Enter Session start date in this format: MM/DD/YY");
			if(d1 != null)	{
				d2 = JOptionPane.showInputDialog(this, "Enter Session end date in this format: MM/DD/YY");
				if(d2 != null)	{
					SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
					try {
						DbManager.setSession(df.parse(d1), df.parse(d2));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					Date[] sessionDates = DbManager.getSession();
					session.setText("Session: "+dateDispFormat.format(sessionDates[0])+" - "+dateDispFormat.format(sessionDates[1]));
				}
			}
		}
	}
}
