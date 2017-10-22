package user.managment.desktop.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import user.managment.desktop.db.DBConnection;

public class Desktop extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JDesktopPane container;

	private UserView userView;
	
	public static boolean isTest = false;

	public Desktop() {
		super("Desktop User Managment...");
		this.setSize(900, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.instantiation();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				if(!isTest) DBConnection.closeConnection();
			}
		});
	}

	public void instantiation() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 884, 0 };
		gridBagLayout.rowHeights = new int[] { 540, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		this.container = new JDesktopPane() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D graphics = (Graphics2D) g.create();
				Image img = new ImageIcon(Desktop.class.getResource("/images/background.jpg")).getImage();
				graphics.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				graphics.dispose();
			}
		};

		container.setBackground(Color.GRAY);
		GridBagConstraints gbcContainer = new GridBagConstraints();
		gbcContainer.weighty = 1.0;
		gbcContainer.weightx = 1.0;
		gbcContainer.ipady = 1;
		gbcContainer.ipadx = 1;
		gbcContainer.fill = GridBagConstraints.BOTH;
		gbcContainer.gridx = 0;
		gbcContainer.gridy = 0;
		this.getContentPane().add(container, gbcContainer);
		container.setLayout(null);

		userView = new UserView();
		userView.setVisible(true);
		container.add(userView);

	}

}
