package user.managment.desktop.query;
//QUERY CLASS
public abstract class Queries {

	// READ USERS
	protected final static String READ_USERS = "SELECT * FROM user ORDER BY idUser DESC";
	
	//GET TOTAL ROWS
	protected final static String TOTAL_ROWS = "SELECT COUNT(iduser) as total_row FROM user";

	// VERIFY USER
	protected final static String VERIFY_USER = "SELECT user.email FROM user WHERE user.email = ?";

	// INSERT USER
	protected final static String INSERT_USER = "INSERT INTO user(first_name, last_name, email, password) VALUES(?, ?, ?, ?)";

	// UPDATE USER
	protected final static String UPDATE_USER = "UPDATE user SET user.first_name = ?, user.last_name = ?, user.password = ? WHERE user.iduser = ?";

	// DELETE USER
	protected final static String DELETE_USER = "DELETE FROM user WHERE user.iduser = ?";

	// SIGN IN
	protected final static String SIGN_IN = "SELECT * FROM user WHERE user.email = ? AND user.Password = ?";

	// FORGOTTEN PASSWORD
	protected final static String FORGOTTEN_PASSWORD = "SELECT user.password FROM user WHERE user.email = ?";

}
