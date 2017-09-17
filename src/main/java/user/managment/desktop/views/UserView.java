package user.managment.desktop.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import user.managment.desktop.controller.UserController;
import user.managment.desktop.controller.Validations;
import user.managment.desktop.model.Error;
import user.managment.desktop.model.User;
import user.managment.desktop.security.AES256;

public class UserView extends JInternalFrame implements ActionListener, DocumentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int perPage = 8;

	private JTable userTable;

	private TableRowSorter<DefaultTableModel> rowSorter;

	private DefaultTableModel defaultTableModel;

	private JScrollPane scroll;
	private JLabel lblTotalRows;
	private JLabel lblLimit;
	private JLabel lblLimitNum;
	private JLabel lblTotalRowsNum;
	private JLabel lblSearch;
	private JLabel lblFirstName, lblLastName, lblEmail, lblPassword;

	private JLabel lblError;
	private JTextField txtPage;
	private JTextField txtSearch;
	private JPasswordField txtPassword;
	private JTextField txtLastName;
	private JTextField txtEmail;

	private JTextField txtFirstName;
	private JButton btnBack, btnNext;
	private JButton btnSave;

	private JButton btnUpdate, btnDelete, btnClean;
	private int lastPage = 0;
	private int page = 1;
	private int arrayIndex = 0;

	private UserController userController;
	private List<User> users;
	private User user;
	private Map<String, Error> errors;

	public UserView() {
		super("Users", true, false, false, true);
		this.setSize(849, 534);
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.instantiation();
		this.events();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnNext) {
			if (page < lastPage) {
				page += 1;

				if (page < lastPage)
					btnBack.setEnabled(true);
				else {
					btnNext.setEnabled(false);
				}

				btnBack.setEnabled(true);

				txtPage.setText(String.valueOf(page));
				generateTable("init");

			}
		}

		else if (ae.getSource() == btnBack) {
			if (page > 1) {
				page -= 1;

				if (page > 1)
					btnBack.setEnabled(true);
				else {
					btnBack.setEnabled(false);
				}

				btnNext.setEnabled(true);

				txtPage.setText(String.valueOf(page));
				generateTable("init");

			}
		}

		else if (ae.getSource() == btnSave) {
			String firstName = txtFirstName.getText();
			String lastName = txtLastName.getText();
			String email = txtEmail.getText();
			@SuppressWarnings("deprecation")
			String password = txtPassword.getText();

			errors = Validations.userValidation(firstName, lastName, email, password);
			if (errors.isEmpty()) {
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setPassword(AES256.encryption(password));

				try {
					User tempUser = userController.saveUser(user);
					if (tempUser != null) {
						users.add(0, tempUser);
						btnBack.setEnabled(false);
						btnNext.setEnabled(true);
						page = 1;
						cleanForm();
						JOptionPane.showMessageDialog(null, "User Inserted");
						generateTable("init");
					}

				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			} else {
				showValidations();
			}

		}

		else if (ae.getSource() == btnUpdate) {
			if (userTable.getSelectedRow() != -1) {
				String firstName = txtFirstName.getText();
				String lastName = txtLastName.getText();
				String email = txtEmail.getText();
				if (txtEmail.getText() != user.getEmail()) {
					JOptionPane.showMessageDialog(null,
							"Email can never be updated - It is going to be keep the same as before!");
					email = user.getEmail();
				}
				System.out.println(email);
				@SuppressWarnings("deprecation")
				String password = txtPassword.getText();
				errors = Validations.userValidation(firstName, lastName, email, password);

				if (errors.isEmpty()) {
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setPassword(AES256.encryption(password));

					try {
						boolean result = userController.updateUser(user);
						if (result) {
							users.set(arrayIndex, user);
							JOptionPane.showMessageDialog(null, "User Updated");
							userTable.clearSelection();
							cleanForm();
							generateTable("init");
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
						e.printStackTrace();
					}

				} else {
					showValidations();
				}
			} else
				JOptionPane.showMessageDialog(null, "Select an Item from the table!");
		}

		else if (ae.getSource() == btnDelete) {
			if (userTable.getSelectedRow() != -1) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Do you really want to delete it?",
						JOptionPane.WARNING_MESSAGE);

				if (choice == 0) {
					try {
						boolean result = userController.deleteUser(user.getIdUser());
						if (result) {
							users.remove(arrayIndex);
							JOptionPane.showMessageDialog(null, "User Deleted");
							userTable.clearSelection();
							cleanForm();
							generateTable("init");
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
						e.printStackTrace();
					}
				}
			} else
				JOptionPane.showMessageDialog(null, "Select an Item from the table!");
		}

		else if (ae.getSource() == btnClean) {
			txtFirstName.setText("");
			txtLastName.setText("");
			txtEmail.setText("");
			txtPassword.setText("");
			userTable.clearSelection();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent de) {
		filter();
	}

	public void cleanForm() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
	}

	public void events() {
		this.btnNext.addActionListener(this);
		this.btnBack.addActionListener(this);
		this.btnSave.addActionListener(this);
		this.btnClean.addActionListener(this);
		this.btnUpdate.addActionListener(this);
		this.btnDelete.addActionListener(this);
		this.txtSearch.getDocument().addDocumentListener(this);

		this.userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				int row = userTable.rowAtPoint(me.getPoint());
				arrayIndex = (int) userTable.getValueAt(row, 0);
				user.setIdUser((int) userTable.getValueAt(row, 1));
				txtFirstName.setText(userTable.getValueAt(row, 2).toString());
				txtLastName.setText(userTable.getValueAt(row, 3).toString());
				txtEmail.setText(userTable.getValueAt(row, 4).toString());
				user.setEmail(userTable.getValueAt(row, 4).toString());
				byte[] pass = (byte[]) userTable.getValueAt(row, 5);
				txtPassword.setText(AES256.decryption(new String(pass)));
			}
		});
	}

	public void filter() {

		rowSorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) userTable.getModel());
		userTable.setRowSorter(rowSorter);

		try {
			if (!txtSearch.getText().isEmpty()) {
				generateTable("filter");
				rowSorter.setRowFilter(RowFilter.regexFilter(txtSearch.getText(), 4));
			} else if (txtSearch.getText().length() == 0) {
				userTable.setRowSorter(null);
				generateTable("init");
			}

		} catch (Exception e) {
			System.err.println("Error ::: " + e.getMessage());
		}
	}

	public void generateTable(String action) {

		int offSet = (page - 1) * perPage;
		int limit = page * perPage;
		int totalRows = users.size();

		lastPage = (int) Math.ceil((totalRows / perPage) + 1);

		lblLimitNum.setText(String.valueOf(lastPage));
		lblTotalRowsNum.setText(String.valueOf(totalRows));
		txtPage.setText(String.valueOf(page));

		defaultTableModel.setRowCount(0);

		if (action == "init") {
			for (int i = offSet; i < limit; i++) {
				if (totalRows > i) {
					int id = users.get(i).getIdUser();
					String firstName = users.get(i).getFirstName();
					String lastName = users.get(i).getLastName();
					String email = users.get(i).getEmail();
					byte[] password = users.get(i).getPassword();

					Object[] data = { i, id, firstName, lastName, email, password };
					defaultTableModel.addRow(data);
				} else
					break;
			}
		}

		else {
			for (int i = 0; i < users.size(); i++) {
				int id = users.get(i).getIdUser();
				String firstName = users.get(i).getFirstName();
				String lastName = users.get(i).getLastName();
				String email = users.get(i).getEmail();
				byte[] password = users.get(i).getPassword();

				Object[] data = { i, id, firstName, lastName, email, password };
				defaultTableModel.addRow(data);

			}
		}

	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		filter();
	}

	public void instantiation() {
		userController = new UserController();
		try {
			users = userController.readUser();
		} catch (SQLException e) {
			System.err.println("Error ::: " + e.getMessage());
			e.printStackTrace();
		}

		user = new User();

		lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(10, 23, 70, 15);
		getContentPane().add(lblFirstName);

		lblEmail = new JLabel("email");
		lblEmail.setBounds(10, 62, 70, 15);
		getContentPane().add(lblEmail);

		lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(388, 23, 70, 15);
		getContentPane().add(lblLastName);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(388, 62, 70, 15);
		getContentPane().add(lblPassword);

		lblTotalRows = new JLabel("Total Rows");
		lblTotalRows.setBounds(10, 371, 89, 20);
		this.getContentPane().add(lblTotalRows);

		lblSearch = new JLabel("Search By Email");
		lblSearch.setBounds(10, 416, 140, 15);
		this.getContentPane().add(lblSearch);

		lblLimit = new JLabel("Pages");
		lblLimit.setBounds(388, 374, 46, 14);
		getContentPane().add(lblLimit);

		lblLimitNum = new JLabel("1");
		lblLimitNum.setBounds(444, 374, 46, 14);
		getContentPane().add(lblLimitNum);

		lblTotalRowsNum = new JLabel("1");
		lblTotalRowsNum.setBounds(104, 374, 46, 14);
		getContentPane().add(lblTotalRowsNum);

		txtPage = new JTextField("1");
		txtPage.setBounds(228, 367, 82, 28);
		txtPage.setEditable(false);
		this.getContentPane().add(txtPage);
		txtPage.setColumns(10);

		txtSearch = new JTextField();
		txtSearch.setBounds(160, 409, 267, 28);
		getContentPane().add(txtSearch);
		txtSearch.setColumns(10);

		btnBack = new JButton("<");
		btnBack.setEnabled(false);
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(new Color(165, 42, 42));
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnBack.setBounds(160, 367, 56, 28);
		this.getContentPane().add(btnBack);

		btnNext = new JButton(">");
		btnNext.setForeground(Color.WHITE);
		btnNext.setBackground(new Color(165, 42, 42));
		btnNext.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNext.setBounds(322, 367, 56, 28);
		this.getContentPane().add(btnNext);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(698, 366, 2, 135);
		this.getContentPane().add(separator);

		btnUpdate = new JButton("Update");
		btnUpdate.setHorizontalAlignment(SwingConstants.LEFT);
		btnUpdate.setIcon(new ImageIcon(UserView.class.getResource("/images/icons/update.png")));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setBackground(new Color(165, 42, 42));
		btnUpdate.setBounds(710, 364, 107, 35);
		this.getContentPane().add(btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setIcon(new ImageIcon(UserView.class.getResource("/images/icons/delete.png")));
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setBackground(new Color(165, 42, 42));
		btnDelete.setBounds(710, 409, 107, 35);
		this.getContentPane().add(btnDelete);

		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(UserView.class.getResource("/images/icons/save.png")));
		btnSave.setForeground(Color.WHITE);
		btnSave.setBackground(new Color(0, 128, 0));
		btnSave.setBounds(710, 455, 107, 35);
		getContentPane().add(btnSave);

		btnClean = new JButton("Clean");
		btnClean.setIcon(new ImageIcon(UserView.class.getResource("/images/icons/clean.png")));
		btnClean.setForeground(Color.WHITE);
		btnClean.setBackground(new Color(138, 43, 226));
		btnClean.setBounds(581, 367, 107, 35);
		getContentPane().add(btnClean);

		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(489, 55, 267, 28);
		getContentPane().add(txtPassword);

		txtLastName = new JTextField();
		txtLastName.setColumns(10);
		txtLastName.setBounds(489, 16, 267, 28);
		getContentPane().add(txtLastName);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(111, 55, 267, 28);
		getContentPane().add(txtEmail);

		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(111, 16, 267, 28);
		getContentPane().add(txtFirstName);

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(10, 88, 807, 44);
		getContentPane().add(lblError);

		userTable = new JTable();
		userTable.setRowHeight(30);
		userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroll = new JScrollPane(userTable);
		scroll.setBounds(0, 94, 833, 259);
		this.getContentPane().add(scroll);

		String columns[] = { "#", "ID", "FirstName", "LastName", "Email", "Password" };
		defaultTableModel = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		defaultTableModel.setColumnIdentifiers(columns);
		if (!users.isEmpty())
			generateTable("init");
		userTable.setModel(defaultTableModel);

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(100, 0, 4, 85));
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		filter();
	}

	public void showValidations() {
		String msg = "";
		for (Map.Entry<String, Error> entry : errors.entrySet()) {
			msg += "* " + entry.getValue().getMessage() + " \n ";
		}

		JOptionPane.showMessageDialog(null, msg);
	}
}
